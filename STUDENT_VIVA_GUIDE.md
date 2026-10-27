# Space Intelligence & Geospatial Data Reliability Management System
## Complete Student Viva, Defense & Code Walkthrough Guide (Micro Project 09)

---

## 1. Project Identity & 60-Second Elevator Pitch
> **Memorize this opening speech for your viva examiner:**
>
> *"Good morning/afternoon, Sir/Ma'am. Our project is titled **AI-Based Space Intelligence and Geospatial Data Reliability Management System**, developed in partnership with **Xovian Aerospace** for the **ABC National Geospatial Intelligence Centre**.*
>
> *Modern Earth observation missions—such as Cartosat-3, Sentinel-2A, INSAT-3DR, UAV reconnaissance flights, and ground weather stations—stream massive volumes of telemetry. However, in operational defense and disaster management, raw sensor data often contains missing coordinates, sensor drift, delayed downlinks, and conflicting readings.*
>
> *Rather than treating Artificial Intelligence as an untrustworthy 'black box', our Java Enterprise application implements a **Rule-Based Explainable AI Confidence Engine** and a **5-Class Automated Anomaly Scanner**. It evaluates hardware reliability, cross-verifies multi-source sensor consistency, and provides mathematical justification for operational decisions.*
>
> *Architecturally, the project is structured on a strict **Model-View-Controller (MVC)** design pattern, backed by a **16-table normalized relational database** (SQLite with zero-setup portability + MySQL-compatible schema), a high-contrast **20-screen Aerospace Control Station** built with Java Swing, and an **embedded HTTP Web Dashboard** fulfilling Module V."*

---

## 2. Quick-Start & Operational Credentials
- **Launch Script**: Double-click `run.bat` (or run `./run.ps1` in PowerShell).
- **Automated Startup Sequence**:
  1. Compiles all 40 Java source files into `bin/`.
  2. Auto-detects `space_intel.db`. If missing, parses `resources/schema.sql` and builds all 16 tables with seed data.
  3. Launches the embedded HTTP server on background port 8080 (`http://localhost:8080/dashboard`).
  4. Activates FlatDarkLaf modern dark theme (`UITheme.java`).
  5. Displays Screen 1 (`SplashScreenView.java`) with mission loading animation, then transitions to Screen 2 (`LoginView.java`).

### Demo Personas & Credentials
| Persona Role | Username | Password | Operational Access & Clearance |
| :--- | :--- | :--- | :--- |
| **Administrator** | `admin` | `admin123` | Unrestricted clearance; User management, System settings, Database backup |
| **Geospatial Analyst** | `analyst` | `analyst123` | Sensor registration, telemetry ingest, credibility matrices, anomaly scanning |
| **Intelligence Officer** | `intel` | `intel123` | Regional monitoring, tactical dossiers, flash briefs, emergency alerts |
| **Disaster Management Officer** | `disaster` | `disaster123` | Surveillance of flood/cyclone/wildfire sectors, alert dispatch, evacuation notes |
| **Data Scientist** | `scientist` | `scientist123` | Anomaly telemetry distributions, confidence score analytics, accuracy trends |

> **Viva Shortcut**: On Screen 2 (`LoginView`), click the **"Quick Role Selector"** dropdown. It immediately auto-fills valid credentials for any role!

---

## 3. The 3-Minute Live Demonstration Script
Follow this exact sequence during your lab evaluation:

### Step 1: Demonstrate Role-Based Access Control (RBAC) (45 seconds)
1. In the Quick Role Selector, select **Geospatial Analyst (`analyst`)** and click **Authenticate & Enter Terminal**.
2. Point out to your professor:
   > *"Sir/Ma'am, in `MainFrame.java` (lines 198–286), the navigation sidebar is dynamically built according to user role permissions. Notice how as an Analyst, sensitive administrative menus like User Accounts, System Settings, and Database Backup are completely hidden to prevent unauthorized access."*
3. Click **Sign Out** at the bottom of the sidebar -> Click **Confirm Sign Out**.
4. Now select **Administrator (`admin`)** and log in.
5. Point out to the evaluator:
   > *"Now that I am authenticated as Administrator, the ADMINISTRATION group appears in the sidebar with User Accounts, System Settings, and Database Backup."*

### Step 2: Ingest Telemetry & Show Explainable AI Corroboration (60 seconds)
1. On the **Dashboard**, click the big action button: **"1. Enter New Observation"** (Screen 5: `ObservationEntryView.java`).
2. Keep the default sample values (or type: Region `REG-01`, Type `Thermal Anomaly`, Value `48.5`, Source `UAV-HERON-01`).
3. Click **"Save & Calculate Confidence"**.
4. A dialog immediately displays the **AI Calculation Breakdown**:
   - Hardware Reliability: `91.0%` (from Table 5: `Source_Credibility`)
   - Consistency with other sensors: `88.4%` (variance check against satellite readings)
   - Weighted Formula: `(0.50 * 91.0) + (0.50 * 88.4) = 89.7%`
   - Recommendation: `PROCEED - Multi-source agreement confirmed.`
5. Explain:
   > *"In `AIEngine.java` (lines 41–114), instead of an opaque neural network, our system provides an explainable mathematical corroboration score that commanders can inspect and audit."*

