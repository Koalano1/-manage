# Base image được sử dụng để build image
FROM amazoncorretto:21

# Thông tin tác giả
LABEL authors="buihien"

# Set working directory trong container
WORKDIR /app

# Copy file JAR được build từ ứng dụng Spring Boot vào working directory trong container
COPY target/User-0.0.1-SNAPSHOT.jar User-0.0.1-SNAPSHOT.jar


# Expose port của ứng dụng
EXPOSE 8080

# Chỉ định command để chạy ứng dụng khi container khởi chạy
CMD ["java", "-jar", "User-0.0.1-SNAPSHOT.jar"]


# Optional: Add custom configuration files or scripts
# COPY ./my-custom.cnf /etc/mysql/conf.d/my-custom.cnf

# Optional: Add scripts to run on container startup
# COPY ./custom-script.sql /docker-entrypoint-initdb.d/custom-script.sql

# Set environment variables (if not using docker-compose to do so)
ENV MYSQL_ROOT_PASSWORD tranglhoc
ENV MYSQL_DATABASE tranglhoc
ENV MYSQL_USER tranglhoc
ENV MYSQL_PASSWORD tranglhoc

# Expose port 3306 for MySQL connections (not necessary since docker-compose handles this)

