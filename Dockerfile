# Multi-stage build para otimizar o tamanho da imagem
FROM maven:3.9.6-openjdk-17-slim AS build

# Definir diretório de trabalho
WORKDIR /app

# Copiar apenas pom.xml primeiro (para cache do Maven)
COPY pom.xml .
COPY people-flow-common/pom.xml ./people-flow-common/
COPY people-flow-pessoascontratos/pom.xml ./people-flow-pessoascontratos/
COPY people-flow-pessoascontratos/pessoascontratos-core/pom.xml ./people-flow-pessoascontratos/pessoascontratos-core/
COPY people-flow-pessoascontratos/pessoascontratos-inbound/pom.xml ./people-flow-pessoascontratos/pessoascontratos-inbound/
COPY people-flow-pessoascontratos/pessoascontratos-outbound/pom.xml ./people-flow-pessoascontratos/pessoascontratos-outbound/
COPY people-flow-organizacao/pom.xml ./people-flow-organizacao/
COPY people-flow-organizacao/organizacao-core/pom.xml ./people-flow-organizacao/organizacao-core/
COPY people-flow-organizacao/organizacao-inbound/pom.xml ./people-flow-organizacao/organizacao-inbound/
COPY people-flow-organizacao/organizacao-outbound/pom.xml ./people-flow-organizacao/organizacao-outbound/
COPY people-flow-application/pom.xml ./people-flow-application/
RUN mvn dependency:go-offline -B

# Copiar código fonte de todos os módulos
COPY people-flow-common ./people-flow-common
COPY people-flow-pessoascontratos ./people-flow-pessoascontratos
COPY people-flow-organizacao ./people-flow-organizacao
COPY people-flow-application ./people-flow-application

# Compilar aplicação (módulo application)
RUN mvn clean package -DskipTests -pl people-flow-application -am

# Estágio de produção
FROM openjdk:17-jre-slim

# Instalar curl para health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Criar usuário não-root para segurança
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Definir diretório de trabalho
WORKDIR /app

# Copiar o JAR compilado do estágio anterior
COPY --from=build /app/people-flow-application/target/people-flow-application-*.jar app.jar

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
