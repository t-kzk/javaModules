перед запуском контейнеров необходимо пересобрать. 

запуск локально
Прописать run конфигурацию для сервиса - Active profiles local

Для этого зайти в меню Run -> Edit Configurations -> выбрать main класс -> указать Active profiles: local
[Инструкция](https://stackoverflow.com/questions/39738901/how-do-i-activate-a-spring-boot-profile-when-running-from-intellij).




для запуска локально необходимо
В IntelliJ:

Run configuration → Modify options → Environment variables → Load from file → .env

добавить файл module-2-di-ioc/src/main/resources/application.env



swagger http://localhost:8080/webjars/swagger-ui/index.html#/