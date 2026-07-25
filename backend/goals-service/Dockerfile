FROM eclipse-temurin:21-jre-alpine
COPY target/goals-service-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app.jar"]
