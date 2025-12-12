package main

import (
	"time"
)

type Submission struct {
	ID int64 `json:"id"`
	StudentID string `json:"student_id"`
	WorkID string `json:"work_id"`
	FilePath string `json:"file_path"`
	CreationTime time.Time `json:"creation_time"`
}

type Report struct {
	ID int64 `json:"id"`
	SubmissionID string `json:"submission_id"`
	ContainsPlagiarism bool `json:"contains_plagiarism"`
	Status string `json:"status"`
	CreationTime time.Time `json:"creation_time"`
}

