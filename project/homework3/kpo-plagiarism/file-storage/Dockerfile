# этап сборки
FROM golang:1.23-alpine AS build

WORKDIR /app

COPY go.mod ./
RUN go mod download

COPY . .

RUN go build -o file-storage

FROM alpine:3.20

WORKDIR /app

RUN mkdir -p /app/data

COPY --from=build /app/file-storage /app/file-storage

EXPOSE 8081

CMD ["/app/file-storage"]
