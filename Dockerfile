FROM openjdk:21
WORKDIR /api
COPY target/racing-api-1.0.jar /api/racing-api-1.0.jar
CMD ["java", "-jar", "racing-api-1.0.jar"]
EXPOSE 8080
