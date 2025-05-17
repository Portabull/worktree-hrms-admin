# Use official Ubuntu 22.04 as the base image
FROM ubuntu:22.04

# Disable interactive prompts during package installation
ENV DEBIAN_FRONTEND=noninteractive

# Update and upgrade system packages to fix vulnerabilities
RUN apt update && \
    apt upgrade -y && \
    apt autoremove -y && \
    apt clean

# Install OpenJDK and essential tools
RUN apt install -y openjdk-17-jdk curl wget && \
    rm -rf /var/lib/apt/lists/*

# Create an app directory
RUN mkdir -p /app

# Set the working directory
WORKDIR /app

# Copy your Spring Boot JAR into the image
COPY target/worktree-hrms-admin-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on (optional)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
