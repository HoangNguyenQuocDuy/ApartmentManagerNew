#FROM maven:3.9.9-eclipse-temurin-21 AS builder
#WORKDIR /app
#COPY . .
#RUN mvn -pl room-service -am clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
#COPY --from=builder /app/room-service/target/*.jar app.jar
COPY room-service/target/room-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]