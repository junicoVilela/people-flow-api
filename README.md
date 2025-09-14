# People Flow API

API REST para gerenciamento de pessoas usando arquitetura hexagonal.

## Tecnologias

- Java 21
- Spring Boot 3.2.0
- Spring Security + OAuth2 JWT
- Spring Data JPA
- PostgreSQL 16 (desenvolvimento e produção)
- Flyway (migrações de banco)
- Docker & Docker Compose

## Ambientes

### Desenvolvimento (dev)
- PostgreSQL 16 local (porta 5432)
- Database: PEOPLE_FLOW_RH_DEV
- App local (porta 8080)
- Logging detalhado
- Actuator com todos os endpoints
- Compressão habilitada

### Produção (prod)
- PostgreSQL 16 local (porta 5433)
- Database: PEOPLE_FLOW_RH
- App local (porta 8081)
- Logging otimizado
- Actuator limitado
- Configurações de performance
- Compressão habilitada

## Como executar

### Desenvolvimento local
```bash
# Com Maven (requer PostgreSQL rodando localmente)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Com Docker (desenvolvimento)
docker-compose -f docker-compose.dev.yml up --build
```

### Produção
```bash
# Com Docker (produção)
docker-compose -f docker-compose.prod.yml up --build
```

## Acessos

### Desenvolvimento
- API: http://localhost:8080/people
- Actuator: http://localhost:8080/actuator
- PostgreSQL: localhost:5432 (PEOPLE_FLOW_RH_DEV)

### Produção
- API: http://localhost:8081/people
- Actuator: http://localhost:8081/actuator/health
- PostgreSQL: localhost:5433 (PEOPLE_FLOW_RH)

## Configurações

### Variáveis de ambiente
- `DATABASE_URL`: URL do banco PostgreSQL
- `DATABASE_USERNAME`: Usuário do banco
- `DATABASE_PASSWORD`: Senha do banco
- `SERVER_PORT`: Porta da aplicação
- `KEYCLOAK_ISSUER_URI`: URI do Keycloak
- `KEYCLOAK_CLIENT_SECRET`: Secret do client Keycloak

### Profiles Spring
- `dev`: Ambiente de desenvolvimento
- `prod`: Ambiente de produção

## Banco de dados

### Desenvolvimento
- Host: localhost (local) / postgres (Docker)
- Porta: 5432
- Database: PEOPLE_FLOW_RH_DEV
- Usuário: peopleflow
- Senha: peopleflow123
- Versão: PostgreSQL 16

### Produção
- Host: localhost (local) / postgres (Docker)
- Porta: 5433 (local) / 5432 (Docker)
- Database: PEOPLE_FLOW_RH
- Usuário: peopleflow
- Senha: peopleflow123
- Versão: PostgreSQL 16

## Comandos Docker

### Desenvolvimento
```bash
# Subir serviços
docker-compose -f docker-compose.dev.yml up -d

# Ver logs
docker-compose -f docker-compose.dev.yml logs -f

# Parar serviços
docker-compose -f docker-compose.dev.yml down

# Parar e remover volumes
docker-compose -f docker-compose.dev.yml down -v
```

### Produção
```bash
# Subir serviços
docker-compose -f docker-compose.prod.yml up -d

# Ver logs
docker-compose -f docker-compose.prod.yml logs -f

# Parar serviços
docker-compose -f docker-compose.prod.yml down

# Parar e remover volumes
docker-compose -f docker-compose.prod.yml down -v
```