### Step 3: Run the 5-Class Anomaly Scanner (45 seconds)
1. In the sidebar, click **Anomaly Scanner** (Screen 8: `AnomalyDashboardView.java`).
2. Click the button **"Run Anomaly Scanner"**.
3. In `AnomalyEngine.java` (lines 39–159), the engine performs 5 algorithmic scans:
   - Identifies outlier glaciological temperatures (>15°C on a glacier).
   - Identifies conflicting measurements between sensors in the same sector.
   - Detects duplicate packets and missing coordinates (Null Island coordinates: `0.0, 0.0`).
4. Select any discovered anomaly in the table and click **"Mark Selected as Resolved"**.

### Step 4: Generate a Formatted Report & Show the Web Dashboard (30 seconds)
1. In the sidebar, click **Generate Reports** (Screen 13: `ReportsView.java`).
2. Select **Intelligence Summary**, choose format **TXT**, and click **"Generate & Save Report"**.
3. The formatted report appears in the live preview pane and is written to `reports/` via `ReportExporter.java`.
4. Open any browser (Chrome, Edge, Firefox) and navigate to: `http://localhost:8080/dashboard`.
5. Point out to your professor:
   > *"In `Main.java` (lines 64–145), an embedded HTTP server runs on port 8080, serving live database telemetry to fulfill Module V Web Development requirements without requiring external Tomcat configuration."*

---

## 4. Complete Codebase Blueprint: File-by-File & Code Chunk Walkthrough
Our project consists of **40 Java source files** organized into 7 distinct packages, adhering strictly to enterprise separation of concerns.

```
src/
├── spaceintel/
│   └── Main.java                          [Entry point & Module V HTTP web server]
├── spaceintel/db/
│   └── DatabaseManager.java               [JDBC connection, singleton, 16-table CRUD & SQL parser]
├── spaceintel/engine/
│   ├── AIEngine.java                      [Rule-based explainable confidence evaluation]
│   └── AnomalyEngine.java                 [5-class geospatial defect detection scanner]
├── spaceintel/model/                      [10 Encapsulated Domain Models / POJOs]
│   ├── Alert.java                         [Emergency operational alert entity]
│   ├── Anomaly.java                       [Detected telemetry anomaly record]
│   ├── ConfidenceAssessment.java          [AI confidence score & justification entity]
│   ├── DataSource.java                    [Satellite, UAV, weather station platform entity]
│   ├── Evidence.java                      [Supporting vs contradicting corroboration facts]
│   ├── IntelligenceReport.java            [Synthesized intelligence dossier entity]
│   ├── Observation.java                   [Raw sensor measurement telemetry entity]
│   ├── Region.java                        [Geographic surveillance sector entity]
│   ├── SourceCredibility.java             [Provider hardware trust rating entity]
│   └── User.java                          [RBAC user profile and credentials entity]
├── spaceintel/util/                       [4 Enterprise Utilities]
│   ├── AuditService.java                  [Table 14 immutable action logger]
│   ├── ReportExporter.java                [File I/O streaming to CSV, JSON, and TXT]
│   ├── SessionManager.java                [Thread-safe authenticated session store]
│   └── UITheme.java                       [FlatDarkLaf palette, dark styling & fonts]
├── spaceintel/ui/                         [20 Aerospace Control Station Swing Views]
│   ├── AboutSystemView.java               [Screen 17: Curriculum mapping & architecture]
│   ├── AlertsView.java                    [Screen 12: Emergency alerts & acknowledgment]
│   ├── AnalyticsDashboardView.java        [Screen 18: Statistical intelligence metrics]
│   ├── AnomalyDashboardView.java          [Screen 8: 1-click anomaly scanner & resolver]
│   ├── ConfidenceAssessmentView.java      [Screen 7: Visual AI progress bar breakdown]
│   ├── DashboardView.java                 [Screen 3: Primary operational command hub]
│   ├── DatabaseBackupView.java            [Screen 16: Database snapshot & demo reset]
│   ├── DataSourceRegistrationView.java    [Screen 4: Sensor & satellite hardware registry]
│   ├── EvidenceViewerView.java            [Screen 9: Corroboration facts matrix]
│   ├── IntelligenceDashboardView.java     [Screen 11: Tactical dossiers & brief publisher]
│   ├── LoginView.java                     [Screen 2: Role-based login gate with presets]
│   ├── LogoutView.java                    [Screen 20: Clean session sign-off view]
│   ├── MainFrame.java                     [Parent JFrame, CardLayout & RBAC menu builder]
│   ├── ObservationEntryView.java          [Screen 5: Telemetry ingest & instant AI scoring]
│   ├── RegionalMonitoringView.java        [Screen 10: Geographic sector observation filter]
│   ├── ReportHistoryView.java             [Screen 19: Export archive with 1-click file open]
│   ├── ReportsView.java                   [Screen 13: Configurable report builder & preview]
│   ├── SettingsView.java                  [Screen 15: Heuristic parameter editor]
│   ├── SourceCredibilityView.java         [Screen 6: Trust leaderboard & recalibration]
│   ├── SplashScreenView.java              [Screen 1: Animated mission loader via SwingWorker]
│   └── UserManagementView.java            [Screen 14: Admin user account provisioning]
└── spaceintel/test/
    └── SystemSmokeTest.java               [6-phase automated verification test suite]
```

---

### 4.1 Entry Point & Web Server (`spaceintel`)

