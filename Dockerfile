FROM openjdk:17

WORKDIR /opt/app

COPY . .

RUN ./mvnw install -DskipTests

CMD ["./mvnw", "spring-boot:run"]
