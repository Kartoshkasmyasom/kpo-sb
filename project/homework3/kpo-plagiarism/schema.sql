CREATE TABLE IF NOT EXISTS submissions (
    id           BIGSERIAL PRIMARY KEY,
    student_id   TEXT      NOT NULL,
    work_id TEXT     NOT NULL,
    file_path    TEXT      NOT NULL,
    creation_time   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS reports (
    id            BIGSERIAL PRIMARY KEY,
    submission_id BIGINT      NOT NULL REFERENCES submissions(id) ON DELETE CASCADE,
    contains_plagiarism BOOLEAN    NOT NULL,
    status        TEXT        NOT NULL,
    creation_time    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);