#### [Main.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/Main.java)
- **Role**: Application bootstrap, Theme initializer, and Embedded HTTP Web Server dispatcher.
- **Key Methods**:
  - `main(String[] args)` (lines 37–58): Calls `startEmbeddedWebServer()`, invokes `UITheme.setupGlobalTheme()`, and dispatches `SplashScreenView` onto the Swing Event Dispatch Thread (EDT) via `SwingUtilities.invokeLater()`.
  - `startEmbeddedWebServer()` (lines 64–74): Instantiates `com.sun.net.httpserver.HttpServer` on port 8080, registers `/dashboard` context, and starts listening.
  - `WebDashboardHandler.handle(HttpExchange exchange)` (lines 79–144): Implements `HttpHandler`. Fetches metrics and observations from `DatabaseManager.getInstance()`, dynamically constructs responsive HTML5/CSS, sets `Content-Type: text/html`, and streams bytes to the browser client with HTTP 200 OK.
- **Key Code Chunk (lines 66–70)**:
```java
HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
server.createContext("/dashboard", new WebDashboardHandler());
server.setExecutor(null); // default executor
server.start();
```
- **Viva Defense**: *"Sir, `Main.java` demonstrates Java's built-in networking capabilities. Rather than burdening the user with an external Apache Tomcat server, we leverage Java's native `HttpServer` to serve real-time HTML5 dashboards on port 8080, fulfilling Module V."*

---

### 4.2 Database & Persistence Layer (`spaceintel.db`)

#### [DatabaseManager.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/db/DatabaseManager.java)
- **Role**: Centralized Data Access Object (DAO) managing JDBC connectivity, table creation, and CRUD operations across all 16 normalized tables.
- **Design Pattern**: **Singleton Pattern** via private constructor and `public static synchronized DatabaseManager getInstance()` (lines 44–53).
- **Key Methods**:
  - `getConnection()` (lines 58–65): Loads `org.sqlite.JDBC` driver and returns `DriverManager.getConnection("jdbc:sqlite:space_intel.db")`.
  - `initDatabase()` (lines 70–80): Checks if `space_intel.db` exists. If not, calls `executeSchemaScript()`.
  - `parseSqlStatements(String script)` (lines 134–178): Custom state-machine parser. Reads `schema.sql` character by character, tracking `inQuote` and `inLineComment` flags so that semicolons inside text literals are never misidentified as statement terminators.
  - CRUD Methods: `authenticate(u, p)`, `getAllObservations()`, `addObservation(obs)`, `getAllDataSources()`, `addDataSource(ds)`, `getAllSourceCredibility()`, `updateSourceCredibility(id, score)`, `getAllAnomalies()`, `addAnomaly(a)`, `resolveAnomaly(id)`, `getAllUsers()`, `addUser(u)`, `getDashboardMetrics()`, `logAudit(user, action, target, details)`.
- **Key Code Chunk - State Machine SQL Parser (lines 140–167)**:
```java
for (int i = 0; i < script.length(); i++) {
    char c = script.charAt(i);
    if (!inQuote && c == '-' && i + 1 < script.length() && script.charAt(i + 1) == '-') {
        inLineComment = true; i++; continue;
    }
    if (inLineComment) {
        if (c == '\n' || c == '\r') inLineComment = false;
        continue;
    }
    if (c == '\'') { inQuote = !inQuote; current.append(c); continue; }
    if (c == ';' && !inQuote) {
        String stmt = current.toString().trim();
        if (!stmt.isEmpty()) list.add(stmt);
        current.setLength(0);
        continue;
    }
    current.append(c);
}
```
- **Viva Defense**: *"Sir, standard `String.split(\";\")` corrupts SQL statements when column values contain semicolons. In `DatabaseManager.java`, we implemented an in-line lexical parser that preserves string literals and strips SQL comments cleanly."*

#### [schema.sql](file:///c:/Users/dhrub/Desktop/Java%20Microproject/resources/schema.sql)
- **Role**: 16-table normalized DDL and initial seed telemetry dataset.
- **Tables Created**:
  1. `Users` (Authentication and roles)
  2. `Data_Sources` (Sensor hardware catalog)
  3. `Observations` (Geospatial measurements)
  4. `Observation_History` (Historical telemetry log)
  5. `Source_Credibility` (Dynamic provider trust ratings)
  6. `Confidence_Assessment` (AI confidence ratings and decisions)
  7. `Anomalies` (Detected sensor discrepancies)
  8. `Evidence` (Corroborating vs contradicting facts)
  9. `Intelligence_Reports` (Tactical synthesized dossiers)
  10. `Regions` (Strategic surveillance sectors)
  11. `Alerts` (Operational emergency warnings)
  12. `Dashboard_Data` (Pre-aggregated KPI metrics)
  13. `Login_Log` (Audit log of authentication attempts)
  14. `Audit_Log` (Immutable action audit trail)
  15. `System_Settings` (Configurable algorithm thresholds)
  16. `Report_History` (Archive of generated exports)

---

### 4.3 Intelligence & Detection Algorithms (`spaceintel.engine`)

#### [AIEngine.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/engine/AIEngine.java)
- **Role**: Rule-Based Explainable AI Corroboration Engine.
- **Mathematical Formula**:
  $$\text{Confidence Score} = (0.50 \times \text{Reliability}) + (0.50 \times \text{Consistency})$$
