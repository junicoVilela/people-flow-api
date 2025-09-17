# Multi-stage build para otimizar o tamanho da imagem
FROM eclipse-temurin:21-jdk-alpine AS build

# Instalar Maven
RUN apk add --no-cache maven

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
FROM eclipse-temurin:21-jre-alpine

# Instalar curl para health check
RUN apk add --no-cache curl

# Criar usuário não-root para segurança
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR compilado do estágio anterior
COPY --from=build /app/target/people-flow-api*.jar app.jar

# Mudar para usuário não-root
USER appuser

# Expor porta
EXPOSE 8080

# Variáveis de ambiente JVM otimizadas para containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
