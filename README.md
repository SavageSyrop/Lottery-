## Этап сборки

### 1. Сборка JAR-файла (через Maven)

`mvn clean package -DskipTests`

### 2. Docker-сборка

`docker build -t lottery-app .`

### 3. Запуск

`docker-compose up --build`