FROM maven:3.8.7-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app

COPY --from=build /app/target/LOTTERY-*.jar ./app.jar

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/lottery
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_FLYWAY_ENABLED=true

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]