# AI-Based Space Intelligence & Geospatial Data Reliability Management System
### Industry Partner: Xovian Aerospace &bull; Operational Centre: ABC National Geospatial Intelligence Centre
**Micro Project 09 &bull; Java Enterprise Technologies &bull; Academic Year 2026**

---

## 1. Project Overview
Modern Earth observation constellations generate gigabytes of daily satellite, drone, and ground telemetry. However, real-world feeds frequently suffer from missing packets, sensor calibration drift, atmospheric interference, and conflicting readings.

This application is an **Enterprise Java System** designed to:
1. Ingest telemetry from 6 source classes: Earth Observation Satellites, Weather Radars, Recon UAVs, GPS Stations, GIS databases, and Ground Environmental Sensors.
2. Maintain operational data across **16 normalized relational database tables**.
3. Implement a **Rule-Based Explainable AI Confidence Engine** that calculates dynamic reliability, consistency, and confidence scores ($0.0 - 100.0\%$) with transparent decision justifications.
4. Detect **5 classes of geospatial anomalies** (Missing Data, Duplicate Ingests, Conflicting Telemetry, Outlier Values, Delayed Updates).
5. Provide a sleek **20-Screen Aerospace Control Station** in Java Swing, alongside an embedded **Module V Web Dashboard** on `http://localhost:8080/dashboard`.

---

## 2. Quick Start (1-Click Run on Windows)
No complex installations (e.g., MySQL, Apache Tomcat, or Maven) are required. Everything is self-contained.

### Option A: Double-Click Batch File
Simply double-click:
```
run.bat
```
This compiles the code into `bin/`, seeds `space_intel.db` with realistic Xovian Aerospace data, launches the live Web Server at `http://localhost:8080/dashboard`, and displays the animated **Splash Screen**.

### Option B: Terminal Command (PowerShell)
```powershell
.\run.bat
```

---

## 3. Pre-Configured Personas & Demo Accounts
On **Screen 2 (Login)**, use the **"Quick Role Selector" dropdown** to instantly select any persona:

| Role | Username | Password | Operational Access |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | Full administrative control, user provisioning, database backups & reset |
| **Geospatial Analyst** | `analyst` | `analyst123` | Observation telemetry entry, credibility matrices, anomaly inspections |
| **Intelligence Officer** | `intel` | `intel123` | Strategic dossiers, publishing flash intelligence briefs, alert response |
| **Disaster Management Officer** | `disaster` | `disaster123` | Regional sector monitoring, evacuation alerts, emergency warnings |
| **Data Scientist** | `scientist` | `scientist123` | Anomaly defect rate statistics, accuracy leaderboards, AI parameter tuning |

---

