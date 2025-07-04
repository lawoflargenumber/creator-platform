FROM openjdk:8u212-jdk-alpine
RUN apk add --no-cache curl
COPY target/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
CMD ["mvn", "spring-boot:run"]
