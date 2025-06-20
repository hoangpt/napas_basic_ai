@echo off
REM Build the Docker image
docker build -t react-nginx-app .

REM Run the container, mapping host port 88 to container port 88
docker run --rm -p 88:88 --name react-nginx-app react-nginx-app
