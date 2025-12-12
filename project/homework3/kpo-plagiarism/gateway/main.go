package main

import (
	"bytes"
	"io"
	"log"
	"mime/multipart"
	"net/http"
	"encoding/json"
	"strings"
	"os"
)

type uploadResponse struct {
	StudentID string `json:"student_id"`
	WorkID string `json:"work_id"`
}

type storageResponse struct {
	FilePath string `json:"file_path"`
}

type analysisRequest struct {
	StudentID string `json:"student_id"`
	WorkID string `json:"work_id"`
	FilePath string `json:"file_path"`
}

type analysisResponse struct {
	SubmissionID int64  `json:"SubmissionID"`
	ReportID int64  `json:"ReportID"`
	ContainsPlagiarism bool   `json:"ContainsPlagiarism"`
	Status string `json:"Status"`
}

var storageURL string
var analysisURL string

func handleUploadWork(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}

	err := r.ParseMultipartForm(20 << 20) // 20 MB
	if err != nil {
		http.Error(w, "failed to parse multipart form: "+err.Error(), http.StatusBadRequest)
		return
	}

	studentID := r.FormValue("student_id")
	workID := r.FormValue("work_id")

	if studentID == "" || workID == "" {
		http.Error(w, "student_id and work_id are required", http.StatusBadRequest)
		return
	}

	srcFile, header, err := r.FormFile("file")
	if err != nil {
		http.Error(w, "file field is required: "+err.Error(), http.StatusBadRequest)
		return
	}
	defer srcFile.Close()

	var buf bytes.Buffer
	mw := multipart.NewWriter(&buf)

	part, err := mw.CreateFormFile("file", header.Filename)
	if err != nil {
		http.Error(w, "failed to create form file: "+err.Error(), http.StatusInternalServerError)
		return
	}

	_, err = io.Copy(part, srcFile)
	if err != nil {
		http.Error(w, "failed to copy file: "+err.Error(), http.StatusInternalServerError)
		return
	}

	err = mw.Close()
	if err != nil {
		http.Error(w, "failed to close multipart writer: "+err.Error(), http.StatusInternalServerError)
		return
	}

	reqStorage, err := http.NewRequest(http.MethodPost, storageURL+"/files", &buf)
	if err != nil {
		http.Error(w, "failed to create request to storage: "+err.Error(), http.StatusInternalServerError)
		return
	}
	reqStorage.Header.Set("Content-Type", mw.FormDataContentType())

	client := &http.Client{}
	respStorage, err := client.Do(reqStorage)
	if err != nil {
		http.Error(w, "failed to call storage: "+err.Error(), http.StatusBadGateway)
		return
	}
	defer respStorage.Body.Close()

	var sresp storageResponse
	err = json.NewDecoder(respStorage.Body).Decode(&sresp)
	if err != nil {
		http.Error(w, "failed to parse storage response: "+err.Error(), http.StatusInternalServerError)
		return
	}

	areq := analysisRequest{
		StudentID: studentID,
		WorkID:    workID,
		FilePath:  sresp.FilePath,
	}

	var abuf bytes.Buffer
	err = json.NewEncoder(&abuf).Encode(areq)
	if err != nil {
		http.Error(w, "failed to encode analysis request: "+err.Error(), http.StatusInternalServerError)
		return
	}

	reqAnalysis, err := http.NewRequest(http.MethodPost, analysisURL+"/analysis/check", &abuf)
	if err != nil {
		http.Error(w, "failed to create request to analysis: "+err.Error(), http.StatusInternalServerError)
		return
	}
	reqAnalysis.Header.Set("Content-Type", "application/json")

	respAnalysis, err := client.Do(reqAnalysis)
	if err != nil {
		http.Error(w, "failed to call analysis: "+err.Error(), http.StatusBadGateway)
		return
	}
	defer respAnalysis.Body.Close()

	if respAnalysis.StatusCode >= 400 {
		bodyBytes, _ := io.ReadAll(respAnalysis.Body)
		log.Println("analysis returned error:", respAnalysis.StatusCode, string(bodyBytes))
		http.Error(w, "analysis service error", http.StatusBadGateway)
		return
	}


	var aresp analysisResponse
	err = json.NewDecoder(respAnalysis.Body).Decode(&aresp)
	if err != nil {
		http.Error(w, "failed to parse analysis response: "+err.Error(), http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	err = json.NewEncoder(w).Encode(aresp)
	if err != nil {
		http.Error(w, "failed to write response: "+err.Error(), http.StatusInternalServerError)
		return
	}
}

func handleGetReports(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}

	parts := strings.Split(r.URL.Path, "/")
	if len(parts) != 5 || parts[1] != "api" || parts[2] != "works" || parts[4] != "reports" {
		http.NotFound(w, r)
		return
	}

	workID := parts[3]
	if workID == "" {
		http.Error(w, "work_id is required", http.StatusBadRequest)
		return
	}

	url := analysisURL + "/works/" + workID + "/reports"

	reqAnalysis, err := http.NewRequest(http.MethodGet, url, nil)
	if err != nil {
		http.Error(w, "failed to create request to analysis: "+err.Error(), http.StatusInternalServerError)
		return
	}

	client := &http.Client{}
	respAnalysis, err := client.Do(reqAnalysis)
	if err != nil {
		http.Error(w, "failed to call analysis: "+err.Error(), http.StatusBadGateway)
		return
	}
	defer respAnalysis.Body.Close()

	if respAnalysis.StatusCode >= 400 {
		bodyBytes, _ := io.ReadAll(respAnalysis.Body)
		log.Println("analysis returned error:", respAnalysis.StatusCode, string(bodyBytes))
		http.Error(w, "analysis service error", http.StatusBadGateway)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	_, _ = io.Copy(w, respAnalysis.Body)
}




func main() {
	storageURL = os.Getenv("STORAGE_URL")
	if storageURL == "" {
		storageURL = "http://127.0.0.1:8081"
	}

	analysisURL = os.Getenv("ANALYSIS_URL")
	if analysisURL == "" {
		analysisURL = "http://127.0.0.1:8082"
	}

	http.HandleFunc("/ping", func(w http.ResponseWriter, r *http.Request) {
		w.Write([]byte("gateway pong\n"))
	})

	http.HandleFunc("/api/works/upload", handleUploadWork)
	http.HandleFunc("/api/works/", handleGetReports)

	addr := ":8080"
	log.Println("gateway starting on", addr)

	err := http.ListenAndServe(addr, nil)
	if err != nil {
		log.Fatal("server error:", err)
	}
}
