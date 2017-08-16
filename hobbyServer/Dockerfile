FROM gradle
VOLUME /tmp
ADD . /home/gradle/app
WORKDIR /home/gradle/app
USER root
RUN gradle jar --info && gradle bootRepackage
RUN mv ./build/libs/gs-spring-boot-0.1.0.jar app.jar
ENV JAVA_OPTS=""
CMD [ "bash", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${CONFIG:-docker} -Djava.security.egd=file:/dev/urandom -jar app.jar" ]
