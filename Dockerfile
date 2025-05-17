# Start with a Java 17 JDK base image
FROM eclipse-temurin:17-jdk-jammy

# Set working directory inside the container
WORKDIR /app

# Copy your built JAR into the container
COPY target/worktree-hrms-admin-0.0.1-SNAPSHOT.jar app.jar

# Expose application port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
