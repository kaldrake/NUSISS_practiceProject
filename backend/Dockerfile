# ---- Build stage ----
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# if your settings.xml is in backend/, keep this
COPY ./tempSettings.xml /app/settings.xml

COPY pom.xml .
RUN mvn -q -B -DskipTests -s /app/settings.xml dependency:go-offline
COPY src ./src

# build
RUN mvn -q -B -DskipTests -s /app/settings.xml clean package

# 👇 diagnostics: show what was built
RUN echo "=== TARGET DIR ===" && ls -lah target || true \
 && echo "=== SEARCHING FOR JARS UNDER /app ===" \
 && find /app -maxdepth 3 -type f \( -name "*.jar" -o -name "*.war" \) -printf "%p\n"

# ---- Runtime stage ----
FROM eclipse-temurin:21-jre
RUN useradd -ms /bin/bash spring
WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=75 -XX:+ExitOnOutOfMemoryError"
ENV TZ=Asia/Singapore

# copy ANY jar that was built
COPY --from=build /app/target/*.jar /app/app.jar
# (if you discover it’s a WAR, we’ll switch strategy—see below)

EXPOSE 8080
USER spring
ENTRYPOINT ["sh","-c","java $JAVA_TOOL_OPTIONS -jar /app/app.jar"]
