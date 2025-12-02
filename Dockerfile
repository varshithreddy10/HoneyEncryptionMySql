
FROM eclipse-temurin:17-jdk

# Directory inside container
WORKDIR /app

# Copy built jar into container
COPY target/*.jar app.jar

# Expose your Spring Boot port
EXPOSE 9100

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]
