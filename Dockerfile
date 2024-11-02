FROM amazoncorretto:21
WORKDIR /app
COPY target/racing-library-1.0-SNAPSHOT.jar ./racing-library-1.0-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "racing-library-1.0-SNAPSHOT.jar"]
