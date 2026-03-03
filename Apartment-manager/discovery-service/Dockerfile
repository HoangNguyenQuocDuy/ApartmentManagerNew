#FROM maven:3.9.9-eclipse-temurin-21 AS builder
#WORKDIR /app
#COPY . .
#RUN mvn -pl discovery-service -am clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
#COPY --from=builder /app/discovery-service/target/*.jar app.jar
COPY discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
