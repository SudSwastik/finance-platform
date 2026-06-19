FROM eclipse-temurin:21-jre-alpine
COPY target/identity-service-0.1.0-SNAPSHOT.jar app.jar
EXPOSE 8079
ENTRYPOINT ["java", "-jar", "/app.jar"]
