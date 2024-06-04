FROM openjdk:17

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 8080

ENV JAR_PATH=/app/build/libs

ENTRYPOINT ["java","-jar","-Duser.timezone=Asia/Seoul","/app.jar"]