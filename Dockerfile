FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
COPY src ./src

ENV SPRING_PROFILES_ACTIVE=test
RUN ./gradlew clean test build --no-daemon

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/WebServiceProject-0.0.1-SNAPSHOT.jar app.jar

COPY src/main/resources/mykeystore.p12 /app/resources/mykeystore.p12

EXPOSE 8443

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-Dserver.ssl.key-store=/app/resources/mykeystore.p12", "-Dserver.ssl.key-store-password=testet1", "-Dserver.ssl.key-store-type=PKCS12", "-Dserver.ssl.key-alias=mycert", "-jar", "app.jar"]
