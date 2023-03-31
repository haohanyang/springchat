# SpringChat

A real-time texting application based STOMP(Streaming Text Oriented Messaging Protocol) and Spring framework, including
components such as Spring Boot, Spring
Security, Spring Messaging, etc.

## Demo
![screen_shoot](/static/screenshot1.png)

## Build and Run
### With gradle and JDK 17
```bash
# Client 
./gradlew -b build_client.gradle build -x test
java -jar build/libs/springchat-{VERSION}-SNAPSHOT.jar

# Server
./gradlew -b build_server.gradle build -x test
java -jar build/libs/springchat-{VERSION}-SNAPSHOT.jar
```
### Docker
```
docker build -f Dockerfile.client -t sprintchat_client .
docker build -f Dockerfile.server -t sprintchat_server .

docker run -d sprintchat_server
docker run -it sprintchat_client
```
## Features

* Authentication and authorization
* User to user messaging
* Group messaging

## Architecture

![architecture](/static/arch1.png)

## TODO

- [ ] Persistence with on-disk database
- [ ] Cache with in-memory database
- [ ] Message receipt
- [ ] Online status
- [ ] Distributed systems