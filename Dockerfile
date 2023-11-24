FROM eclipse-temurin:17
COPY ./build/libs/web-clinic-app-1.0.jar ./app.jar
ENTRYPOINT ["java","-jar","/app.jar"]