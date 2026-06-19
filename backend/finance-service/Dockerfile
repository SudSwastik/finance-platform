FROM eclipse-temurin:21-jre-alpine
COPY target/finance-service-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app.jar"]
