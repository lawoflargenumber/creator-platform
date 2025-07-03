FROM openjdk:15-jdk-alpine
RUN apk add --no-cache curl
COPY target/*SNAPSHOT.jar app.jar
EXPOSE 8082
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
CMD ["mvn", "spring-boot:run"]
