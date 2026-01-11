# Учебный проект (хранение и изменение файлов)

**Стек**: Java 21, Spring Boot 3.5, WebFlux, Spring Security (JWT), MapStruct, Lombok, Springdoc OpenAPI, MinIO, Testcontainers, Docker Compose, MySQL.


---
## Локальный запуск 
> Требуется: **Docker** (compose), **JDK 21**. Порты по умолчанию: 8080 (сервис), 3306 (Mysql), 9000 (Minio).

В IntelliJ:

Run configuration → Modify options → Environment variables → Load from file → [application.env](src/main/resources/application.env)

Запустить сервис через сконфигурированный main 

## Локальный запуск тестов 
из корня подмудоля module-2-di-ioc

   ```bash
   gradle clean test
   ```

## Запуск через docker compose 

   ```bash
   docker compose down
   ```

   ```bash
   docker compose up -d
   ```

## Swagger 
http://localhost:8080/webjars/swagger-ui/index.html