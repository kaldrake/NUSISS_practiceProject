FROM mysql:8.0

# Copy init.sql into MySQL's init folder
COPY init/init.sql /docker-entrypoint-initdb.d/

# Explicitly exposing port 3306
EXPOSE 3306