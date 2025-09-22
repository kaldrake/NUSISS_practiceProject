# Project Set up Guide

# Env 1: Dev Env (with local frontend backend and db) 
## db:
1. Find the SQL Script in the dbscript folder and run it. It generates all tables and sample data.
2. Change the db username and password in application.yml

## Frontend Angular App:
>npm install --legacy-peer-deps

>npm start
## Backend Spring App:
>mvn clean install -s .\tempSettings.xml   
>mvn spring-boot:run -s .\tempSettings.xml
## To verify:
Go to localhost:4200 and click Business, should be able to see some data there.

# Env 2: Dockerized frontend backend but connect to local db:
## db: 
1. Choose the right mode in application.yml.
2. Change the db username and password too.
## Build and start frontend and backend docker containers:
Go to backend folder
>docker network create myapp-net 2>/dev/null || true

>docker build -t my-backend:latest .

>docker run -p 8080:8080 --network myapp-net --name backend-test -e SPRING_DATASOURCE_URL='jdbc:mysql://host.docker.internal:3306/common_queue?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Singapore'  -e SPRING_DATASOURCE_USERNAME='root'  -e SPRING_DATASOURCE_PASSWORD='newpassword'  my-backend:latest

Go to frontend/common-queue-app folder
>docker build -t my-frontend:latest .

> docker run -d --name my-frontend --network myapp-net -p 80:80 my-frontend:latest

## To verify:
Go to localhost:80 and click Business, should be able to see some data there.

# Env 3: Prod Env
## db
1.Choose the right mode in application.yml. Don't change the password and username.

## Build and start docker containers:
Run in the root folder:
>docker compose up -d --build

## To verify:
Go to localhost:80
Click Business, should be able to see some data there.
If not, run the command below to manually load init.sql:
>docker exec -i db mysql -uroot -prootpass common_queue < ./db/init/init.sql

## Check logs:
docker compose logs -f db