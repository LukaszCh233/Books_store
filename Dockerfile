#BUILD
FROM maven:3.8.5-openjdk-17-slim AS build

WORKDIR /app

COPY ./pom.xml .
COPY ./src ./src

RUN mvn clean package -DskipTests

#RUN
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY --from=build /app/target/Book_Store-0.0.1-SNAPSHOT.jar Book_Store-0.0.1-SNAPSHOT.jar

EXPOSE 8085

ENTRYPOINT [ "java", "-jar", "/app/Book_Store-0.0.1-SNAPSHOT.jar" ]