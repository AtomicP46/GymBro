# Stage 1: build
FROM maven:3.9.0-eclipse-temurin-17-alpine AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src src
RUN mvn clean package -DskipTests -B

# Stage 2: runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia qualquer JAR gerado para app.jar
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]