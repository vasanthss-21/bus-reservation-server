# Use an official Java 17 image
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies (cached)
RUN ./mvnw dependency:go-offline

# Copy the rest of the project
COPY src src

# Package the Spring Boot app
RUN ./mvnw clean package -DskipTests

# Expose the app port (change if needed)
EXPOSE 8081

# Run the app
CMD ["java", "-jar", "target/*.jar"]
