FROM openjdk:17
COPY spring1app.jar /app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]