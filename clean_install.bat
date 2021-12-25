echo ############################## Maven clean
call .\mvnw clean
pause

echo ############################## Maven install
call .\mvnw install -DskipTests
pause

echo ############################## Maven run
call .\mvnw spring-boot:run
pause

echo Finished run
pause
