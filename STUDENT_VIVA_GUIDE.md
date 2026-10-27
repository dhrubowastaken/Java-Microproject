# Space Intelligence & Geospatial Data Reliability Management System
## Complete Student Viva & Presentation Guide (Micro Project 09)

---

### 1. The 60-Second Elevator Pitch (Memorize This for Professors!)
> "Good morning/afternoon, Sir/Ma'am. Our project is titled **AI-Based Space Intelligence and Geospatial Data Reliability Management System**, developed in partnership with **Xovian Aerospace** for the **ABC National Geospatial Intelligence Centre**.
>
> Modern Earth observation constellations (Cartosat-3, Sentinel-2A, INSAT-3DR, UAVs, and weather stations) generate vast volumes of geospatial data. However, real-world data suffers from missing packets, sensor drift, delayed telemetry, and conflicting observations.
>
> Rather than treating AI as a mysterious black box, our Java Enterprise application implements an **Explainable AI Confidence Engine** that evaluates hardware credibility, cross-verifies multi-source sensor consistency in real time, and detects 5 classes of anomalies. The application is built with a clean **MVC architecture**, a **16-table normalized database**, and a modern **20-screen Aerospace Control Station** in Java Swing, alongside an embedded HTTP Web Dashboard satisfying Module V."

---

### 2. How to Run the Project (Zero-Setup!)
1. Double-click `run.bat` in the project folder.
2. The batch script will automatically:
   - Compile all Java sources in `src/` using Java 21 LTS into `bin/`.
   - Initialize `space_intel.db` (SQLite) with all 16 normalized tables and sample data.
   - Start the live Web Dashboard at `http://localhost:8080/dashboard`.
   - Launch Screen 1 (`SplashScreenView`) with mission loading animation.
   - Transition to Screen 2 (`LoginView`).

---

### 3. Demo Credentials (5 Supported Roles)
| Persona Role | Username | Password | Purpose & Clearance |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | Full access, user provisioning, system parameter tuning, database backup |
| **Geospatial Analyst** | `analyst` | `analyst123` | Ingests telemetry, observes credibility matrices, views corroboration |
| **Intelligence Officer** | `intel` | `intel123` | Reviews tactical dossiers, publishes flash reports, acknowledges alerts |
| **Disaster Management Officer** | `disaster` | `disaster123` | Regional surveillance, cyclone/wildfire alerts, evacuation dispatch |
| **Data Scientist** | `scientist` | `scientist123` | Explores anomaly rates, confidence distributions, provider accuracy |

> **Pro Tip for Viva**: On the Login screen, click the **"Quick Role Selector" dropdown** to instantly auto-fill credentials for any persona!

---

### 3.1 The 3-Minute Showcase Script (Follow This Step-by-Step!)
If you feel overwhelmed, do NOT worry! Just follow this exact 3-minute script when presenting to your teacher:

#### Step 1: Demonstrate Role-Based Access Control (RBAC) (45 seconds)
1. Launch the app and log in as **Geospatial Analyst** (`analyst`).
2. Point out to your professor:
   > *"Sir/Ma'am, our system enforces strict Role-Based Access Control (RBAC). Notice how my sidebar as an Analyst is clean and uncluttered. Administrative panels like User Provisioning and Database Backup are completely hidden for non-admins."*
3. Look at the bottom-left sidebar at the **Live Persona Switcher**:
   > *"Watch what happens when I switch to Administrator: The Administration section unlocks with User Accounts, System Settings, and Database Backup!"*

#### Step 2: Follow the 3-Step Guided Tour on the Dashboard (90 seconds)
Click on the **Dashboard** in the sidebar. At the top, you will see the 3 large action buttons:
1. **Click Button 1: "1. Enter New Observation"**
   - Click "Save & Calculate Confidence".
   - A dialog immediately pops up showing the **Explainable AI Confidence Engine calculation**:
     `Confidence = (0.5 * Reliability) + (0.5 * Consistency) = 86.3% -> Decision: PROCEED`.
   - Tell your professor: *"Sir, instead of a black box, our AI shows the exact mathematical corroboration formula."*
2. **Click Button 2: "2. Scan For Data Errors"**
   - Click "Run Scanner".
   - The engine scans the database and identifies **Outlier Values**, **Duplicate Packets**, and **Sensor Conflicts**!
   - Click any anomaly and click "Mark as Resolved".
3. **Click Button 3: "3. Generate & Download Report"**
   - Click "Generate Report".
   - The formatted dossier appears live in the text preview box and saves to the `reports/` folder in TXT, CSV, or JSON!

#### Step 3: Show Role-Based Access Control & Web Dashboard (45 seconds)
1. Open Google Chrome or any browser and type: `http://localhost:8080/dashboard`.
   - Show your professor the live web dashboard running alongside Swing!
2. Showcase strict Role-Based Access Control (RBAC):
   - Notice that non-admin officers only see operational tools relevant to their job.
   - Click **Logout** -> **Confirm Logout & Return to Gate**.
   - Select **Administrator (admin)** from the Quick Role Selector dropdown and click **Authenticate & Enter Terminal**.
   - Notice that the **ADMINISTRATION** menu (User Accounts, System Settings, Database Backup) is now visible!

