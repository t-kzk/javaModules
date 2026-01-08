FROM gradle:8.14.3-jdk21 AS build

WORKDIR /home/gradle/project

COPY build.gradle .
COPY gradle.properties .
COPY src ./src

RUN gradle --no-daemon clean bootJar

FROM eclipse-temurin:21.0.3_9-jdk-jammy

WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]