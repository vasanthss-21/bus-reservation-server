# Use Java 21 JDK to match your project
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make mvnw executable
RUN chmod +x mvnw

# Download dependencies (cached)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the Spring Boot JAR
RUN ./mvnw clean package -DskipTests

# Expose port 8081
EXPOSE 8081

# Run the JAR
# Replace with exact JAR name from target folder
CMD ["java", "-jar", "target/Bus_Reservation_System-0.0.1-SNAPSHOT.jar"]
