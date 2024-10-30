#!/usr/bin/env sh

echo "Building the application..."
./mvnw clean package -DskipTests

echo "Running the application..."
java -jar target/*.jar

exit 1
