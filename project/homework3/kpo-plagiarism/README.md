# Система проверки плагиата по файлам

Учебный проект на Go с микросервисной архитектурой:

- **file-storage** — хранение файлов студентов
- **file-analysis** — запись сдач в БД и проверка на плагиат
- **gateway** — единая входная точка для студентов и преподавателей
- **PostgreSQL** — хранение данных о сдачах и отчётах

Все сервисы запускаются через `docker compose`.

---

## Архитектура

### 1. file-storage (порт 8081)

Отвечает только за приём и сохранение файлов.

**Эндпоинты:**

- `GET /ping` — health-check, отвечает `pong`
- `POST /files`  
  - Принимает: `multipart/form-data`, поле `file`  
  - Сохраняет файл в директорию `/app/data`  
  - Возвращает JSON:

    ```json
    {
      "file_path": "./data/<имя_файла>"
    }
    ```

---

### 2. file-analysis (порт 8082)

Сервис работы с БД и логики плагиата.  
Использует PostgreSQL и таблицы из `schema.sql`.

**Основные сущности:**

- `submissions` — сдачи работ  
  - `id`
  - `student_id`
  - `work_id`
  - `file_path`
  - `creation_time`
- `reports` — отчёты о проверке  
  - `id`
  - `submission_id`
  - `contains_plagiarism`
  - `status`
  - `creation_time`

**Логика плагиата:**

Работа считается плагиатом, если в таблице `submissions` существует **более ранняя** сдача:

- с тем же `work_id`,
- с тем же `file_path`,
- от **другого** `student_id`.

Т.е. если второй студент сдаёт тот же файл по той же работе → `contains_plagiarism = true`.

**Эндпоинты:**

- `GET /ping` — health-check, отвечает `analysis pong`

- `POST /analysis/check`  
  - Принимает JSON:

    ```json
    {
      "student_id": "b1",
      "work_id": "d1",
      "file_path": "./data/test.txt"
    }
    ```

  - Создаёт запись в `submissions`
  - Ищет более ранние сдачи по условию выше
  - Создаёт запись в `reports`
  - Возвращает JSON:

    ```json
    {
      "SubmissionID": 1,
      "ReportID": 1,
      "ContainsPlagiarism": false,
      "Status": "done"
    }
    ```

- `GET /works/{work_id}/reports`  
  - Возвращает список сдач и отчётов по работе:

    ```json
    [
      {
        "submission_id": 1,
        "student_id": "b1",
        "work_id": "d1",
        "created_at": "...",
        "report_id": 1,
        "contains_plagiarism": false,
        "status": "done",
        "checked_at": "..."
      },
      ...
    ]
    ```

---

### 3. gateway (порт 8080)

Единственная точка входа для клиентов.  
Общается с:

- `file-storage` (через `STORAGE_URL`)
- `file-analysis` (через `ANALYSIS_URL`)

**Эндпоинты:**

- `GET /ping` — health-check, отвечает `gateway pong`

- `POST /api/works/upload`  
  Полный сценарий сдачи работы студентом.

  - Принимает `multipart/form-data`:

    - `student_id` — ID студента
    - `work_id` — ID работы
    - `file` — файл с решением

  - Шаги внутри:
    1. Отправляет файл в `file-storage /files`
    2. Получает `file_path`
    3. Отправляет запрос в `file-analysis /analysis/check` с:
       ```json
       {
         "student_id": "...",
         "work_id": "...",
         "file_path": "..."
       }
       ```
    4. Возвращает наружу ответ `file-analysis`.

  - Пример ответа:

    ```json
    {
      "SubmissionID": 1,
      "ReportID": 1,
      "ContainsPlagiarism": false,
      "Status": "done"
    }
    ```

- `GET /api/works/{work_id}/reports`  
  Эндпоинт для преподавателя.  
  Проксирует запрос в `file-analysis /works/{work_id}/reports` и возвращает список всех сдач по работе.

---

## Запуск через Docker

### Требования

- Docker
- Docker Compose

### Команда запуска

В корне проекта:

```bash
docker compose up --build
