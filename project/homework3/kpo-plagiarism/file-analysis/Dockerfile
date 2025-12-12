FROM golang:1.23-alpine AS build

WORKDIR /app

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN go build -o file-analysis

FROM alpine:3.20

WORKDIR /app

COPY --from=build /app/file-analysis /app/file-analysis

EXPOSE 8082

CMD ["/app/file-analysis"]
