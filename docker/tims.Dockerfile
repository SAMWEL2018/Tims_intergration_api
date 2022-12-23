FROM eclipse-temurin:17-jre-alpine

ENV APP intergration-1.0.0.jar

WORKDIR /

ADD $APP $APP

EXPOSE 5000

ENTRYPOINT ["java","-jar","intergration-1.0.0.jar","--server"]
