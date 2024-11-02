#Build
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests
#Deploy
FROM openjdk:21
WORKDIR /api
COPY --from=build /app/target/racing-api-1.0.jar /api/racing-api-1.0.jar
CMD ["java", "-jar", "racing-api-1.0.jar"]
EXPOSE 8080
