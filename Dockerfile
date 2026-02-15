# Stage 1: Build the JAR
FROM maven:3.9.10-eclipse-temurin-21 AS build
WORKDIR /app

# Copy only the pom first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the JAR
# Java 21 JRE – supports Apple Silicon (ARM64) and AWS x86
FROM eclipse-temurin:21-jre

# Set working directory inside the container
WORKDIR /app

# Install mysql client for health check
RUN apt-get update && apt-get install -y mysql-client && rm -rf /var/lib/apt/lists/*

# Copy your Spring Boot fat jar into the container
COPY --from=build /app/target/*.jar app.jar
COPY wait-for-mysql.sh /wait-for-mysql.sh
RUN chmod +x /wait-for-mysql.sh

EXPOSE 8080

# Run the application
ENTRYPOINT ["/wait-for-mysql.sh", "db", "java", "-jar", "app.jar"]

