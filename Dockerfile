FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY . .
RUN chmod +x ./mvnw ./run.sh
EXPOSE 8080
ENTRYPOINT ["./run.sh"]
