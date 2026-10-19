# syntax=docker/dockerfile:1
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY . .
# All services share this build layer; dependencies persist between builds.
# Database integration tests run separately with Docker available.
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp -DskipTests package

FROM build AS test
RUN --mount=type=cache,target=/root/.m2 mvn -B -ntp test

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
ARG SERVICE=dashboard-bff
COPY --from=build /workspace/${SERVICE}/target/${SERVICE}-*.jar /app/app.jar
USER 10001:10001
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
