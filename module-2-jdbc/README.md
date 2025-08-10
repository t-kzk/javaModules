Запуск приложения

Что нужно: 
1. Docker 
2. Java 21+ 

Шаги: 
1. из корня проекта запустить контейнер с mySql docker compose up -d
2. из корня проекта запустить приложение ./gradlew clean run


БД:
url: jdbc:mysql://localhost:3306
user: root
password: secret