- **Key Methods**:
  - `evaluateObservation(Observation obs)` (lines 41–114):
    1. Looks up `reliability` for `obs.getSourceId()` from `Source_Credibility` table.
    2. Compares measurement against other observations of the same type in the same region:
       $$\text{diff} = |\text{obs.val} - \text{other.val}|, \quad \text{percentDiff} = \left(\frac{\text{diff}}{\max(1.0, |\text{obs.val}|)}\right) \times 100$$
       $$\text{consistency} = \max(10.0, \min(100.0, 100.0 - \text{percentDiff}))$$
    3. Blends scores into weighted confidence.
    4. Issues explainable recommendations:
       - $\ge 85.0\% \implies \text{PROCEED}$
       - $60.0\% \le \text{Score} < 85.0\% \implies \text{CORROBORATE}$
       - $< 60.0\% \implies \text{REJECT}$
  - `assessAllRawObservations()` (lines 119–138): Batch-evaluates all raw telemetry records and generates corresponding `Evidence` records in Table 8.
- **Key Code Chunk (lines 68–80)**:
```java
double diff = Math.abs(obs.getMeasuredValue() - other.getMeasuredValue());
double base = Math.max(1.0, Math.abs(obs.getMeasuredValue()));
double percentDiff = (diff / base) * 100.0;
consistency = Math.max(10.0, Math.min(100.0, 100.0 - percentDiff));

double confidence = (0.50 * reliability) + (0.50 * consistency);
confidence = Math.round(confidence * 10.0) / 10.0;
```
- **Viva Defense**: *"Sir, our AI engine eliminates black-box ambiguity. In `AIEngine.java`, every confidence rating is derived transparently from hardware reliability and multi-source regional sensor consistency."*

#### [AnomalyEngine.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/engine/AnomalyEngine.java)
- **Role**: Automated 5-class telemetry defect detection scanner.
- **Key Methods**:
  - `runFullAnomalyScan()` (lines 39–159): Scans observations and sources, cataloging anomalies into 5 types:
    1. **Outlier Values** (lines 47–83): Checks physical limits (e.g., glaciological temp $>15^\circ\text{C}$, thermal outside $[-10, 150]^\circ\text{C}$, barometric pressure outside $[800, 1100]\text{ hPa}$, NDVI outside $[-1.0, 1.0]$).
    2. **Duplicate Observations** (lines 88–103): Nested loop detecting records with matching `sourceId`, identical value, and geographic distance $< 0.0001^\circ$.
    3. **Conflicting Measurements** (lines 108–130): Compares different sources in the same region and metric reporting $>40\%$ divergence.
    4. **Delayed Updates** (lines 135–143): Detects platforms with status `DEGRADED` or `OFFLINE`.
    5. **Missing Data** (lines 148–156): Identifies "Null Island" coordinates where `latitude == 0.0` and `longitude == 0.0`.
  - `scanAndPersist()` (lines 164–173): Executes scan and persists discovered issues to Table 7 (`Anomalies`).
- **Key Code Chunk - Conflict Detection (lines 116–127)**:
```java
double diff = Math.abs(a.getMeasuredValue() - b.getMeasuredValue());
double maxVal = Math.max(1.0, Math.max(Math.abs(a.getMeasuredValue()), Math.abs(b.getMeasuredValue())));
if ((diff / maxVal) > 0.40) { // More than 40% variance
    Anomaly anom = new Anomaly(0, b.getObservationId(), b.getSourceId(),
            "Conflicting Measurements", "HIGH", b.getMeasuredValue(),
            "Variance <= 20%",
            "Telemetry conflict: " + a.getSourceId() + " reports " + a.getMeasuredValue() +
            " but " + b.getSourceId() + " reports " + b.getMeasuredValue(),
            null, "OPEN");
    discovered.add(anom);
}
```

---

### 4.4 Utility & Infrastructure Helpers (`spaceintel.util`)

#### [SessionManager.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/util/SessionManager.java)
- **Role**: Thread-safe in-memory session management for Role-Based Access Control (RBAC).
- **Key Methods**:
  - `setCurrentUser(User user)` (lines 25–27): Stores authenticated `User` object upon login.
  - `isAdmin()` (lines 53–55): Returns `true` only if current role is `"Administrator"`.
  - `getRole()`, `getUsername()`, `getFullName()`, `logout()`.
- **Viva Defense**: *"Sir, `SessionManager.java` uses synchronized static methods to ensure thread-safe session tracking across all 20 views without requiring global state leakage."*

#### [AuditService.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/util/AuditService.java)
- **Role**: Compliance and defense action audit logger.
- **Key Methods**:
  - `log(String action, String entityAffected, String details)` (lines 17–21): Retrieves active username from `SessionManager` and writes an immutable audit record to Table 14 (`Audit_Log`).
- **Key Code Chunk (lines 17–20)**:
```java
public static void log(String action, String entityAffected, String details) {
    String username = SessionManager.getUsername();
    DatabaseManager.getInstance().logAudit(username, action, entityAffected, details);
    System.out.println("[AUDIT LOG] " + username + " -> " + action + " on " + entityAffected);
}
```

#### [ReportExporter.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/util/ReportExporter.java)
- **Role**: Multi-format File I/O report generator streaming to CSV, JSON, and TXT.
- **Key Methods**:
  - `generateReport(String reportType, String format)` (lines 37–59): Generates timestamped file in `reports/` directory using `PrintWriter` and `FileWriter`. Records export in Table 16 (`Report_History`) and calls `AuditService.log()`.
  - `writeTxtReport(PrintWriter w, ...)` (lines 61–105): Formats human-readable intelligence dossier with ASCII alignment.
  - `writeCsvReport(PrintWriter w, ...)` (lines 107–134): Formats comma-separated spreadsheet data.
  - `writeJsonReport(PrintWriter w, ...)` (lines 136–166): Formats structured JSON array of data objects.
