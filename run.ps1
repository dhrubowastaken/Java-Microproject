# ====================================================================
# MICRO PROJECT 09: SPACE INTELLIGENCE & GEOSPATIAL DATA SYSTEM
# Organization: ABC National Geospatial Intelligence Centre (Xovian Aerospace)
# ====================================================================

Write-Host "====================================================================" -ForegroundColor Cyan
Write-Host "MICRO PROJECT 09: SPACE INTELLIGENCE AND GEOSPATIAL DATA SYSTEM" -ForegroundColor Cyan
Write-Host "Organization: ABC National Geospatial Intelligence Centre" -ForegroundColor Cyan
Write-Host "====================================================================" -ForegroundColor Cyan
Write-Host ""

if (-not (Test-Path "bin")) { New-Item -ItemType Directory -Path "bin" | Out-Null }
if (-not (Test-Path "reports")) { New-Item -ItemType Directory -Path "reports" | Out-Null }
if (-not (Test-Path "backups")) { New-Item -ItemType Directory -Path "backups" | Out-Null }

Write-Host "[1/2] Compiling Java Enterprise sources..." -ForegroundColor Yellow
$sources = Get-ChildItem -Recurse -Filter "*.java" src | ForEach-Object { $_.FullName }
javac -encoding UTF-8 -cp "lib/sqlite-jdbc.jar;lib/flatlaf.jar" -d bin $sources

if ($LASTEXITCODE -ne 0) {
    Write-Host "[ERROR] Compilation failed! Please inspect output above." -ForegroundColor Red
    pause
    exit $LASTEXITCODE
}

Write-Host "[OK] Compilation successful!" -ForegroundColor Green
Write-Host ""
Write-Host "[2/2] Launching Space Intelligence Control Station and Web Server..." -ForegroundColor Yellow
Write-Host "Desktop Application GUI will open shortly in Modern Aerospace Dark Theme." -ForegroundColor White
Write-Host "Web Dashboard will be available at: http://localhost:8080/dashboard" -ForegroundColor Cyan
Write-Host ""

java -cp "bin;lib/sqlite-jdbc.jar;lib/flatlaf.jar;resources" spaceintel.Main
