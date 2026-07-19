@echo off
cd /d "%~dp0"
set "JAVA_EXE=java"
if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin\java.exe" (
    set "JAVA_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin\java.exe"
)
echo Iniciando Goal Planner...
start "GoalPlanner" /min "%JAVA_EXE%" -jar target\goal-planner-1.0.0.jar
timeout /t 8 /nobreak >nul
start "" http://localhost:8080
