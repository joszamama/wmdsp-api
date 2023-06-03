FROM maven:3.8.3-openjdk-17 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /app/src/
RUN mvn package

FROM openjdk:17-jdk-alpine3.13

WORKDIR /app

COPY --from=builder /app/target/W-MDSP-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]