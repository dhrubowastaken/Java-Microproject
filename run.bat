@echo off
title Xovian Aerospace - Space Intelligence and Geospatial Data Reliability System
echo ====================================================================
echo MICRO PROJECT 09: SPACE INTELLIGENCE AND GEOSPATIAL DATA SYSTEM
echo Organization: ABC National Geospatial Intelligence Centre
echo ====================================================================
echo.

if not exist bin mkdir bin
if not exist reports mkdir reports
if not exist backups mkdir backups

echo [1/2] Compiling Java Enterprise sources...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Get-ChildItem -Recurse -Filter '*.java' src | ForEach-Object { '\"' + $_.FullName.Replace('\', '/') + '\"' } | Set-Content -Encoding Ascii sources.txt"
javac -encoding UTF-8 -cp "lib\sqlite-jdbc.jar;lib\flatlaf.jar" -d bin @sources.txt
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Compilation failed! Please inspect the output above.
    if exist sources.txt del sources.txt
    pause
    exit /b %ERRORLEVEL%
)
if exist sources.txt del sources.txt
echo [OK] Compilation successful!
echo.

echo [2/2] Launching Space Intelligence Control Station and Web Server...
echo Desktop Application GUI will open shortly in Modern Aerospace Dark Theme.
echo Web Dashboard will be available at: http://localhost:8080/dashboard
echo.
java -cp "bin;lib\sqlite-jdbc.jar;lib\flatlaf.jar;resources" spaceintel.Main

pause
