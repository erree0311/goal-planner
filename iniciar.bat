@echo off
cd /d "%~dp0"
set "JAVA_EXE=java"
if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin\java.exe" (
    set "JAVA_EXE=C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot\bin\java.exe"
)
if "%ANTHROPIC_API_KEY%"=="" (
    echo [ERROR] No se encontro la variable ANTHROPIC_API_KEY.
    echo Crea una API key en https://console.anthropic.com/settings/keys
    echo y guardala una sola vez con:  setx ANTHROPIC_API_KEY "tu-api-key"
    echo Luego cierra esta ventana y vuelve a ejecutar iniciar.bat.
    pause
    exit /b 1
)
echo Iniciando Goal Planner...
start "GoalPlanner" /min "%JAVA_EXE%" -jar target\goal-planner-1.0.0.jar
timeout /t 8 /nobreak >nul
start "" http://localhost:8080
