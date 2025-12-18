FROM maven:3.9.6-eclipse-temurin-21 AS builder

WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21.0.1_12-jre-jammy
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN mkdir -p /config-repo
# COPY --from=builder /app/src/main/resources/config-repo/* /config-repo/
RUN if [ -d "/app/src/main/resources/config-repo" ]; then \
    find /app/src/main/resources/config-repo -type f -exec cp {} /config-repo/ \;; \
else \
    echo "No config files to copy"; \
fi

RUN groupadd -r spring && useradd -r -g spring spring
RUN chown -R spring:spring /app
USER spring

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
