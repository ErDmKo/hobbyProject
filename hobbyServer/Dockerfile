FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD ./build/libs/gs-spring-boot-0.1.0.jar app.jar
ENV JAVA_OPTS=""
CMD [ "sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=docker -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
