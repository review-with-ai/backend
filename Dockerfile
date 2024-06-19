FROM openjdk:17
ARG JAR_FILE=app-api/build/libs/app-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENV USE_PROFILE dev
ENTRYPOINT ["java", "-Dspring.profiles.active=${USE_PROFILE}", "-jar", "/app.jar"]

# docker buildx build --platform linux/amd64 --tag aireview:latest .
#docker tag aireview:latest i960107/ai-review:latest
#docker push i960107/aireview:latest

#docker-compose pull
#docker-compose up -d

