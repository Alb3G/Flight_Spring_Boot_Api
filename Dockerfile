FROM ubuntu:latest AS build
RUN apt-get update && apt-get install openjdk-21-jdk -y
COPY . .
RUN ./gradlew bootjar --no-daemon

FROM openjdk:21-jdk-slim
EXPOSE 8888
COPY --from=build /build/libs/flights_api-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]