FROM gradle
VOLUME /tmp
ADD . /home/gradle/app
WORKDIR /home/gradle/app
USER root
run apt-get update && apt-get install -y sudo build-essential
run curl -sL https://deb.nodesource.com/setup_10.x | sudo -E bash - && apt-get install -y nodejs
run cd src/main/resources/ && npm install && npm run build && cd ../../..
RUN gradle jar --info && gradle bootRepackage
RUN mv ./build/libs/gs-spring-boot-0.1.0.jar app.jar
ENV JAVA_OPTS=""
CMD [ "bash", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${CONFIG:-docker} -Djava.security.egd=file:/dev/urandom -jar app.jar" ]
