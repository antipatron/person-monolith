@echo off
echo.
echo ##############################################################
echo.
echo add enviroments
echo.

set IMAGE_NAME_MONO=person-monolith-i
set CONTAINER_NAME_MONO=person-monolith-i-con

set/p mySqlPassDev=Enter de mysql password dev:
set/p mongoPassDev=Enter mongo password dev:
set/p s3UserOne=Enter de s3 user:
set/p s3AccessKeyOne=Enter de s3 access key:
set/p s3SecretKeyOne=Enter de s3 secret key:

echo %mySqlPassDev% %mongoPassDev% %s3UserOne% %s3AccessKeyOne% %s3SecretKeyOne%