---

### 4. Curriculum Module Mapping (Syllabus Proof)
When the evaluator asks: *"How does your project cover the syllabus modules?"*, answer:

- **Module I – Java Programming Concepts:**
  - Robust input validation (regex checks, latitude `[-90, +90]`, longitude `[-180, +180]`).
  - Formatting reports in CSV, JSON, and TXT using file streaming.
- **Module II – Object-Oriented Programming (OOP):**
  - **Encapsulation:** Private attributes with getters/setters across domain models (`User`, `Observation`, `DataSource`, `Anomaly`, `Evidence`).
  - **Inheritance & Interfaces:** Swing components extending `JPanel`/`JFrame`, JDBC `Connection`/`Statement` interfaces.
  - **Packages:** Clean separation of concerns (`spaceintel.model`, `spaceintel.db`, `spaceintel.engine`, `spaceintel.ui`, `spaceintel.util`).
- **Module III – Exception Handling, Multithreading & File I/O:**
  - Custom error handling (`SQLException`, `NumberFormatException`, `IOException`).
  - **Multithreading:** `SwingWorker` background threads for progress bars, asynchronous HTTP server dispatch.
  - Immutable audit trail logged to Table 14 (`Audit_Log`).
- **Module IV – Collections Framework & GUI Programming:**
  - `ArrayList`, `Map<String, String>`, `HashMap` used for data filtering and aggregations.
  - 20 complete GUI Screens built with modern Java Swing, `CardLayout` navigation, and aerospace dark styling (`UITheme`).
- **Module V – Database Connectivity & Web Development:**
  - JDBC database persistence across **16 normalized tables**.
  - Dual database strategy: zero-setup SQLite embedded database file + `resources/schema.sql` (100% compatible with MySQL).
  - Built-in HTTP web dashboard endpoint at `http://localhost:8080/dashboard`.

---

### 5. The 16 Normalized Database Tables
1. `Users` - Credentials, full names, roles, departments.
2. `Data_Sources` - Satellites, drones, weather stations, GPS, GIS assets.
3. `Observations` - Live telemetry (lat, lon, observation type, measured value, units).
4. `Observation_History` - Historical passes for trend analysis.
5. `Source_Credibility` - Dynamic trust score (0-100%), historical accuracy, validation status.
6. `Confidence_Assessment` - AI confidence score, reliability weight, consistency weight, decision.
7. `Anomalies` - Catalog of detected defects (missing data, duplicates, outliers, etc.).
8. `Evidence` - Corroborating vs contradicting sensor facts backing decisions.
9. `Intelligence_Reports` - Synthesized dossiers with threat assessments.
10. `Regions` - Strategic surveillance sectors with bounding coordinates.
11. `Alerts` - Critical operational warnings and acknowledgements.
12. `Dashboard_Data` - Centralized KPI metrics.
13. `Login_Log` - Security access logs for every login attempt.
14. `Audit_Log` - Immutable record of analyst modifications.
15. `System_Settings` - Configurable thresholds and engine sensitivities.
16. `Report_History` - Archive of generated report exports.

---

### 6. The 20 GUI Screens & What to Show
1. **Screen 1: Splash Screen (`SplashScreenView`)** - Simulated loading sequence with mission status.
2. **Screen 2: Login (`LoginView`)** - Role preset selection and database credential check.
3. **Screen 3: Dashboard (`DashboardView`)** - KPI cards, telemetry stream, quick actions.
4. **Screen 4: Data Sources (`DataSourceRegistrationView`)** - Register satellites and sensors.
5. **Screen 5: Observation Entry (`ObservationEntryView`)** - Record coordinates and trigger instant AI scoring.
6. **Screen 6: Source Credibility (`SourceCredibilityView`)** - Leaderboard and score recalibration.
7. **Screen 7: AI Confidence Viewer (`ConfidenceAssessmentView`)** - Explainable AI breakdown with progress bars.
8. **Screen 8: Anomaly Scanner (`AnomalyDashboardView`)** - 1-Click live scan identifying 5 defect types.
9. **Screen 9: Evidence Viewer (`EvidenceViewerView`)** - Supporting vs contradicting facts matrix.
10. **Screen 10: Regional Monitoring (`RegionalMonitoringView`)** - Filter observations by geographic sector.
11. **Screen 11: Tactical Dossiers (`IntelligenceDashboardView`)** - Read and publish flash intelligence briefs.
12. **Screen 12: Alerts (`AlertsView`)** - Filter emergency warnings and acknowledge them.
13. **Screen 13: Reports (`ReportsView`)** - Export CSV, JSON, TXT reports with live preview.
14. **Screen 14: User Accounts (`UserManagementView`)** - RBAC admin panel for managing user roles.
15. **Screen 15: Settings (`SettingsView`)** - Configure AI thresholds and sensor ping intervals.
16. **Screen 16: Database Backup (`DatabaseBackupView`)** - 1-Click point-in-time snapshot & demo reset.
17. **Screen 17: About System (`AboutSystemView`)** - Curriculum syllabus mapping and architecture.
18. **Screen 18: Analytics (`AnalyticsDashboardView`)** - Observation category volumes and trust rankings.
19. **Screen 19: Report History (`ReportHistoryView`)** - Audit trail of exported documents with 1-click open.
20. **Screen 20: Logout (`LogoutView`)** - Clean session termination and security sign-off.

