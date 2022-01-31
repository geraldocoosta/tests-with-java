FROM openjdk:11-jdk-slim
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "$JAVA_OPTS", "-Dserver.port=$PORT","-jar","/app.jar"]