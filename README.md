# People Flow API

API REST para gerenciamento de pessoas usando arquitetura hexagonal com autenticação Keycloak.

## Tecnologias

- Java 17
- Spring Boot 3.2.0
- Spring Security + OAuth2 JWT
- Keycloak (autenticação)
- Spring Data JPA
- PostgreSQL 16 (desenvolvimento e produção)
- Flyway (migrações de banco)
- Docker & Docker Compose

## Ambientes

### Desenvolvimento (dev)
- PostgreSQL 16 local (porta 5432)
- Database: PEOPLE_FLOW_RH_DEV
- Keycloak (porta 8082)
- App local (porta 8080)
- Logging detalhado
- Actuator com todos os endpoints
- Compressão habilitada

### Produção (prod)
- PostgreSQL 16 local (porta 5433)
- Database: PEOPLE_FLOW_RH
- Keycloak (porta 8083)
- App local (porta 8081)
- Logging otimizado
- Actuator limitado
- Configurações de performance
- Compressão habilitada

## Como executar

### Desenvolvimento local
```bash
# Com Maven (requer PostgreSQL e Keycloak rodando localmente)
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
- Keycloak Admin: http://localhost:8082 (admin/admin123)
- PostgreSQL: localhost:5432 (PEOPLE_FLOW_RH_DEV)

### Produção
- API: http://localhost:8081/people
- Actuator: http://localhost:8081/actuator/health
- Keycloak Admin: http://localhost:8083 (admin/admin123)
- PostgreSQL: localhost:5433 (PEOPLE_FLOW_RH)

## Configuração do Keycloak

### 1. Acessar o Admin Console
- URL: http://localhost:8082 (dev) ou http://localhost:8083 (prod)
- Usuário: admin
- Senha: admin123

### 2. Criar Realm
- Nome: `people-flow-realm`

### 3. Criar Client
- Client ID: `people-flow-api`
- Client Protocol: `openid-connect`
- Access Type: `confidential`
- Valid Redirect URIs: `http://localhost:8080/*`
- Web Origins: `http://localhost:8080`

### 4. Criar Usuários e Roles
- Criar usuários de teste
- Criar roles: `USER`, `ADMIN`
- Atribuir roles aos usuários

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

## Testando a API

### 1. Obter Token do Keycloak
```bash
curl -X POST http://localhost:8082/realms/people-flow-realm/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=seu-usuario" \
  -d "password=sua-senha" \
  -d "grant_type=password" \
  -d "client_id=people-flow-api" \
  -d "client_secret=seu-client-secret"
```

### 2. Usar Token na API
```bash
curl -X GET http://localhost:8080/people \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

## Versões dos Componentes

- **Java**: 17
- **Spring Boot**: 3.2.0
- **PostgreSQL**: 16-alpine
- **Keycloak**: 23.0
- **Maven**: 3.9.6
