 # Use an official OpenJDK runtime as the base image
 FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/task-manager-0.0.1-SNAPSHOT.jar app.jar

# Expose the application's port (modify if your app uses a different port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

