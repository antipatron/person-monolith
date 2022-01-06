@echo off
echo.
echo ##############################################################

echo Starting build with docker...
cls
echo.
docker build --tag %IMAGE_NAME_MONO% .
docker run --name %CONTAINER_NAME_MONO% -d -e MYSQL_PASSWORD_DEV=%mySqlPassDev% -e MONGO_PASSWORD=%mongoPassDev% -e S3_USER_ONE=%s3UserOne% -e S3_ACCESS_KEY_ONE=%s3AccessKeyOne% -e S3_SECRET_KEY_ONE=%s3SecretKeyOne% -p 7979:7979 %IMAGE_NAME_MONO%:latest