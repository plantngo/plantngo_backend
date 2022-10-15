FROM openjdk:17

WORKDIR /opt/app

# RUN [ "apt update && apt upgrade && apt add curl"]

COPY . .

RUN ./mvnw install -DskipTests

CMD ["./mvnw", "spring-boot:run"]
