#!/usr/bin/env sh

echo "Testing the application..."
./mvnw clean test -P mutation-tests