- **Viva Defense**: *"Sir, `ReportExporter.java` fulfills Module III File I/O requirements by implementing Java `try-with-resources` to safely stream structured telemetry to CSV, JSON, and TXT files without resource leaks."*

#### [UITheme.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/util/UITheme.java)
- **Role**: Centralized Design System providing modern aerospace styling, colors, and fonts.
- **Key Constants & Methods**:
  - Palette: `BG_DARK` (`#0B0F19`), `PANEL_BG` (`#131B2E`), `SIDEBAR_BG` (`#0F172A`), `CYAN` (`#38BDF8`), `TEXT_PRIMARY` (`#FFFFFF`).
  - `setupGlobalTheme()` (lines 56–105): Reflectively loads FlatDarkLaf (`com.formdev.flatlaf.FlatDarkLaf`) and applies customized dark properties to all Swing widgets (`Table.background`, `Button.background`, etc.).
  - Factory helpers: `createPrimaryButton()`, `createSecondaryButton()`, `createDarkTable()`, `styleHeaderLabel()`.

---

### 4.5 Domain Models / POJOs (`spaceintel.model`)
All 10 domain models encapsulate business data with private fields, default constructors, parameterized constructors, and complete getters/setters (satisfying Module II OOP Encapsulation).

1. **[User.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/User.java)**: Models Table 1 (`userId`, `username`, `password`, `fullName`, `role`, `department`, `email`).
2. **[DataSource.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/DataSource.java)**: Models Table 2 (`sourceId`, `sourceName`, `sourceType`, `agency`, `coverageArea`, `updateFrequency`, `status`).
3. **[Observation.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/Observation.java)**: Models Table 3 (`observationId`, `timestamp`, `latitude`, `longitude`, `observationType`, `measuredValue`, `unit`, `sourceId`, `regionId`, `status`).
4. **[SourceCredibility.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/SourceCredibility.java)**: Models Table 5 (`sourceId`, `reliabilityScore`, `historicalAccuracy`, `updateFrequency`, `validationStatus`, `confidenceLevel`).
5. **[ConfidenceAssessment.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/ConfidenceAssessment.java)**: Models Table 6 (`assessmentId`, `observationId`, `confidenceScore`, `reliabilityScore`, `consistencyScore`, `evidenceSummary`, `recommendedDecision`, `assessedAt`).
6. **[Anomaly.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/Anomaly.java)**: Models Table 7 (`anomalyId`, `observationId`, `sourceId`, `anomalyType`, `severity`, `detectedValue`, `expectedRange`, `description`, `detectedAt`, `resolutionStatus`).
7. **[Evidence.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/Evidence.java)**: Models Table 8 (`evidenceId`, `assessmentId`, `supportingObservation`, `contradictingObservation`, `confidenceExplanation`, `recommendation`).
8. **[Region.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/Region.java)**: Models Table 10 (`regionId`, `regionName`, `centerLat`, `centerLon`, `terrainType`, `threatLevel`, `description`).
9. **[Alert.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/Alert.java)**: Models Table 11 (`alertId`, `severity`, `title`, `message`, `regionId`, `status`, `createdAt`, `acknowledgedBy`).
10. **[IntelligenceReport.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/model/IntelligenceReport.java)**: Models Table 9 (`reportId`, `title`, `author`, `threatLevel`, `confidenceRating`, `summaryText`, `recommendations`, `publishedAt`).

---

### 4.6 The 20 GUI Screens in Java Swing (`spaceintel.ui`)

#### [MainFrame.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/MainFrame.java)
- **Role**: Central JFrame container orchestrating screen transitions and Role-Based Access Control.
- **Architecture**: Employs `java.awt.CardLayout` to host all operational views without window flickering or memory reallocation.
- **Key Methods**:
  - `initUI()` (lines 65–130): Initializes the 18 docked views, registers them in `cardContainer` using unique string keys, and creates the left sidebar.
  - `rebuildSidebarMenu()` (lines 198–286): Inspects `SessionManager.getRole()`. Dynamically appends navigation buttons organized into 5 groups (`OPERATIONS`, `AI & RELIABILITY`, `REPORTS`, `ADMINISTRATION`, `SYSTEM`).
  - `showScreen(String name)` (lines 345–365): Guards admin screens; if a non-admin attempts access, redirects and displays a security alert dialog. Switches visible card via `cardLayout.show(cardContainer, name)`.

---

