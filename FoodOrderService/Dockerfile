# ============================
# Stage 1: Build the application
# ============================
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the code
COPY src ./src

# Build the jar
RUN mvn clean package -DskipTests

# ============================
# Stage 2: Run the application
# ============================
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/FoodOrderService-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8083

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
