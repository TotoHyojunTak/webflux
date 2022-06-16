FROM openjdk:17-ea-11-jdk-slim
MAINTAINER toto <thj0309@hotmail.com>
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} webflux.jar
ENTRYPOINT ["java","-jar","/webflux.jar"]
