FROM openjdk:17-alpine

WORKDIR /opt/app

RUN apk update && apk upgrade && apk add curl

COPY . .

RUN ./mvnw install 

CMD ["./mvnw", "spring-boot:run"]
