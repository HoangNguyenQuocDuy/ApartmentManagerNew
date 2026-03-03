#FROM maven:3.9.9-eclipse-temurin-21 AS builder
#WORKDIR /app
#COPY . .
#RUN mvn -pl payment-service -am clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
#COPY --from=builder /app/payment-service/target/*.jar app.jar
COPY payment-service/target/payment-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]