FROM golang:1.23-alpine AS build

WORKDIR /app

COPY go.mod ./
RUN go mod download

COPY . .

RUN go build -o gateway

FROM alpine:3.20

WORKDIR /app

COPY --from=build /app/gateway /app/gateway

EXPOSE 8080

CMD ["/app/gateway"]
