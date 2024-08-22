FROM openjdk:17-jdk-slim-buster
WORKDIR /app
RUN mkdir templates
COPY LogGenerator.jar .
COPY templates/* templates/

#COPY app/build/libs/app.jar build/

WORKDIR /app
ENTRYPOINT ["java",  "-jar",  "LogGenerator.jar"]