#### Detailed Breakdown of all 20 Views
| # | Screen Class | Simple English Definition | Key Swing Widgets | Underlying Logic / Code Action |
| :-: | :--- | :--- | :--- | :--- |
| **1** | **[SplashScreenView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/SplashScreenView.java)** | Animated Mission Bootstrapper | `JProgressBar`, `JLabel`, `SwingWorker` | Runs `SwingWorker` background thread to simulate subsystem checks (0–100%), initializes `DatabaseManager`, then invokes `onComplete.run()` on EDT to open `LoginView`. |
| **2** | **[LoginView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/LoginView.java)** | Authentication & Role Gate | `JTextField`, `JPasswordField`, `JComboBox`, `JButton` | Authenticates against Table 1 via `db.authenticate(u, p)`. Sets `SessionManager.setCurrentUser()`, records timestamp in Table 13 (`Login_Log`), opens `MainFrame`. |
| **3** | **[DashboardView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/DashboardView.java)** | Primary Command Overview | Metric Cards (`JPanel`), `JTable`, Quick Action Buttons | Queries `db.getDashboardMetrics()` to render 3 KPI metric tiles; renders live observations in a styled `JTable`; includes 3 guided tour buttons. |
| **4** | **[DataSourceRegistrationView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/DataSourceRegistrationView.java)** | Satellite & Sensor Registry | `JTextField`, `JComboBox`, `JTable`, Form Buttons | Validates hardware source ID, persists new platform to Table 2 (`Data_Sources`), and automatically registers baseline trust in Table 5 (`Source_Credibility`). |
| **5** | **[ObservationEntryView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/ObservationEntryView.java)** | Telemetry Ingest & AI Corroboration | Coordinate Form, Number Parsers, Modal Dialog | Validates latitude `[-90, +90]`, longitude `[-180, +180]`, calls `AIEngine.evaluateObservation(obs)`, saves observation, and pops up transparent AI corroboration dialog. |
| **6** | **[SourceCredibilityView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/SourceCredibilityView.java)** | Hardware Trust Leaderboard | `JTable`, `JSpinner`, Recalibrate Button | Reads Table 5 (`Source_Credibility`). Allows analysts to recalibrate reliability scores (0–100%) and updates database via `db.updateSourceCredibility()`. |
| **7** | **[ConfidenceAssessmentView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/ConfidenceAssessmentView.java)** | Explainable AI Breakdown | `JTable`, Custom `TableCellRenderer`, `JProgressBar` | Displays AI confidence evaluations with embedded horizontal progress bars color-coded by decision (`PROCEED` = Green, `CORROBORATE` = Amber, `REJECT` = Red). |
| **8** | **[AnomalyDashboardView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/AnomalyDashboardView.java)** | 5-Class Anomaly Scanner | `JTable`, Action Buttons, Severity Badges | 1-Click button invokes `AnomalyEngine.runFullAnomalyScan()`, logs discovered defects into Table 7 (`Anomalies`), and provides a button to mark issues as `RESOLVED`. |
| **9** | **[EvidenceViewerView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/EvidenceViewerView.java)** | Corroboration Evidence Matrix | Split-Pane `JTable`, Evidence Detail Card | Queries Table 8 (`Evidence`). Displays supporting telemetry facts vs contradicting sensor observations for any selected assessment. |
| **10** | **[RegionalMonitoringView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/RegionalMonitoringView.java)** | Geographic Sector Surveillance | `JComboBox` Region Filter, Sensor Table | Displays geographic sectors from Table 10 (`Regions`) and dynamically filters sensor observations by selected geographic bounding sector. |
| **11** | **[IntelligenceDashboardView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/IntelligenceDashboardView.java)** | Tactical Intelligence Dossiers | `JTextArea`, `JTable`, Publish Dialog | Reads Table 9 (`Intelligence_Reports`). Allows intelligence officers to review synthesized briefs and draft new classified tactical flash bulletins. |
| **12** | **[AlertsView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/AlertsView.java)** | Emergency Warning Center | Alert Feed Panel, Acknowledge Button | Renders operational alerts from Table 11 (`Alerts`). Officers click "Acknowledge Alert" which timestamps the user's name in `acknowledged_by`. |
| **13** | **[ReportsView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/ReportsView.java)** | Configurable Report Exporter | Report Selector, Format Radios, `JTextArea` | Invokes `ReportExporter.generateReport()`, outputs file to `reports/` in CSV, JSON, or TXT, and provides an instant live text preview box. |
| **14** | **[UserManagementView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/UserManagementView.java)** | Administrator User Provisioning | `JTable`, User Creation Form | Strictly restricted to Administrator. Displays all registered accounts from Table 1 (`Users`) and provisions new officer accounts with defined RBAC roles. |
| **15** | **[SettingsView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/SettingsView.java)** | Heuristic Parameter Tuning | `JTextField` Threshold Inputs, Save Button | Admin-only panel to adjust engine sensitivities (Minimum Confidence Threshold, Anomaly Scan Interval) in Table 15 (`System_Settings`). |
| **16** | **[DatabaseBackupView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/DatabaseBackupView.java)** | Database Snapshot & Demo Reset | Snapshot Button, Reset Button, Status Log | Performs point-in-time file copy of `space_intel.db` into `backups/` directory; includes a button to re-execute `schema.sql` to restore default demo baseline. |
| **17** | **[AboutSystemView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/AboutSystemView.java)** | Syllabus & Architecture Guide | Rich Formatted `JEditorPane` / HTML | Displays project background, Xovian Aerospace partnership details, and comprehensive syllabus module mapping (Modules I to V). |
| **18** | **[AnalyticsDashboardView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/AnalyticsDashboardView.java)** | Statistical Telemetry Metrics | Progress Bars, Category Breakdown Panel | Computes and displays telemetry category distributions, provider trust rankings, and mean anomaly resolution percentages. |
| **19** | **[ReportHistoryView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/ReportHistoryView.java)** | Exported Report Archive | `JTable`, Open File Button (`Desktop.getDesktop()`) | Displays export history from Table 16 (`Report_History`). Clicking "Open Report" invokes `java.awt.Desktop.getDesktop().open(file)` to launch the report in Notepad/Excel. |
| **20** | **[LogoutView.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/ui/LogoutView.java)** | Clean Session Termination | Confirmation Buttons, Audit Hook | Prompts confirmation, executes `SessionManager.logout()`, disposes `MainFrame`, and returns safely to `LoginView` without memory leaks. |

