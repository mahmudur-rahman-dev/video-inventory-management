# Stage 1: Build the application
FROM gradle:jdk17-alpine AS builder

# Set working directory
WORKDIR /app

# Copy gradle files first to cache dependencies
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle

# Copy source code
COPY src src

# Build application
RUN gradle bootJar --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create volume directory for video uploads
RUN mkdir -p /app/uploads/videos

# Copy jar from builder stage
COPY --from=builder /app/target/video-inventory.jar app.jar

# Environment variables
ENV SPRING_PROFILES_ACTIVE=default
ENV TZ=Asia/Dhaka

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]