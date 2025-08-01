#!/bin/bash
git pull origin master
docker stop $(docker ps -a -q)
docker build -t morningapi .
docker run -d -p 192.168.1.132:8080:8080 morningapi
