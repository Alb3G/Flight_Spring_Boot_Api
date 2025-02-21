FROM ubuntu:latest AS build
# Actualizar apt y instalar dependencias necesarias
RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    wget \
    unzip

# Instalar Gradle
RUN wget https://services.gradle.org/distributions/gradle-8.12.1-bin.zip
RUN unzip gradle-8.12.1-bin.zip
RUN mv gradle-8.12.1 /opt/gradle
ENV PATH="/opt/gradle/bin:${PATH}"

# Copiar los archivos del proyecto al contenedor y construir el JAR
COPY . .
RUN ./gradlew bootJar --no-daemon

# Verificar si el JAR se ha generado correctamente
RUN ls -l build/libs

FROM openjdk:21-jdk-slim
EXPOSE 8080

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /app/build/libs/flights_api-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
