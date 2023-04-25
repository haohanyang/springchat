FROM gradle:jdk17-focal as builder

COPY ./ /home/springchat

WORKDIR /home/springchat

ARG VERSION=0.0.1

RUN ./gradlew :client:build

FROM eclipse-temurin:17 as runner

WORKDIR /app

COPY --from=builder /home/springchat/client/build/libs/springchat-0.0.1-SNAPSHOT.jar app.jar

CMD [ "java", "-jar", "app.jar" ]