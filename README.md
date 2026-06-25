> **Real-Time CDC-Based Log Monitoring & Observability Platform**

---

## 📌 Features

- 📂 Reads thousands of log files from a local directory
- ⚡ Imports logs into PostgreSQL in real time
- 🔍 Parses log fields such as:
  - Log Level
  - User
  - IP Address
  - Endpoint
  - Action
  - Error Message
  - Response Time
- 🔄 Streams database changes using Debezium CDC
- 📡 Publishes events to Apache Kafka
- 🔎 Indexes logs into Elasticsearch
- 📊 Visualizes logs in Grafana dashboards
- 📈 Response time monitoring
- 🚨 Error tracking
- 👤 User activity monitoring
- 🌐 Endpoint analytics

---

# 🏗️ Architecture

```
                 Log Files
              (C:\APP_LOGS)
                     │
                     ▼
            Spring Boot Parser
                     │
                     ▼
               PostgreSQL
               (app_log)
                     │
                     ▼
          Debezium CDC Connector
                     │
                     ▼
          Apache Kafka Topic
          pg.public.app_log
                     │
                     ▼
      Elasticsearch Sink Connector
                     │
                     ▼
            Elasticsearch
        pg_public_app_log
                     │
                     ▼
               Grafana
          Live Dashboards
```

---

# 🛠️ Tech Stack

| Technology | Purpose |
|------------|----------|
| Java | Programming Language |
| Spring Boot | Backend Application |
| PostgreSQL | Database |
| Debezium | Change Data Capture |
| Apache Kafka | Event Streaming |
| Kafka Connect | Connector Framework |
| Elasticsearch | Search & Indexing |
| Grafana | Dashboard & Visualization |
| Maven | Dependency Management |

---

# 📁 Project Structure

```
src
 └── main
      ├── java
      │     └── com.example.cdc
      │             ├── AppLog.java
      │             ├── AppLogRepository.java
      │             ├── LogController.java
      │             └── CdcApplication.java
      │
      └── resources
            ├── application.properties
            └── ...
```

---

# ⚙️ How It Works

### 1️⃣ Log Import

Spring Boot continuously reads log files from

```
C:\APP_LOGS
```

---

### 2️⃣ Log Parsing

Each log is parsed into structured fields:

- Log Level
- User
- IP Address
- Endpoint
- Action
- Error Message
- Response Time

---

### 3️⃣ PostgreSQL Storage

Parsed logs are inserted into the

```
app_log
```

table.

---

### 4️⃣ Debezium CDC

Debezium monitors PostgreSQL's Write Ahead Log (WAL).

Whenever a new row is inserted:

```
INSERT INTO app_log
```

Debezium immediately captures the change.

---

### 5️⃣ Apache Kafka

Debezium publishes every database change into

```
pg.public.app_log
```

Kafka Topic.

---

### 6️⃣ Elasticsearch

Kafka Connect automatically transfers Kafka events into

```
pg_public_app_log
```

Elasticsearch Index.

---

### 7️⃣ Grafana

Grafana reads data from Elasticsearch and creates real-time dashboards.

---

# 📊 Dashboards

The project includes dashboards for:

- Total Logs
- Total Errors
- Error Rate
- Average Response Time
- Top Users
- Top Endpoints
- Top IP Addresses
- Live Logs
- Error Trend
- Response Time Analytics

---

# 🚀 Running the Project

## Step 1

Start PostgreSQL

---

## Step 2

Start Elasticsearch

```bash
elasticsearch.bat
```

---

## Step 3

Start Kafka

```bash
kafka-server-start.bat config/server.properties
```

---

## Step 4

Start Kafka Connect

```bash
connect-standalone.bat ^
config/connect-standalone.properties ^
config/postgres-source.properties ^
config/elastic-sink.properties
```

---

## Step 5

Run Spring Boot

Start the application from IntelliJ IDEA or using Maven.

---

## Step 6

Import Logs

```
http://localhost:8080/start-import
```

---

## Step 7

Check Import Progress

```
http://localhost:8080/import-status
```

---

## Step 8

Open Grafana

```
http://localhost:3000
```

---



# 📈 Future Enhancements

- AI-Based Anomaly Detection
- Kafka Streams Processing
- Email Alerts
- Slack Notifications
- Geo-IP Tracking
- Predictive Analytics
- Security Monitoring
- Docker Deployment
- Kubernetes Support

---

# 👨‍💻 Author

** Tisya Singh**

Computer Science Engineering

---

# ⭐ If you found this project useful, consider giving it a star!
