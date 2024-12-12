FROM ubuntu:latest
LABEL authors="quocduy"
# Sử dụng OpenJDK 11 làm base image
FROM openjdk:11-jdk-slim as builder

# Đặt thư mục làm việc
WORKDIR /app

# Sao chép file JAR vào trong container
COPY target/room-service.jar /app/room-service.jar

# Cung cấp lệnh chạy ứng dụng khi container khởi động
ENTRYPOINT ["java", "-jar", "room-service.jar"]

# Expose port 9002 để có thể truy cập ứng dụng từ bên ngoài
EXPOSE 9002
