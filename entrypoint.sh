#!/bin/bash
set -e

# ----------------------------
# Start MariaDB (Alpine version)
# ----------------------------
echo "Starting MariaDB..."

# Initialize and start MariaDB
if [ ! -d "/run/mysqld" ]; then
    mkdir -p /run/mysqld
    chown -R mysql:mysql /run/mysqld
fi

# Start MariaDB daemon
mysqld_safe --user=mysql --datadir=/var/lib/mysql --pid-file=/run/mysqld/mysqld.pid &

# Wait for MariaDB to be ready
echo "Waiting for MariaDB to start..."
for i in {30..0}; do
    if mysqladmin ping --silent; then
        break
    fi
    echo 'MariaDB init process in progress...'
    sleep 2
done

if [ "$i" = 0 ]; then
    echo >&2 'MariaDB init process failed.'
    exit 1
fi

echo "MariaDB is up - configuring database..."

# Create database and user
mysql -u root <<-EOSQL
    CREATE DATABASE IF NOT EXISTS common_queue;
    CREATE USER IF NOT EXISTS 'projectuser'@'localhost' IDENTIFIED BY 'projectpassword';
    CREATE USER IF NOT EXISTS 'projectuser'@'%' IDENTIFIED BY 'projectpassword';
    GRANT ALL PRIVILEGES ON common_queue.* TO 'projectuser'@'localhost';
    GRANT ALL PRIVILEGES ON common_queue.* TO 'projectuser'@'%';
    FLUSH PRIVILEGES;
EOSQL

# Import schema if exists
if [ -f /docker-entrypoint-initdb.d/dbscript.sql ]; then
    echo "Importing database schema..."
    mysql -u root common_queue < /docker-entrypoint-initdb.d/dbscript.sql
else
    echo "No database script found at /docker-entrypoint-initdb.d/dbscript.sql"
fi

# ----------------------------
# Start Nginx
# ----------------------------
echo "Starting Nginx..."
nginx -t && nginx

# ----------------------------
# Start Spring Boot backend
# ----------------------------
echo "Starting Spring Boot application..."
exec java -jar /app/app.jar