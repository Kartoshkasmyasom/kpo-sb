package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"
	"strings"
	"os"

	_ "github.com/lib/pq"
)

var db *sql.DB

type checkRequest struct {
	StudentID string `json:"student_id"`
	WorkID string `json:"work_id"`
	FilePath string `json:"file_path"`
}

type checkResponse struct {
	SubmissionID int64 `json:submission_id`
	ReportID int64 `json:report_id`
	ContainsPlagiarism bool `json:contains_plagiarism`
	Status string `json:status`
}

type reportItem struct {
	SubmissionID  int64 `json:"submission_id"`
	StudentID  string `json:"student_id"`
	WorkID	string `json:"assignment_id"`
	CreatedAt time.Time `json:"created_at"`
	ReportID  int64  `json:"report_id"`
	ContainsPlagiarism bool      `json:"is_plagiarized"`
	Status  string    `json:"status"`
	CheckedAt time.Time `json:"checked_at"`
}

func handleCheck(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "method is not allowed", http.StatusMethodNotAllowed)
		return
	}
		
	var req checkRequest
	err := json.NewDecoder(r.Body).Decode(&req)
	if err != nil {
		http.Error(w, "error during parsing json: " + err.Error(), http.StatusBadRequest)
		return
	}
	fmt.Println(req.StudentID)
	fmt.Println(req.WorkID)
	fmt.Println(req.FilePath)

	if req.StudentID == "" || req.WorkID == "" || req.FilePath == "" {
		http.Error(w, "student_id, work_id and file_path are required", http.StatusBadRequest)
		return
	}

	var submissionID int64
	err = db.QueryRow(
    	`INSERT INTO submissions (student_id, work_id, file_path)
     	VALUES ($1, $2, $3)
     	RETURNING id`,
    	req.StudentID, req.WorkID, req.FilePath,
	).Scan(&submissionID)

	if err != nil {
    	log.Println("failed to insert submission:", err)
    	http.Error(w, "failed to create submission", http.StatusInternalServerError)
    	return
	}

	var count int
	err = db.QueryRow(
    	`SELECT COUNT(*)
     	FROM submissions
     	WHERE work_id = $1
       	AND file_path = $2
       	AND student_id <> $3
       	AND creation_time < (SELECT creation_time FROM submissions WHERE id = $4)`,
    	req.WorkID, req.FilePath, req.StudentID, submissionID,
	).Scan(&count)

	if err != nil {
		log.Println("failed to check plagiarism:", err)
		http.Error(w, "failed to check plagiarism", http.StatusInternalServerError)
		return
	}

	containsPlagiarism := count > 0
	status := "done"

	var reportID int64
	err = db.QueryRow(
		`INSERT INTO reports (submission_id, contains_plagiarism, status)
		VALUES ($1, $2, $3)
		RETURNING id`,
		submissionID, containsPlagiarism, status,
	).Scan(&reportID)

	if err != nil {
		log.Println("failed to insert report:", err)
		http.Error(w, "failed to create report", http.StatusInternalServerError)
		return
	}

	resp := checkResponse{
		SubmissionID: submissionID,
		ReportID: reportID,
		ContainsPlagiarism: containsPlagiarism,
		Status: status,
	}

	w.Header().Set("Content-Type", "application/json")
	err = json.NewEncoder(w).Encode(resp)
    if err != nil {
		http.Error(w, "failed to write response: ", http.StatusInternalServerError)
		return
	}
}

func handleGetReports(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}

	parts := strings.Split(r.URL.Path, "/")
	if len(parts) != 4 || parts[1] != "works" || parts[3] != "reports" {
		http.NotFound(w, r)
		return
	}

	workID := parts[2]
	if workID == "" {
		http.Error(w, "workID is required", http.StatusBadRequest)
		return
	}

	rows, err := db.Query(`
		SELECT
			s.id,
			s.student_id,
			s.work_id,
			s.creation_time,
			r.id,
			r.contains_plagiarism,
			r.status,
			r.creation_time
		FROM submissions s
		JOIN reports r ON r.submission_id = s.id
		WHERE s.work_id = $1
		ORDER BY s.creation_time, r.creation_time	
	`, workID)
	if err != nil {
		log.Println("failed to query reports:", err)
		http.Error(w, "failed to query reports", http.StatusInternalServerError)
		return
	}
	defer rows.Close()

	var items []reportItem

	for rows.Next() {
		var it reportItem
		err = rows.Scan(
			&it.SubmissionID,
			&it.StudentID,
			&it.WorkID,
			&it.CreatedAt,
			&it.ReportID,
			&it.ContainsPlagiarism,
			&it.Status,
			&it.CheckedAt,
		)
		if err != nil {
			log.Println("failed to scan row:", err)
			http.Error(w, "failed to read data", http.StatusInternalServerError)
			return
		}

		items = append(items, it)
	}

	if err = rows.Err(); err != nil {
		log.Println("rows error:", err)
		http.Error(w, "failed to read data", http.StatusInternalServerError)
		return
	}

	w.Header().Set("Content-Type", "application/json")
	err = json.NewEncoder(w).Encode(items)
	if err != nil {
		http.Error(w, "failed to write response: "+err.Error(), http.StatusInternalServerError)
		return
	}
}

func main() {
	connection := os.Getenv("DATABASE_URL")
	if connection == "" {
		connection = "postgres://kpo:kpo@localhost:5432/plagiarism?sslmode=disable"
	}
	var err error
	db, err = sql.Open("postgres", connection)
	if err != nil {
		log.Fatal("failed to open db: ", err)
	}

	err = db.Ping()
	if err != nil {
		log.Fatal("failed to connect to db: ", err)
	}
	log.Println("connected to postgres")

	http.HandleFunc("/ping", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintln(w, "analysis pong")
	})

	http.HandleFunc("/analysis/check", handleCheck)

	http.HandleFunc("/works/", handleGetReports)
	
	addr := ":8082"
	log.Println("file-analysis service starting on", addr)

	err = http.ListenAndServe(addr, nil)
	if err != nil {
		log.Fatal("server error:", err)
	}
}