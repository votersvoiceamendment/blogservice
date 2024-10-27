# Use an official OpenJDK runtime as a parent image
#FROM openjdk:17-jdk-alpine
#Need because on your mac
FROM maven:3.8.4-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Copy the entire project and build the application
COPY . .
RUN mvn clean package -DskipTests

# Expose the port that the service will run on
EXPOSE 8080

# Define environment variables to be passed to the container
ENV DATABASE_URL=${DATABASE_URL}
ENV DATABASE_USERNAME=${DATABASE_USERNAME}
ENV DATABASE_PASSWORD=${DATABASE_PASSWORD}
ENV AUTH_SECRET=${AUTH_SECRET}
ENV ALLOWED_ORIGINS=${ALLOWED_ORIGINS}

# Run the Spring Boot application
CMD ["java", "-jar", "target/blogservice-0.0.1-SNAPSHOT.jar"]
