FROM gradle:jdk17-alpine AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

COPY src src

RUN gradle bootJar --no-daemon --info

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

RUN mkdir -p /app/uploads/videos

COPY --from=builder /app/target/video-inventory.jar app.jar

ENV SPRING_PROFILES_ACTIVE=default
ENV TZ=Asia/Dhaka

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]