---

### 4.7 Testing & Quality Assurance (`spaceintel.test`)

#### [SystemSmokeTest.java](file:///c:/Users/dhrub/Desktop/Java%20Microproject/src/spaceintel/test/SystemSmokeTest.java)
- **Role**: Automated Headless Verification Suite validating all subsystems before defense.
- **The 6 Automated Test Phases**:
  1. **Phase 1: Database & 16 Normalized Tables**: Validates that `space_intel.db` exists and verifies row counts for Users, Data Sources, Observations, and Credibility.
  2. **Phase 2: Authentication & RBAC**: Verifies credential hashing and role assignment for `admin`.
  3. **Phase 3: AI Confidence Engine**: Instantiates `AIEngine`, evaluates an observation, and confirms `ConfidenceScore`, `ReliabilityScore`, and `ConsistencyScore`.
  4. **Phase 4: Anomaly Detection Engine**: Executes `AnomalyEngine.runFullAnomalyScan()` and asserts discovery of all 5 anomaly types.
  5. **Phase 5: Report Generation File I/O**: Generates reports in TXT, CSV, and JSON, asserting file creation in `reports/`.
  6. **Phase 6: GUI Screen Instantiation**: Constructs `SplashScreenView`, `LoginView`, and `MainFrame`, confirming all 20 views instantiate cleanly without Swing layout exceptions.
- **Viva Defense**: *"Sir, before any demonstration, we execute `SystemSmokeTest.java` to programmatically verify that 100% of the database tables, AI heuristics, and UI views are operational."*

---

