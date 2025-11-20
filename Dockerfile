
# Etapa de build
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace/app

# Instala Maven
RUN apk add --no-cache maven

# Copia arquivos do projeto
COPY pom.xml .
COPY src ./src

# Compila o projeto (sem rodar testes)
RUN mvn clean package -DskipTests

# Etapa de runtime
FROM eclipse-temurin:17-jre-alpine

# Diretório da aplicação
WORKDIR /app

# Copia o .jar gerado do build
# Isso pega qualquer JAR gerado em target 
COPY --from=build /workspace/app/target/*.jar app.jar

# Porta padrão Spring Boot (ajusta se sua app usar outra)
EXPOSE 8080

# Usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Comando de entrada genérico, independe do nome do pacote/classe
ENTRYPOINT ["java","-jar","app.jar"]
