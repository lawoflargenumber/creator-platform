FROM adoptopenjdk/maven-openjdk11:latest
WORKDIR /usr/src
COPY . .
EXPOSE 8082
ENV SPRING_PROFILES_ACTIVE=docker
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
CMD ["mvn", "spring-boot:run"]
