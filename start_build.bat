@echo off
echo.

echo Step 0: Add environment variable.
call set-env.bat
pause
echo Step 1: Start application
call build_docker.bat

exit

