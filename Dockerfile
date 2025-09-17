# Multi-stage build para otimizar o tamanho da imagem
FROM maven:3.9.6-openjdk-17-slim AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar apenas pom.xml primeiro (para cache do Maven)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Compilar aplicação
RUN mvn clean package -DskipTests

# Estágio de produção
FROM openjdk:17-jre-slim

# Instalar curl para health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Criar usuário não-root para segurança
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR compilado do estágio anterior
COPY --from=build /app/target/people-flow-api-*.jar app.jar

# Mudar propriedade do arquivo para o usuário não-root
RUN chown appuser:appgroup app.jar

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
