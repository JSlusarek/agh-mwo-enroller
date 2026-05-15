FROM maven:3.9-amazoncorretto-17 AS builder
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

FROM amazoncorretto:17
WORKDIR /app
COPY --from=builder /app/target/enroller-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]