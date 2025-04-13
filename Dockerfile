FROM openjdk:17-jdk-slim

# Define a build argument for the JAR file location
ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container
COPY ${JAR_FILE} app.jar

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "/app.jar"]