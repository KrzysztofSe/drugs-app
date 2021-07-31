FROM adoptopenjdk/openjdk11:alpine-jre
WORKDIR /opt/app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]