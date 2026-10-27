-- ====================================================================
-- MICRO PROJECT 09: AI-BASED SPACE INTELLIGENCE & GEOSPATIAL DATA RELIABILITY SYSTEM
-- Organization: ABC National Geospatial Intelligence Centre (Xovian Aerospace)
-- Database: space_intel (16 Normalized Tables, Standard SQL compatible with SQLite & MySQL)
-- ====================================================================

-- 1. USERS TABLE
-- Manages authentication & Role-Based Access Control (Admin, Analyst, Intel Officer, Disaster Officer, Data Scientist)
CREATE TABLE IF NOT EXISTS Users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL, -- Administrator, Geospatial Analyst, Intelligence Officer, Disaster Management Officer, Data Scientist
    department VARCHAR(100) DEFAULT 'Space Intelligence Directorate',
    email VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. DATA_SOURCES TABLE
-- Manages remote sensing satellites, drones, GIS databases, weather stations, GPS, and sensors
CREATE TABLE IF NOT EXISTS Data_Sources (
    source_id VARCHAR(50) PRIMARY KEY,
    source_name VARCHAR(100) NOT NULL,
    source_type VARCHAR(50) NOT NULL, -- Satellite, Drone, GIS, Weather Station, GPS, Sensor
    organization VARCHAR(100) NOT NULL, -- ISRO, ESA, NASA, IMD, Survey of India, Air Force
    coverage_area VARCHAR(100) NOT NULL,
    status VARCHAR(30) DEFAULT 'ACTIVE', -- ACTIVE, DEGRADED, CALIBRATING, OFFLINE
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. REGIONS TABLE
-- Geographical zones monitored for space intelligence and disaster tracking
CREATE TABLE IF NOT EXISTS Regions (
    region_id VARCHAR(50) PRIMARY KEY,
    region_name VARCHAR(100) NOT NULL,
    center_lat REAL NOT NULL,
    center_lon REAL NOT NULL,
    terrain_type VARCHAR(50) NOT NULL, -- Forest, Coastal, Desert, Mountain, Urban
    threat_level VARCHAR(30) DEFAULT 'NORMAL', -- NORMAL, ELEVATED, CRITICAL
    description TEXT
);

-- 4. OBSERVATIONS TABLE
-- Operational telemetry and geospatial data collected from all sources
CREATE TABLE IF NOT EXISTS Observations (
    observation_id VARCHAR(50) PRIMARY KEY,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    observation_type VARCHAR(50) NOT NULL, -- Thermal Anomaly, NDVI Vegetation, Atmospheric Pressure, Optical Surface, Flood Inundation
    measured_value REAL NOT NULL,
    unit VARCHAR(20) NOT NULL, -- °C, index, hPa, reflectance, meters
    source_id VARCHAR(50) NOT NULL,
    region_id VARCHAR(50) NOT NULL,
    status VARCHAR(30) DEFAULT 'RAW', -- RAW, VERIFIED, CONFLICTING, ANOMALOUS
    FOREIGN KEY (source_id) REFERENCES Data_Sources(source_id),
    FOREIGN KEY (region_id) REFERENCES Regions(region_id)
);

-- 5. OBSERVATION_HISTORY TABLE
-- Historical observation records for trend analysis and audit compliance
CREATE TABLE IF NOT EXISTS Observation_History (
    history_id INTEGER PRIMARY KEY AUTOINCREMENT,
    observation_id VARCHAR(50) NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    latitude REAL NOT NULL,
    longitude REAL NOT NULL,
    measured_value REAL NOT NULL,
    source_id VARCHAR(50) NOT NULL,
    flag VARCHAR(50) DEFAULT 'ARCHIVED'
);

-- 6. SOURCE_CREDIBILITY TABLE
-- Tracks dynamic reliability scores and historical validation metrics per data provider
CREATE TABLE IF NOT EXISTS Source_Credibility (
    credibility_id INTEGER PRIMARY KEY AUTOINCREMENT,
    source_id VARCHAR(50) UNIQUE NOT NULL,
    reliability_score REAL NOT NULL, -- Range: 0.0 - 100.0%
    historical_accuracy REAL NOT NULL, -- Range: 0.0 - 100.0%
    update_frequency VARCHAR(50) NOT NULL, -- Real-time, 15 min, 1 Hour, Daily
    validation_status VARCHAR(50) NOT NULL, -- CERTIFIED, PROVISIONAL, UNDER_REVIEW
    confidence_level VARCHAR(30) NOT NULL, -- HIGH, MEDIUM, LOW
    last_evaluated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (source_id) REFERENCES Data_Sources(source_id)
);

-- 7. CONFIDENCE_ASSESSMENT TABLE
-- Explainable AI confidence metrics computed from multi-source corroboration
CREATE TABLE IF NOT EXISTS Confidence_Assessment (
    assessment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    observation_id VARCHAR(50) NOT NULL,
    confidence_score REAL NOT NULL, -- 0.0 - 100.0%
    reliability_score REAL NOT NULL,
    consistency_score REAL NOT NULL,
    evidence_summary TEXT NOT NULL,
    recommended_decision VARCHAR(100) NOT NULL, -- PROCEED, CORROBORATE, REJECT, ESCALATE
    assessed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (observation_id) REFERENCES Observations(observation_id)
);

-- 8. ANOMALIES TABLE
-- Records detected data defects: Missing Data, Duplicates, Conflicts, Outliers, Delayed Updates
CREATE TABLE IF NOT EXISTS Anomalies (
    anomaly_id INTEGER PRIMARY KEY AUTOINCREMENT,
    observation_id VARCHAR(50),
    source_id VARCHAR(50) NOT NULL,
    anomaly_type VARCHAR(50) NOT NULL, -- Missing Data, Duplicate Observations, Conflicting Measurements, Outlier Values, Delayed Updates
    severity VARCHAR(30) NOT NULL, -- LOW, MEDIUM, HIGH, CRITICAL
    detected_value REAL,
    expected_range VARCHAR(50),
    description TEXT NOT NULL,
    detected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolution_status VARCHAR(30) DEFAULT 'OPEN' -- OPEN, INVESTIGATING, RESOLVED
);

-- 9. EVIDENCE TABLE
-- Stores corroborating and contradicting sensor observations backing each AI decision
CREATE TABLE IF NOT EXISTS Evidence (
    evidence_id INTEGER PRIMARY KEY AUTOINCREMENT,
    assessment_id INTEGER NOT NULL,
    supporting_observation VARCHAR(255) NOT NULL,
    contradicting_observation VARCHAR(255),
    confidence_explanation TEXT NOT NULL,
    recommendation TEXT NOT NULL,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (assessment_id) REFERENCES Confidence_Assessment(assessment_id)
);

-- 10. INTELLIGENCE_REPORTS TABLE
-- High-level operational intelligence dossiers generated for decision-makers
CREATE TABLE IF NOT EXISTS Intelligence_Reports (
    report_id VARCHAR(50) PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    author VARCHAR(100) NOT NULL,
    region_id VARCHAR(50) NOT NULL,
    executive_summary TEXT NOT NULL,
    threat_assessment VARCHAR(50) NOT NULL, -- STABLE, WATCH, WARNING, CRITICAL
    confidence_rating REAL NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (region_id) REFERENCES Regions(region_id)
);

-- 11. ALERTS TABLE
-- Active situational warnings dispatched across agency departments
CREATE TABLE IF NOT EXISTS Alerts (
    alert_id INTEGER PRIMARY KEY AUTOINCREMENT,
    alert_code VARCHAR(30) NOT NULL,
    region_id VARCHAR(50) NOT NULL,
    severity VARCHAR(30) NOT NULL, -- INFO, ADVISORY, WARNING, EMERGENCY
    title VARCHAR(150) NOT NULL,
    message TEXT NOT NULL,
    is_acknowledged INTEGER DEFAULT 0, -- 0 = Unread, 1 = Acknowledged
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (region_id) REFERENCES Regions(region_id)
);

-- 12. DASHBOARD_DATA TABLE
-- Centralized telemetry aggregates for rapid KPI rendering
CREATE TABLE IF NOT EXISTS Dashboard_Data (
    metric_id VARCHAR(50) PRIMARY KEY,
    metric_name VARCHAR(100) NOT NULL,
    metric_value VARCHAR(50) NOT NULL,
    unit VARCHAR(20),
    trend VARCHAR(30), -- UP, DOWN, STABLE
    category VARCHAR(50) NOT NULL, -- SATELLITE, RELIABILITY, ANOMALIES, SYSTEM
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 13. LOGIN_LOG TABLE
-- Security access log recording every authentication attempt
CREATE TABLE IF NOT EXISTS Login_Log (
    log_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    login_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45) DEFAULT '127.0.0.1',
    status VARCHAR(30) NOT NULL -- SUCCESS, FAILED_CREDENTIALS, LOCKED
);

-- 14. AUDIT_LOG TABLE
-- Immutable traceability log of all system modifications and analyst actions
CREATE TABLE IF NOT EXISTS Audit_Log (
    audit_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_name VARCHAR(50) NOT NULL,
    action VARCHAR(100) NOT NULL,
    entity_affected VARCHAR(50) NOT NULL,
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 15. SYSTEM_SETTINGS TABLE
-- System thresholds, tolerance limits, and operational parameters
CREATE TABLE IF NOT EXISTS System_Settings (
    setting_key VARCHAR(50) PRIMARY KEY,
    setting_value VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL, -- AI_ENGINE, SENSOR_TOLERANCE, UI_THEME
    description TEXT
);

-- 16. REPORT_HISTORY TABLE
-- Audit archive of generated report exports and downloads
CREATE TABLE IF NOT EXISTS Report_History (
    history_id INTEGER PRIMARY KEY AUTOINCREMENT,
    report_type VARCHAR(50) NOT NULL, -- Observation Report, Source Reliability Report, Confidence Report, Anomaly Report
    generated_by VARCHAR(50) NOT NULL,
    file_format VARCHAR(20) NOT NULL, -- CSV, JSON, TXT, PDF
    file_path VARCHAR(255),
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ====================================================================
-- SEED DATA: Realistic Xovian Aerospace & Geospatial Intelligence Records
-- ====================================================================

-- Users (All 5 Roles)
INSERT INTO Users (username, password, full_name, role, department, email) VALUES
('admin', 'admin123', 'Commander Vikram Sharma', 'Administrator', 'Space Operations Command', 'v.sharma@xovian-space.gov.in'),
('analyst', 'analyst123', 'Dr. Radhika Nair', 'Geospatial Analyst', 'Earth Observation Division', 'r.nair@xovian-space.gov.in'),
('intel', 'intel123', 'Col. Rajiv Menon', 'Intelligence Officer', 'Strategic Analysis Wing', 'r.menon@xovian-space.gov.in'),
('disaster', 'disaster123', 'Ananya Deshmukh', 'Disaster Management Officer', 'Emergency Response Centre', 'a.deshmukh@ndma.gov.in'),
('scientist', 'scientist123', 'Dr. Arun Patel', 'Data Scientist', 'AI Research & Telemetry Lab', 'a.patel@xovian-space.gov.in');

-- Regions
INSERT INTO Regions (region_id, region_name, center_lat, center_lon, terrain_type, threat_level, description) VALUES
('REG-01', 'Dehradun Forest Reserve', 30.3165, 78.0322, 'Forest', 'ELEVATED', 'Himalayan foothills forest cover with thermal wildfire risk'),
('REG-02', 'Bay of Bengal Cyclone Zone', 16.5000, 88.0000, 'Coastal', 'CRITICAL', 'Monsoon low-pressure system and storm surge surveillance corridor'),
('REG-03', 'Western Ghats Water Basin', 11.5000, 76.5000, 'Mountain', 'NORMAL', 'Key reservoir catchment areas and soil moisture monitoring'),
('REG-04', 'Thar Border Sector', 27.0238, 71.0000, 'Desert', 'NORMAL', 'High temperature desert expanse and terrain reconnaissance'),
('REG-05', 'Ladakh Glacier Basin', 34.1526, 77.5771, 'Mountain', 'ELEVATED', 'High-altitude cryosphere glaciological melt and avalanche watch');

-- Data Sources (Satellites, Drones, GIS, Weather Stations, GPS, Sensors)
INSERT INTO Data_Sources (source_id, source_name, source_type, organization, coverage_area, status) VALUES
('SAT-CARTO3', 'Cartosat-3 Optical Constellation', 'Satellite', 'ISRO', 'Pan-India High Res', 'ACTIVE'),
('SAT-SENT2A', 'Sentinel-2A Multispectral', 'Satellite', 'ESA', 'Global Multispectral', 'ACTIVE'),
('SAT-INSAT3', 'INSAT-3DR Meteorological Imager', 'Satellite', 'ISRO / IMD', 'South Asia & Indian Ocean', 'ACTIVE'),
('DRN-SCOUT1', 'SkyScout Alpha Long-Endurance UAV', 'Drone', 'Xovian Aerospace', 'Sector North Tactical', 'ACTIVE'),
('DRN-HERON2', 'Heron-TP Heavy Recon UAV', 'Drone', 'National Drone Fleet', 'Coastal Reconnaissance', 'ACTIVE'),
('WTH-RADAR1', 'Doppler Weather Radar Paradip', 'Weather Station', 'IMD', 'East Coast Maritime', 'ACTIVE'),
('WTH-HIMAL1', 'Himalayan Automatic Weather Station', 'Weather Station', 'Snow & Avalanche Wing', 'Northern High Peaks', 'DEGRADED'),
('GPS-NAVIC1', 'NavIC Geodetic Reference Ref-01', 'GPS', 'ISRO Space Center', 'Subcontinent Base', 'ACTIVE'),
('GIS-BHUVAN', 'Bhuvan National Spatial Database', 'GIS', 'NRSC / ISRO', 'National Geospatial Layer', 'ACTIVE'),
('SNR-AQGRID', 'Environmental Sensor Grid Array', 'Sensor', 'Central Pollution Board', 'Gangetic Plain Network', 'ACTIVE');

-- Source Credibility
INSERT INTO Source_Credibility (source_id, reliability_score, historical_accuracy, update_frequency, validation_status, confidence_level) VALUES
('SAT-CARTO3', 96.5, 98.2, '15 min', 'CERTIFIED', 'HIGH'),
('SAT-SENT2A', 94.8, 96.0, 'Daily', 'CERTIFIED', 'HIGH'),
('SAT-INSAT3', 92.0, 93.5, '15 min', 'CERTIFIED', 'HIGH'),
('DRN-SCOUT1', 89.2, 91.0, 'Real-time', 'CERTIFIED', 'HIGH'),
('DRN-HERON2', 87.5, 88.4, 'Real-time', 'CERTIFIED', 'MEDIUM'),
('WTH-RADAR1', 93.1, 94.7, '10 min', 'CERTIFIED', 'HIGH'),
('WTH-HIMAL1', 68.4, 72.0, '1 Hour', 'UNDER_REVIEW', 'LOW'),
('GPS-NAVIC1', 99.1, 99.8, 'Real-time', 'CERTIFIED', 'HIGH'),
('GIS-BHUVAN', 95.0, 97.5, 'Daily', 'CERTIFIED', 'HIGH'),
('SNR-AQGRID', 79.4, 82.0, '5 min', 'PROVISIONAL', 'MEDIUM');

-- Observations
INSERT INTO Observations (observation_id, timestamp, latitude, longitude, observation_type, measured_value, unit, source_id, region_id, status) VALUES
('OBS-2026-001', '2026-10-27 10:15:00', 30.3200, 78.0400, 'Thermal Anomaly', 64.8, '°C', 'SAT-CARTO3', 'REG-01', 'VERIFIED'),
('OBS-2026-002', '2026-10-27 10:18:00', 30.3210, 78.0415, 'Thermal Anomaly', 66.2, '°C', 'DRN-SCOUT1', 'REG-01', 'VERIFIED'),
('OBS-2026-003', '2026-10-27 10:20:00', 16.5200, 88.0500, 'Atmospheric Pressure', 972.4, 'hPa', 'SAT-INSAT3', 'REG-02', 'VERIFIED'),
('OBS-2026-004', '2026-10-27 10:25:00', 16.5180, 88.0480, 'Atmospheric Pressure', 974.1, 'hPa', 'WTH-RADAR1', 'REG-02', 'VERIFIED'),
('OBS-2026-005', '2026-10-27 10:30:00', 11.5100, 76.5200, 'NDVI Vegetation', 0.78, 'index', 'SAT-SENT2A', 'REG-03', 'VERIFIED'),
('OBS-2026-006', '2026-10-27 10:35:00', 27.0250, 71.0100, 'Optical Surface', 48.5, '°C', 'SAT-CARTO3', 'REG-04', 'VERIFIED'),
('OBS-2026-007', '2026-10-27 10:40:00', 34.1550, 77.5800, 'Glacial Temperature', -4.2, '°C', 'SAT-SENT2A', 'REG-05', 'VERIFIED'),
('OBS-2026-008', '2026-10-27 10:42:00', 34.1550, 77.5800, 'Glacial Temperature', 22.8, '°C', 'WTH-HIMAL1', 'REG-05', 'ANOMALOUS'),
('OBS-2026-009', '2026-10-27 10:45:00', 30.3200, 78.0400, 'Thermal Anomaly', 64.8, '°C', 'SAT-CARTO3', 'REG-01', 'CONFLICTING'),
('OBS-2026-010', '2026-10-27 10:50:00', 16.5000, 88.0000, 'Flood Inundation', 4.8, 'meters', 'DRN-HERON2', 'REG-02', 'VERIFIED');

-- Observation History
INSERT INTO Observation_History (observation_id, recorded_at, latitude, longitude, measured_value, source_id, flag) VALUES
('OBS-2026-001', '2026-10-27 09:15:00', 30.3200, 78.0400, 52.3, 'SAT-CARTO3', 'PRIOR_PASS'),
('OBS-2026-001', '2026-10-27 10:15:00', 30.3200, 78.0400, 64.8, 'SAT-CARTO3', 'ACTIVE_SPIKE'),
('OBS-2026-003', '2026-10-27 09:20:00', 16.5200, 88.0500, 988.0, 'SAT-INSAT3', 'STORM_DEEPENING');

-- Confidence Assessment
INSERT INTO Confidence_Assessment (observation_id, confidence_score, reliability_score, consistency_score, evidence_summary, recommended_decision) VALUES
('OBS-2026-001', 94.5, 96.5, 92.5, 'Corroborated by SkyScout UAV thermal infrared pass (+1.4°C variance within tolerance)', 'PROCEED'),
('OBS-2026-003', 95.8, 92.0, 98.0, 'Confirmed by Paradip Doppler Radar with barometric gradient agreement', 'PROCEED'),
('OBS-2026-008', 31.2, 68.4, 12.0, 'Major discrepancy: Himalayan Station reports +22.8°C while Sentinel-2A glaciological scan reads -4.2°C', 'REJECT'),
('OBS-2026-010', 88.4, 87.5, 89.3, 'Confirmed by coastal UAV multispectral flood mapping', 'PROCEED');

-- Evidence
INSERT INTO Evidence (assessment_id, supporting_observation, contradicting_observation, confidence_explanation, recommendation) VALUES
(1, 'OBS-2026-002: SkyScout Alpha confirms 66.2°C hotspot at same coordinates', 'None', 'Multi-platform confirmation from space and tactical UAV validates active thermal outbreak.', 'Dispatch Dehradun Forest Ranger Rapid Response Unit.'),
(2, 'OBS-2026-004: Paradip Doppler Radar logs 974.1 hPa corroborating low pressure eye', 'None', 'Satellite and ground radar show aligned pressure drop, indicating cyclone intensification.', 'Issue Level-4 Maritime Cyclone Warning for coastal shipping.'),
(3, 'None', 'OBS-2026-007: Sentinel-2A radiometric temp is -4.2°C, snow-pack confirmed intact', 'Sensor telemetry drift detected in Himalayan AWS thermal probe (+27°C outlier).', 'Suspend WTH-HIMAL1 telemetry feed; dispatch calibration team.'),
(4, 'GIS-BHUVAN: Digital Elevation Model shows low-lying delta susceptible to tidal ingress', 'None', 'High UAV optical match combined with regional hydrologic flood vulnerability.', 'Evacuate vulnerable river-mouth hamlets in Kendrapara district.');

-- Anomalies
INSERT INTO Anomalies (observation_id, source_id, anomaly_type, severity, detected_value, expected_range, description, resolution_status) VALUES
('OBS-2026-008', 'WTH-HIMAL1', 'Outlier Values', 'CRITICAL', 22.8, '-15.0 to 5.0 °C', 'High-altitude snow station temperature impossible for current season (-4.2°C actual).', 'OPEN'),
('OBS-2026-009', 'SAT-CARTO3', 'Duplicate Observations', 'MEDIUM', 64.8, 'Unique per 15 min', 'Duplicate frame received within 2 seconds of prior ingestion.', 'RESOLVED'),
('OBS-2026-008', 'WTH-HIMAL1', 'Conflicting Measurements', 'HIGH', 22.8, 'Aligned with Sentinel-2A', 'Glacial temperature reading contradicts satellite infrared by 27.0°C.', 'INVESTIGATING'),
(NULL, 'SNR-AQGRID', 'Delayed Updates', 'LOW', 0.0, 'Update every 5 min', 'Sensor cluster 04 has not transmitted telemetry for 42 minutes.', 'OPEN'),
(NULL, 'DRN-HERON2', 'Missing Data', 'MEDIUM', 0.0, 'Telemetry Packet', 'Packet sequence 4012 dropped during satellite downlink handoff.', 'RESOLVED');

-- Intelligence Reports
INSERT INTO Intelligence_Reports (report_id, title, author, region_id, executive_summary, threat_assessment, confidence_rating) VALUES
('INTEL-2026-R01', 'Forest Wildfire Outbreak Flash Report', 'Dr. Radhika Nair', 'REG-01', 'Multi-source thermal infrared imaging confirms expanding canopy fire 8km north of Dehradun reserve.', 'WARNING', 94.5),
('INTEL-2026-R02', 'Cyclone Deepening Intelligence Dossier', 'Col. Rajiv Menon', 'REG-02', 'Barometric pressure plunged to 972 hPa in Bay of Bengal with storm surge threat along Odisha coast.', 'CRITICAL', 95.8),
('INTEL-2026-R03', 'Himalayan Cryosphere Telemetry Integrity Audit', 'Dr. Arun Patel', 'REG-05', 'Ground sensor hardware malfunction identified at high altitude; satellite fallback active.', 'WATCH', 89.0);

-- Alerts
INSERT INTO Alerts (alert_code, region_id, severity, title, message, is_acknowledged) VALUES
('ALT-FIRE-01', 'REG-01', 'WARNING', 'Wildfire Thermal Anomaly Detected', 'Thermal cluster of 66.2°C verified by Cartosat-3 and SkyScout UAV in Sector 4.', 0),
('ALT-CYC-02', 'REG-02', 'EMERGENCY', 'Severe Cyclonic Depression Low Pressure', 'Central pressure 972.4 hPa detected; wind shear speed exceeding 110 km/h anticipated.', 0),
('ALT-SENS-03', 'REG-05', 'ADVISORY', 'Sensor Telemetry Inconsistency Flagged', 'Himalayan station reading quarantined due to extreme outlier discrepancy with Sentinel-2A.', 1),
('ALT-NET-04', 'REG-03', 'INFO', 'Catchment Basin Moisture Stable', 'Western Ghats reservoir level optimal; normal satellite NDVI vegetation reading 0.78.', 1);

-- Dashboard Data
INSERT INTO Dashboard_Data (metric_id, metric_name, metric_value, unit, trend, category) VALUES
('METRIC-SOURCES', 'Active Space & Ground Sources', '10', 'Units', 'STABLE', 'SYSTEM'),
('METRIC-OBS', 'Total Observations Ingested', '1,420', 'Records', 'UP', 'SATELLITE'),
('METRIC-CONFIDENCE', 'Mean AI Confidence Score', '92.4', '%', 'UP', 'RELIABILITY'),
('METRIC-ANOMALIES', 'Active Anomalies Detected', '3', 'Cases', 'DOWN', 'ANOMALIES'),
('METRIC-THREATS', 'Active Operational Alerts', '4', 'Alerts', 'STABLE', 'SYSTEM'),
('METRIC-CREDIBILITY', 'Certified Data Providers', '8/10', 'Sources', 'STABLE', 'RELIABILITY');

-- Login Log
INSERT INTO Login_Log (username, role, status) VALUES
('admin', 'Administrator', 'SUCCESS'),
('analyst', 'Geospatial Analyst', 'SUCCESS'),
('intel', 'Intelligence Officer', 'SUCCESS');

-- Audit Log
INSERT INTO Audit_Log (user_name, action, entity_affected, details) VALUES
('admin', 'SYSTEM_INITIALIZATION', 'System', 'Database initialized with 16 normalized tables and Xovian Aerospace baseline data.'),
('analyst', 'ANOMALY_FLAG', 'Observation OBS-2026-008', 'Flagged Himalayan AWS temperature reading as critical outlier.'),
('intel', 'REPORT_PUBLISHED', 'Report INTEL-2026-R01', 'Published Forest Wildfire Outbreak Flash Report with 94.5% confidence rating.');

-- System Settings
INSERT INTO System_Settings (setting_key, setting_value, category, description) VALUES
('CONFIDENCE_THRESHOLD_HIGH', '85.0', 'AI_ENGINE', 'Minimum confidence score to automatically approve situational recommendations.'),
('CONFIDENCE_THRESHOLD_MEDIUM', '60.0', 'AI_ENGINE', 'Threshold requiring secondary sensor corroboration before alert dispatch.'),
('OUTLIER_SIGMA_FACTOR', '2.5', 'AI_ENGINE', 'Standard deviation factor used by statistical anomaly engine.'),
('SENSOR_PING_TIMEOUT_SEC', '300', 'SENSOR_TOLERANCE', 'Maximum allowable delay before triggering a Delayed Updates anomaly.'),
('DEFAULT_THEME', 'AEROSPACE_DARK', 'UI_THEME', 'Primary visual design system theme for command consoles.');

-- Report History
INSERT INTO Report_History (report_type, generated_by, file_format, file_path) VALUES
('Daily Intelligence Summary', 'analyst', 'TXT', 'reports/Daily_Intel_Summary_20261027.txt'),
('Source Reliability Report', 'scientist', 'CSV', 'reports/Source_Reliability_Q4.csv'),
('Anomaly Report', 'admin', 'JSON', 'reports/Anomaly_Log_20261027.json');
