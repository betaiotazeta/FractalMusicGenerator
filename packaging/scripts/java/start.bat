@echo off
setlocal

rem Change to the directory containing this script.
cd /d "%~dp0"

rem Find the application JAR.
set "JAR="
for %%F in (FractalMusicGenerator-*.jar) do (
    set "JAR=%%F"
    goto :found
)

echo Error: No FractalMusicGenerator-*.jar found.
pause
exit /b 1

:found
rem Launch the application detached from this batch file.
start "" javaw -jar "%JAR%" %*

endlocal