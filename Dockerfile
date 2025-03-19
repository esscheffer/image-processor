# Use an official Java 11 image as the base
FROM gradle:8.13.0-jdk21-alpine AS build

COPY src /app/src
COPY build.gradle /app/build.gradle
COPY settings.gradle /app/settings.gradle
COPY gradlew /app/gradlew
COPY gradle /app/gradle

WORKDIR /app

RUN ./gradlew build

FROM amazoncorretto:21-alpine3.21

COPY --from=build /app/build/libs/image-processor-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]