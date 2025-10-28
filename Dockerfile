# Stage 1: build
FROM gradle:9-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

# Stage 2: run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/app.jar app.jar
ENTRYPOINT ["java","-jar","app.jar","--spring.profiles.active=${SPRING_PROFILES_ACTIVE}"]
