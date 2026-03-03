# ===== BUILD STAGE =====
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

ARG SERVICE_NAME

# Copy toàn bộ project (vì là multi-module)
COPY . .

# Build đúng service + các module phụ thuộc (ví dụ common)
RUN mvn -B -pl ${SERVICE_NAME} -am clean package -DskipTests


# ===== RUN STAGE =====
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

ARG SERVICE_NAME

COPY --from=build /app/${SERVICE_NAME}/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]