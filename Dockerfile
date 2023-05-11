# Use a Java runtime as a base image
FROM openjdk:17-oracle


# Set the working directory to /app
WORKDIR /app

# Copy the jar file into the container at /app
COPY ./target/todo-app-0.0.1-SNAPSHOT.jar /app/todo-app-0.0.1-SNAPSHOT.jar

# Expose port 8080 for the Spring Boot app to listen on
EXPOSE 8080

# Set the entrypoint to run the Spring Boot app when the container starts
ENTRYPOINT ["java", "-jar", "/app/todo-app-0.0.1-SNAPSHOT.jar"]