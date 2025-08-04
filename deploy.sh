#!/bin/bash
git pull origin master
docker stop morning-api-container || true
docker build -t morning-api .
docker run -d --name morning-api-container -p 192.168.1.132:80:8080 morning-api