### 4.8 Build & Automation Scripts
- **[run.bat](file:///c:/Users/dhrub/Desktop/Java%20Microproject/run.bat)**: Windows batch script that sets compiler classpath, writes all source paths to `sources.txt`, invokes `javac`, and runs `spaceintel.Main`.
- **[run.ps1](file:///c:/Users/dhrub/Desktop/Java%20Microproject/run.ps1)**: Native PowerShell runner with error trapping and terminal diagnostics.
- **[test.ps1](file:///c:/Users/dhrub/Desktop/Java%20Microproject/test.ps1)**: PowerShell test runner that compiles and executes `spaceintel.test.SystemSmokeTest`.

---

## 5. Curriculum Syllabus Alignment (Modules I to V)

| Syllabus Module | Curricular Topic | Exact File & Code Implementation |
| :--- | :--- | :--- |
| **Module I: Java Programming Concepts** | Data Types, Control Structures, Input Validation | `ObservationEntryView.java` (lines 90–120) enforces coordinate bounds check (`lat between -90 and +90`, `lon between -180 and +180`); `AIEngine.java` (lines 68–80) implements mathematical rounding and variance formulas. |
| **Module II: Object-Oriented Programming (OOP)** | Encapsulation, Inheritance, Interfaces, Packages | All 10 classes in `spaceintel.model/` encapsulate private attributes with getters/setters; all UI views inherit from `javax.swing.JPanel`; `WebDashboardHandler` implements `HttpHandler`; 7 specialized packages prevent namespace collisions. |
| **Module III: Exception Handling, Multithreading & File I/O** | Custom Exceptions, `SwingWorker`, `PrintWriter`, Streams | `DatabaseManager.java` handles `SQLException` with `try-with-resources`; `SplashScreenView.java` uses `SwingWorker` background thread for progress animation; `ReportExporter.java` uses `PrintWriter` to stream formatted CSV, JSON, and TXT files. |
| **Module IV: Collections Framework & GUI Programming** | `List`, `Map`, `ArrayList`, Swing, `CardLayout`, Custom Renderers | `AIEngine.java` and `AnomalyEngine.java` process `List<Observation>` and `List<Anomaly>`; `MainFrame.java` uses `CardLayout` across 20 views; `ConfidenceAssessmentView.java` uses custom `TableCellRenderer` with `JProgressBar`. |
| **Module V: Database Connectivity & Web Development** | JDBC Persistence, Parameterized Queries, HTTP Web Server | `DatabaseManager.java` executes SQL across 16 normalized tables using `PreparedStatement`; `Main.java` starts an embedded `HttpServer` serving dynamic HTML5 web telemetry at `http://localhost:8080/dashboard`. |

---

## 6. Top 15 Teacher Viva Questions & Direct Code-Referenced Answers

#### Q1: Why did you use an embedded SQLite database instead of MySQL?
> **Answer**: *"Sir/Ma'am, we implemented a **Dual-Mode Enterprise Architecture**. For portability and zero-configuration grading, the application uses an embedded SQLite database (`space_intel.db`), allowing it to run out-of-the-box on any computer. Simultaneously, all 16 tables are defined in standard ANSI SQL in `resources/schema.sql`, which is 100% compatible with MySQL and phpMyAdmin."*

#### Q2: How does your AI Engine calculate confidence scores?
> **Answer**: *"In `AIEngine.java` (lines 41–114), we use a rule-based weighted formula: $\text{Confidence} = (0.50 \times \text{Reliability}) + (0.50 \times \text{Consistency})$. Reliability is looked up from Table 5 (`Source_Credibility`). Consistency is computed by comparing the observation against independent sensors in the same region using absolute percentage difference: $\text{percentDiff} = (\text{diff} / \text{base}) \times 100$. If score $\ge 85\%$, the decision is `PROCEED`; if $60\%\text{--}84.9\%$, `CORROBORATE`; and if $<60\%$, `REJECT`."*

#### Q3: What design patterns are implemented in your codebase?
> **Answer**:
> 1. **Model-View-Controller (MVC)**: Models in `spaceintel.model/`, Views in `spaceintel.ui/`, and Controllers in `spaceintel.engine/` and `spaceintel.db/`.
> 2. **Singleton Pattern**: In `DatabaseManager.java` (lines 44–53), ensuring a single shared connection manager exists.
> 3. **CardLayout Navigation**: In `MainFrame.java` (lines 77–118), switching views smoothly within a single frame.
> 4. **State Machine / Lexical Parser**: In `DatabaseManager.java` (lines 134–178) for parsing SQL scripts respecting quotes.

#### Q4: How is Role-Based Access Control (RBAC) enforced?
> **Answer**: *"In `SessionManager.java`, the authenticated user's role is stored in memory. In `MainFrame.java` (lines 198–286), the `rebuildSidebarMenu()` method checks `SessionManager.getRole()` and conditionally adds menu buttons. Administrative options like User Provisioning and Database Backup are strictly restricted to the `Administrator` role."*

#### Q5: How do you prevent SQL Injection?
> **Answer**: *"We strictly use JDBC `PreparedStatement` with parameterized placeholders (`?`) across `DatabaseManager.java` (e.g., lines 185–195 for authentication, lines 340–355 for observations), ensuring user input is treated strictly as literal data rather than executable SQL."*

#### Q6: How does the application satisfy Module V Web Development without Tomcat?
> **Answer**: *"In `Main.java` (lines 64–74), we start Java's native `com.sun.net.httpserver.HttpServer` on port 8080. The `WebDashboardHandler` class implements `HttpHandler`, queries `DatabaseManager.getInstance()`, and writes dynamic HTML5 telemetry to `http://localhost:8080/dashboard` with HTTP 200 OK headers."*

#### Q7: What are the 5 types of anomalies detected by your system?
> **Answer**: *"In `AnomalyEngine.java` (lines 39–159), `runFullAnomalyScan()` detects:
> 1. **Outlier Values**: Measurements violating physical limits (e.g., glaciological temperature $>15^\circ\text{C}$).
> 2. **Duplicate Observations**: Telemetry packets ingested multiple times from the same source with identical values.
> 3. **Conflicting Measurements**: Independent sensors in the same region reporting $>40\%$ divergence.
> 4. **Delayed Updates**: Hardware platforms with status `DEGRADED` or `OFFLINE`.
> 5. **Missing Data**: Telemetry records with Null Island coordinates (`latitude == 0.0 && longitude == 0.0`)."*

#### Q8: How does the system handle Multithreading?
> **Answer**: *"We implement multithreading in two places:
> 1. In `SplashScreenView.java`, a `SwingWorker` thread updates the progress bar and initializes database tables on a background thread so the Swing Event Dispatch Thread (EDT) never freezes.
> 2. In `Main.java`, the embedded HTTP web server handles incoming web dashboard requests concurrently on background threads."*

#### Q9: How is File I/O implemented for reporting?
> **Answer**: *"In `ReportExporter.java`, `generateReport()` uses `PrintWriter` and `FileWriter` inside a `try-with-resources` block to format and write reports to the `reports/` directory. It supports CSV, JSON, and ASCII-formatted TXT files, while logging each export to Table 16 (`Report_History`)."*

#### Q10: What happens if `space_intel.db` is deleted or missing?
> **Answer**: *"In `DatabaseManager.java` (lines 70–80), `initDatabase()` detects that the database file is absent, reads `resources/schema.sql`, parses the DDL and DML statements via `parseSqlStatements()`, and automatically recreates all 16 tables with seed data in under two seconds."*

#### Q11: How do you ensure database auditability?
> **Answer**: *"Through `AuditService.java`. Whenever a user logs in, enters telemetry, runs an anomaly scan, or exports a report, `AuditService.log()` timestamps the user's username, action, and target into Table 14 (`Audit_Log`), creating an immutable security audit trail."*

#### Q12: How did you solve the problem of semicolons inside SQL statements?
> **Answer**: *"In `DatabaseManager.java` (lines 134–178), `parseSqlStatements()` uses a state-machine parser. It iterates character-by-character tracking `inQuote` and `inLineComment` flags. A semicolon is only treated as a delimiter if it occurs outside single quotes, preventing split errors when column text contains semicolons."*

#### Q13: How is data consistency maintained between Swing and the Database?
> **Answer**: *"Every view queries the centralized singleton `DatabaseManager.getInstance()`. When records are modified (such as resolving an anomaly or adding an observation), the view commits the change via SQL `PreparedStatement` and immediately re-queries the database to refresh the `JTable` model."*

#### Q14: How does the UI support dark theme without third-party graphical artifacts?
> **Answer**: *"In `UITheme.java` (lines 56–105), we initialize `FlatDarkLaf` and configure global `UIManager` defaults for background, foreground, selection colors, and fonts across all Swing components, preventing white background bleed-through."*

#### Q15: How can you prove all parts of the application work before the viva?
> **Answer**: *"By running `SystemSmokeTest.java` (in `src/spaceintel/test/`). It executes a 6-phase headless automated test suite covering database persistence, role authentication, AI confidence scoring, anomaly scanning, report file I/O, and UI view instantiation with 100% pass verification."*
