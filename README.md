UCFR Spring Boot MVP with React Dashboard
=========================================
This is a hackathon-ready MVP for Universal Credit File Reconciliation (UCFR).

Backend: Java Spring Boot (in src/main/java)
Frontend: React (Vite) in frontend/

Run backend:
  mvn clean package
  java -jar target/ucfr-mvp-0.0.1-SNAPSHOT.jar

Or using Docker (backend only):
  docker build -t ucfr-mvp .
  docker run -p 8080:8080 ucfr-mvp

Frontend (development):
  cd frontend
  npm install
  npm run dev

The backend exposes:
  POST /api/v1/reconcile
  GET  /api/v1/consumer/{token}/canonical

Sample reports at: src/main/resources/static/sample_reports.json