## 4. Project Directory Structure
```
Java Microproject/
├── lib/
│   └── sqlite-jdbc.jar              # Zero-dependency embedded database driver
├── resources/
│   └── schema.sql                   # 16 Normalized Tables + Sample Data (SQLite & MySQL)
├── src/
│   └── spaceintel/
│       ├── Main.java                # Entry point, HTTP Web Server & Swing Launcher
│       ├── db/
│       │   └── DatabaseManager.java # JDBC connection layer & 16-table CRUD methods
│       ├── model/                   # Pure OOP Domain Models (Encapsulation)
│       │   ├── User.java
│       │   ├── DataSource.java
│       │   ├── Observation.java
│       │   ├── SourceCredibility.java
│       │   ├── ConfidenceAssessment.java
│       │   ├── Anomaly.java
│       │   ├── Evidence.java
│       │   ├── Region.java
│       │   ├── Alert.java
│       │   └── IntelligenceReport.java
│       ├── engine/                  # Core Intelligence & Detection Engines
│       │   ├── AIEngine.java        # Explainable confidence calculation & decision rules
│       │   └── AnomalyEngine.java   # Detects outliers, duplicates, conflicts, delayed updates
│       ├── util/                    # Utility Helpers
│       │   ├── SessionManager.java  # Current logged-in user & RBAC
│       │   ├── AuditService.java    # Security logging (Table 14)
│       │   ├── ReportExporter.java  # CSV, JSON, TXT report generation (Module 9)
│       │   └── UITheme.java         # Aerospace Dark Theme design tokens & styling
│       ├── ui/                      # 20 GUI Screens
│       │   ├── MainFrame.java       # Central Control Station (CardLayout + Sidebar)
│       │   ├── SplashScreenView.java       (Screen 1: Animated Splash Screen)
│       │   ├── LoginView.java              (Screen 2: Role-based Login)
│       │   ├── DashboardView.java          (Screen 3: Executive Telemetry Overview)
│       │   ├── DataSourceRegistrationView.java (Screen 4: Asset Registration)
│       │   ├── ObservationEntryView.java   (Screen 5: Coordinate & Telemetry Entry)
│       │   ├── SourceCredibilityView.java  (Screen 6: Trust Leaderboard & Recalibration)
│       │   ├── ConfidenceAssessmentView.java (Screen 7: Explainable AI Breakdown)
│       │   ├── AnomalyDashboardView.java   (Screen 8: 1-Click Anomaly Scanner)
│       │   ├── EvidenceViewerView.java     (Screen 9: Corroboration Matrix)
│       │   ├── RegionalMonitoringView.java (Screen 10: Territorial Surveillance)
│       │   ├── IntelligenceDashboardView.java (Screen 11: Tactical Dossiers)
│       │   ├── AlertsView.java             (Screen 12: Emergency Alarms)
│       │   ├── ReportsView.java            (Screen 13: Report Generation & Preview)
│       │   ├── UserManagementView.java     (Screen 14: User Accounts Admin)
│       │   ├── SettingsView.java           (Screen 15: Heuristic System Parameters)
│       │   ├── DatabaseBackupView.java     (Screen 16: Point-in-time Snapshot & Reset)
│       │   ├── AboutSystemView.java        (Screen 17: Curriculum Syllabus Mapping)
│       │   ├── AnalyticsDashboardView.java (Screen 18: Statistical Metrics)
│       │   ├── ReportHistoryView.java      (Screen 19: Export Archive & Launcher)
│       │   └── LogoutView.java             (Screen 20: Clean Sign-Off)
│       └── test/
│           └── SystemSmokeTest.java         # Automated verification suite
├── run.bat                          # Double-click Windows launcher
├── schema.sql                       # Copy of MySQL / SQLite schema
├── STUDENT_VIVA_GUIDE.md            # Complete Viva Q&A Guide for Professors
└── README.md                        # Documentation
```

---

## 5. Database Architecture (16 Normalized Tables)
The system satisfies the 16 normalized tables requirement:
1. `Users`
2. `Data_Sources`
3. `Observations`
4. `Observation_History`
5. `Source_Credibility`
6. `Confidence_Assessment`
7. `Anomalies`
8. `Evidence`
9. `Intelligence_Reports`
10. `Regions`
11. `Alerts`
12. `Dashboard_Data`
13. `Login_Log`
14. `Audit_Log`
15. `System_Settings`
16. `Report_History`

> **Note**: An embedded SQLite database (`space_intel.db`) is automatically initialized upon run. In addition, the file `resources/schema.sql` can be imported directly into MySQL / phpMyAdmin whenever external MySQL submission is requested.

---

## 6. Module V: Live Web Dashboard
While the Swing desktop application is running, open any browser and navigate to:
```
http://localhost:8080/dashboard
```
This renders a live HTML5 dashboard consuming real-time telemetry from the database, satisfying Module V (*Servlets / HTTP Request-Response / JSP Dashboard*) without needing external server installations.

---

## 7. Viva & Exam Presentation Advice
For complete sample questions and word-for-word teacher answers, refer to:
[STUDENT_VIVA_GUIDE.md](file:///c:/Users/dhrub/Desktop/Java%20Microproject/STUDENT_VIVA_GUIDE.md)