---

### 7. The AI Engine Logic (How to Explain It)
- **Q: How does your AI simulate confidence?**
  - **Answer**: "We use a transparent, multi-factor rule-based corroboration formula:
    $$\text{Confidence} = (0.5 \times \text{Reliability}) + (0.5 \times \text{Consistency})$$
    1. **Reliability** comes from historical accuracy ratings in Table 5 (`Source_Credibility`).
    2. **Consistency** compares the current reading against other independent sensors in the same region (e.g. comparing UAV thermal scans with satellite infrared).
    3. If Confidence $\ge 85\%$, the decision is **PROCEED**.
    4. If $60\% \le \text{Confidence} < 85\%$, the decision is **CORROBORATE** (request a secondary drone pass).
    5. If Confidence $< 60\%$, the decision is **REJECT** (telemetry drift or hardware glitch)."

---

### 8. The 5 Anomaly Types (How to Explain It)
1. **Outlier Values**: Values violating physical laws (e.g. $+22.8^\circ\text{C}$ glaciological sensor reading on a high-altitude Himalayan snowpack).
2. **Duplicate Observations**: The same satellite packet ingested multiple times within a 60-second window.
3. **Conflicting Measurements**: Two sensors in the same sector reporting $>40\%$ divergence on the same metric.
4. **Delayed Updates**: Inactive or degraded data providers that have dropped out of telemetry contact.
5. **Missing Data**: Corrupted telemetry records lacking latitude/longitude coordinates (Null Island coordinates).

---

### 9. Top 10 Teacher Viva Questions & Direct Answers

#### Q1: Why did you use SQLite instead of MySQL?
> **Answer**: "We implemented a **Dual-Mode Database Architecture**. For ease of demonstration and portability, the application uses an embedded SQLite database (`space_intel.db`), meaning it runs immediately on any computer with zero configuration. At the same time, we created a standard `resources/schema.sql` file containing all 16 normalized tables and seed data that can be directly executed inside MySQL or phpMyAdmin."

#### Q2: What design patterns did you use?
> **Answer**:
> 1. **MVC (Model-View-Controller)**: Models hold data (`model/`), Views render UI (`ui/`), and Engine/DB act as controllers (`engine/`, `db/`).
> 2. **Singleton Pattern**: In `DatabaseManager.java`, ensuring only one central database connection manager exists throughout the application lifecycle.
> 3. **CardLayout / Screen Switcher**: In `MainFrame.java`, switching views efficiently without destroying or re-allocating window resources.

#### Q3: How do you handle multithreading?
> **Answer**: "We utilize `SwingWorker` background worker threads. In `SplashScreenView.java`, the progress bar updates on a background thread while database tables are initialized, preventing the Swing Event Dispatch Thread (EDT) from freezing. We also run an embedded HTTP server thread for the Module V web dashboard."

#### Q4: How is Role-Based Access Control (RBAC) implemented?
> **Answer**: "The `SessionManager` class stores the authenticated `User` object upon login. Each screen checks `SessionManager.getRole()` or `SessionManager.isAdmin()` before allowing administrative tasks like user account provisioning or database resets."

#### Q5: How do you prevent SQL Injection?
> **Answer**: "We consistently use JDBC `PreparedStatement` with parameterized placeholders (`?`) rather than concatenating raw user strings into SQL queries."

#### Q6: How does the application fulfill Module V (Web Development & Servlets)?
> **Answer**: "The application features an embedded HTTP server running on port 8080. Evaluators can navigate to `http://localhost:8080/dashboard` in Google Chrome or any browser to view live web telemetry, fulfilling the HTTP request/response and web dashboard requirements alongside the Swing desktop application."

#### Q7: Where are exported reports saved?
> **Answer**: "Exported dossiers are written to the `reports/` directory as `.csv`, `.json`, or `.txt` files. Every export is recorded in Table 16 (`Report_History`) and can be opened directly from Screen 19."

#### Q8: How is the database initialized on a new computer?
> **Answer**: "When the program starts, `DatabaseManager.initDatabase()` checks if `space_intel.db` exists. If not, it parses `resources/schema.sql` and automatically executes the DDL and DML statements to create all 16 tables and seed realistic Xovian Aerospace telemetry."

#### Q9: What happens if an anomaly is detected?
> **Answer**: "The anomaly is logged to Table 7 (`Anomalies`) with its severity (CRITICAL, HIGH, MEDIUM, LOW) and expected range. Analysts can view it on Screen 8 and click 'Mark Selected as Resolved' once addressed."

#### Q10: How do you ensure code maintainability?
> **Answer**: "Every class has beginner-friendly, step-by-step explanatory documentation and follows uniform naming conventions. The UI uses a shared design system in `UITheme.java` so styling, fonts, and colors remain consistent."
