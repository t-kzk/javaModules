FROM eclipse-temurin:21.0.3_9-jdk-jammy as build

ARG JAR_FILE=module-2-di-ioc-0.0.1.jar

WORKDIR /app

COPY build/libs/${JAR_FILE} application.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "application.jar"]

