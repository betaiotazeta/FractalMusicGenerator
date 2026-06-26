@echo off

rem Change to the directory containing this script.
cd /d "%~dp0"

rem Launch the application without a console window.
javaw -jar FractalMusicGenerator-*.jar %*
