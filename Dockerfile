# dockerfiles/Dockerfile.app

# Etapa de build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace

# Copia configuração do Gradle
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle gradle
COPY src src

RUN chmod +x gradlew
RUN ./gradlew clean bootJar --no-daemon

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar app.jar

EXPOSE 8080
ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
