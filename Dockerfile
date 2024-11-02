FROM amazoncorretto:21
WORKDIR /app
COPY target/racing-api-1.0.jar ./racing-api-1.0.jar
EXPOSE 8080
CMD ["java", "-jar", "racing-api-1.0.jar"]
