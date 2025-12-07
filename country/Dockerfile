FROM eclipse-temurin:21-jdk

WORKDIR /myApp

COPY country/build/libs/app.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]


