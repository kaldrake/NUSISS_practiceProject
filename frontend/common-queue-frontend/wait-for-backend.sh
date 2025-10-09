#!/bin/sh

# Wait for backend container to be ready on port 8080
echo "Waiting for backend to be ready..."
while ! nc -z localhost 8080; do
  echo "Backend not ready yet, sleeping 2s..."
  sleep 2
done

echo "Backend is up, starting Nginx..."
exec nginx -g "daemon off;"
