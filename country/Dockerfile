FROM eclipse-temurin:21-jdk

WORKDIR /myApp

COPY build/libs/* app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]


