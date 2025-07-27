# Vaihe 1: buildaa JAR
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /logger
COPY . .
RUN mvn clean package -DskipTests

# Vaihe 2: aja valmis JAR
FROM eclipse-temurin:21-jre
WORKDIR /logger
COPY --from=build /logger/target/logger-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 80
ENTRYPOINT ["java", "-jar", "app.jar"]
