package main

import (
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"encoding/json"
)
	type uploadResponse struct {
	    FilePath string `json:"file_path"`
	}


	func handleUploadFile(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodPost {
			http.Error(w, "method is not allowed", http.StatusMethodNotAllowed)
			return
		}
		
		r.Body = http.MaxBytesReader(w, r.Body, 20<<20)
		err := r.ParseMultipartForm(20 << 20)
		if err != nil {
			http.Error(w, "failed to parse form: " + err.Error(), http.StatusBadRequest)
			return
		}

		file, header, err := r.FormFile("file")
		if err != nil {
			http.Error(w, "file field is required: " + err.Error(), http.StatusBadRequest)
			return
		}

		defer file.Close()

		dstPath := "./data/" + header.Filename
		dst, err := os.Create(dstPath)
		if err != nil {
			http.Error(w, "failed to create file: " + err.Error(), http.StatusInternalServerError)
			return
		}
		defer dst.Close()

		_, err = io.Copy(dst, file)
		if err != nil {
			http.Error(w, "failed to save file " + err.Error(), http.StatusInternalServerError)
		}

		resp := uploadResponse{
    		FilePath: dstPath,
		}	

		w.Header().Set("Content-Type", "application/json")
		err = json.NewEncoder(w).Encode(resp)
    	if err != nil {
			http.Error(w, "failed to write response: ", http.StatusInternalServerError)
		}
	}

func main() {
	http.HandleFunc("/ping", func(w http.ResponseWriter, r *http.Request){
		fmt.Fprintln(w, "pong")
	})

	http.HandleFunc("/files", handleUploadFile)

	addr := ":8081"
	log.Println("file-storage service starting on", addr)

	err := http.ListenAndServe(addr, nil)
	if err != nil {
		log.Fatal("server error:", err)
	}
}