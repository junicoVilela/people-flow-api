# People Flow API

API para gestão de colaboradores e RH desenvolvida com Spring Boot seguindo os princípios de **Arquitetura Hexagonal** e **Domain-Driven Design (DDD)**.

## 📋 Índice

- [Arquitetura](#-arquitetura)
  - [Visão Geral](#visão-geral)
  - [Arquitetura Hexagonal (Ports & Adapters)](#arquitetura-hexagonal-ports--adapters)
  - [Estrutura Multi-Módulo](#estrutura-multi-módulo)
  - [Fluxo de Dados](#fluxo-de-dados)
- [Guia de Desenvolvimento](#-guia-de-desenvolvimento)
  - [Passo a Passo: Adicionar Nova Funcionalidade](#passo-a-passo-adicionar-nova-funcionalidade)
  - [Exemplo Prático: Criar Endpoint](#exemplo-prático-criar-endpoint)
  - [Boas Práticas](#boas-práticas)
- [Como Executar](#-como-executar)
- [Build e Deploy](#-build-e-deploy)
- [Tecnologias](#-tecnologias)

---

## 🏗️ Arquitetura

### Visão Geral

Este projeto utiliza uma **estrutura multi-módulo Maven** combinada com **Arquitetura Hexagonal**, organizando o código por **domínios** e **camadas**:

```
people-flow-api/ (pom parent)
├── people-flow-common/                    # Código compartilhado
├── people-flow-pessoascontratos/         # Domínio: Pessoas e Contratos
│   ├── pessoascontratos-core/            # Regras de negócio (hexágono)
│   ├── pessoascontratos-inbound/         # Adaptadores primários
│   └── pessoascontratos-outbound/        # Adaptadores secundários
├── people-flow-organizacao/              # Domínio: Organização
│   └── organizacao-outbound/            # JPA Entities (Cliente, Empresa, etc.)
└── people-flow-application/              # Aplicação Spring Boot
```

### Arquitetura Hexagonal (Ports & Adapters)

A **Arquitetura Hexagonal** (também conhecida como Ports & Adapters) separa a **lógica de negócio** (core) da **infraestrutura** (adaptadores), criando um "hexágono" onde:

- **Core (Hexágono Central)**: Contém as regras de negócio puras, sem dependências externas
- **Ports (Interfaces)**: Definem contratos de comunicação
  - **Ports In (Use Cases)**: O que o sistema pode fazer
  - **Ports Out (Repositories)**: O que o sistema precisa do mundo externo
- **Adapters (Adaptadores)**: Implementam os ports
  - **Inbound Adapters**: Recebem dados do mundo externo (HTTP, Events, CLI)
  - **Outbound Adapters**: Enviam dados para o mundo externo (Database, APIs, File System)

#### Diagrama Conceitual

```
                    ┌─────────────────────────────────┐
                    │   INBOUND ADAPTERS              │
                    │  (Controllers, Event Listeners) │
                    └────────────┬────────────────────┘
                                  │
                    ┌─────────────▼─────────────┐
                    │   PORTS IN (Use Cases)    │
                    │  ColaboradorUseCase       │
                    └─────────────┬─────────────┘
                                  │
        ┌─────────────────────────┼─────────────────────────┐
        │                         │                         │
        │         ┌───────────────▼───────────────┐        │
        │         │      CORE (DOMAIN)            │        │
        │         │  - Domain Entities            │        │
        │         │  - Business Logic             │        │
        │         │  - Domain Events              │        │
        │         │  - Value Objects              │        │
        │         └───────────────┬───────────────┘        │
        │                         │                         │
        └─────────────────────────┼─────────────────────────┘
                                  │
                    ┌─────────────▼─────────────┐
                    │   PORTS OUT (Repositories) │
                    │  ColaboradorRepositoryPort │
                    └─────────────┬─────────────┘
                                  │
                    ┌─────────────▼────────────────────┐
                    │   OUTBOUND ADAPTERS              │
                    │  (JPA Repositories, APIs)        │
                    └──────────────────────────────────┘
```

### Estrutura Multi-Módulo

Cada **domínio** (ex: `pessoascontratos`, `organizacao`) é um módulo Maven agregador com 3 sub-módulos:

#### 1. **Core** (`*-core`)
**Responsabilidade**: Lógica de negócio pura, sem dependências de infraestrutura

```
pessoascontratos-core/
└── src/main/java/com/peopleflow/pessoascontratos/core/
    ├── domain/              # Entidades de domínio (Rich Domain Model)
    │   ├── Colaborador.java
    │   └── events/          # Domain Events
    ├── ports/
    │   ├── in/              # Ports de entrada (Use Cases)
    │   │   └── ColaboradorUseCase.java
    │   └── out/             # Ports de saída (Repositories)
    │       └── ColaboradorRepositoryPort.java
    ├── usecase/             # Implementação dos Use Cases
    │   └── ColaboradorService.java
    ├── query/               # Objetos de consulta (CQRS)
    │   └── ColaboradorFilter.java
    └── valueobject/         # Value Objects imutáveis
        ├── Cpf.java
        ├── Email.java
        └── StatusColaborador.java
```

**Características**:
- ✅ Pode depender apenas de `common`
- ✅ Contém regras de negócio puras
- ✅ Não conhece HTTP, JPA, ou qualquer tecnologia específica
- ✅ Define interfaces (ports) que serão implementadas pelos adapters

#### 2. **Inbound** (`*-inbound`)
**Responsabilidade**: Adaptadores primários - recebem dados do mundo externo

```
pessoascontratos-inbound/
└── src/main/java/com/peopleflow/pessoascontratos/inbound/
    ├── web/                 # Adaptadores HTTP
    │   ├── ColaboradorController.java
    │   ├── dto/             # Data Transfer Objects
    │   │   ├── ColaboradorRequest.java
    │   │   └── ColaboradorResponse.java
    │   └── mapper/          # Mappers (DTO ↔ Domain)
    │       └── ColaboradorWebMapper.java
    └── events/              # Event Listeners
        ├── ColaboradorEventListener.java
        └── AuditoriaEventListener.java
```

**Características**:
- ✅ Depende de `core` (usa Use Cases)
- ✅ Depende de `common` (exceptions, security)
- ✅ Converte DTOs para entidades de domínio
- ✅ Não conhece implementação de repositório (usa apenas interfaces do core)

#### 3. **Outbound** (`*-outbound`)
**Responsabilidade**: Adaptadores secundários - persistem dados no mundo externo

```
pessoascontratos-outbound/
└── src/main/java/com/peopleflow/pessoascontratos/outbound/
    └── jpa/                 # Implementação JPA
        ├── entity/          # JPA Entities
        │   └── ColaboradorEntity.java
        ├── repository/      # Spring Data Repositories
        │   └── ColaboradorJpaRepository.java
        ├── adapter/         # Implementação dos Ports
        │   └── ColaboradorRepositoryAdapter.java
        ├── mapper/          # Mappers (Entity ↔ Domain)
        │   └── ColaboradorJpaMapper.java
        └── specification/   # Specifications (JPA Criteria)
            └── ColaboradorSpecification.java
```

**Características**:
- ✅ Depende de `core` (implementa Ports Out)
- ✅ Depende de `common` (AuditableEntity)
- ✅ Implementa interfaces definidas no core
- ✅ Conhece detalhes de infraestrutura (JPA, PostgreSQL)

#### 4. **Application** (`people-flow-application`)
**Responsabilidade**: Agrega todos os módulos e configura Spring Boot

```
people-flow-application/
├── src/main/java/com/peopleflow/
│   ├── PeopleFlowApplication.java    # Classe principal
│   └── common/config/
│       └── OpenApiConfig.java         # Configuração Swagger
└── src/main/resources/
    ├── application.yml
    ├── application-dev.yml
    ├── application-prod.yml
    └── db/migration/                  # Flyway migrations
```

### Fluxo de Dados

#### Exemplo: Criar Colaborador

```
1. HTTP Request
   ↓
2. ColaboradorController (inbound)
   - Recebe ColaboradorRequest (DTO)
   - Valida com Bean Validation
   ↓
3. ColaboradorWebMapper (inbound)
   - Converte DTO → Domain Entity
   ↓
4. ColaboradorService (core)
   - Valida regras de negócio
   - Cria entidade de domínio
   - Publica Domain Event
   ↓
5. ColaboradorRepositoryPort (core - interface)
   ↓
6. ColaboradorRepositoryAdapter (outbound)
   - Implementa o port
   ↓
7. ColaboradorJpaMapper (outbound)
   - Converte Domain Entity → JPA Entity
   ↓
8. ColaboradorJpaRepository (outbound)
   - Persiste no banco
   ↓
9. Resposta HTTP
   - Domain Entity → DTO
   - Retorna ColaboradorResponse
```

### Dependências entre Módulos

#### ✅ Dependências Permitidas

```
Common
  ↑
Core ──→ Common
  ↑         ↑
Inbound ───┘
  ↑
Application ──→ Inbound, Outbound, Core, Common
  ↑
Outbound ──→ Core, Common
```

#### ❌ Dependências Proibidas

- **Core** NÃO pode depender de **Inbound** ou **Outbound**
- **Inbound** NÃO pode depender de **Outbound**
- **Common** NÃO pode depender de módulos de domínio

### Estrutura Detalhada de Módulos

```
people-flow-api/ (pom parent)
├── people-flow-common/                    # Código compartilhado
│   └── src/main/java/com/peopleflow/common/
│       ├── audit/                         # Auditoria
│       ├── config/                        # Configurações (exceto OpenAPI)
│       ├── exception/                     # Exceções customizadas
│       └── security/                      # Segurança e autenticação
│
├── people-flow-pessoascontratos/          # Módulo agregador
│   ├── pessoascontratos-core/             # Regras de negócio
│   │   └── src/main/java/com/peopleflow/pessoascontratos/core/
│   │       ├── domain/                    # Entidades de domínio
│   │       ├── ports/                     # Interfaces (in/out)
│   │       ├── query/                     # Objetos de consulta
│   │       ├── usecase/                   # Casos de uso (services)
│   │       └── valueobject/              # Value Objects
│   │
│   ├── pessoascontratos-inbound/         # Adaptadores primários
│   │   └── src/main/java/com/peopleflow/pessoascontratos/inbound/
│   │       ├── events/                    # Event listeners
│   │       └── web/                       # Controllers, DTOs, Mappers
│   │
│   └── pessoascontratos-outbound/         # Adaptadores secundários
│       └── src/main/java/com/peopleflow/pessoascontratos/outbound/
│           └── jpa/                       # JPA entities, repositories, adapters
│
├── people-flow-organizacao/               # Módulo agregador
│   └── organizacao-outbound/              # Adaptadores secundários (JPA Entities)
│
└── people-flow-application/               # Aplicação Spring Boot
    ├── src/main/java/com/peopleflow/
    │   ├── PeopleFlowApplication.java     # Classe principal
    │   └── common/config/                 # OpenApiConfig (específico da app)
    └── src/main/resources/                # application.yml, migrations, etc.
```

### Benefícios da Estrutura Multi-Módulo

1. **Isolamento de Dependências**: Cada módulo declara apenas suas dependências necessárias
2. **Build Incremental**: Maven só recompila módulos que mudaram
3. **Reutilização**: Módulos podem ser usados em outros projetos
4. **Escalabilidade**: Fácil adicionar novos domínios como módulos separados
5. **Testes Independentes**: Cada módulo pode ser testado isoladamente
6. **Clareza Arquitetural**: A estrutura reflete a arquitetura hexagonal

**Notas Importantes**:
- A aplicação Spring Boot está em `people-flow-application`
- O `OpenApiConfig` foi movido para `people-flow-application` pois depende do SpringDoc
- Todos os módulos compartilham a mesma versão definida no pom parent
- O módulo `organizacao` atualmente contém apenas `organizacao-outbound` (JPA entities). Os módulos `core` e `inbound` serão criados quando necessário

---

## 👨‍💻 Guia de Desenvolvimento

### Passo a Passo: Adicionar Nova Funcionalidade

Vamos criar um exemplo completo: **adicionar endpoint para buscar colaboradores por CPF**.

#### **Passo 1: Definir o Port In (Use Case)**

**Arquivo**: `pessoascontratos-core/src/main/java/.../ports/in/ColaboradorUseCase.java`

```java
public interface ColaboradorUseCase {
    // ... métodos existentes ...
    
    /**
     * Busca colaborador por CPF
     * @param cpf CPF do colaborador
     * @return Colaborador encontrado
     * @throws ResourceNotFoundException se não encontrado
     */
    Colaborador buscarPorCpf(String cpf);
}
```

#### **Passo 2: Implementar o Use Case**

**Arquivo**: `pessoascontratos-core/src/main/java/.../usecase/ColaboradorService.java`

```java
@Service
public class ColaboradorService implements ColaboradorUseCase {
    
    @Override
    @Transactional(readOnly = true)
    public Colaborador buscarPorCpf(String cpf) {
        log.debug("Buscando colaborador por CPF: {}", cpf);
        
        // Valida CPF usando Value Object
        Cpf cpfValueObject = Cpf.of(cpf);
        
        // Busca no repositório (usa interface, não implementação)
        Colaborador colaborador = colaboradorRepository
            .buscarPorCpf(cpfValueObject.getValorNumerico())
            .orElseThrow(() -> {
                log.warn("Colaborador não encontrado: cpf={}", cpf);
                return new ResourceNotFoundException("Colaborador", "CPF", cpf);
            });
        
        // Valida permissões de acesso
        validarPermissaoDeAcesso(
            colaborador.getClienteId(), 
            colaborador.getEmpresaId()
        );
        
        return colaborador;
    }
}
```

#### **Passo 3: Adicionar método no Port Out (Repository)**

**Arquivo**: `pessoascontratos-core/src/main/java/.../ports/out/ColaboradorRepositoryPort.java`

```java
public interface ColaboradorRepositoryPort {
    // ... métodos existentes ...
    
    /**
     * Busca colaborador por CPF
     * @param cpf CPF sem formatação
     * @return Optional com colaborador se encontrado
     */
    Optional<Colaborador> buscarPorCpf(String cpf);
}
```

#### **Passo 4: Implementar no Outbound Adapter**

**Arquivo**: `pessoascontratos-outbound/src/main/java/.../adapter/ColaboradorRepositoryAdapter.java`

```java
@Component
public class ColaboradorRepositoryAdapter implements ColaboradorRepositoryPort {
    
    private final ColaboradorJpaRepository jpaRepository;
    private final ColaboradorJpaMapper mapper;
    
    @Override
    public Optional<Colaborador> buscarPorCpf(String cpf) {
        return jpaRepository.findByCpf(cpf)
            .map(mapper::toDomain);
    }
}
```

**Arquivo**: `pessoascontratos-outbound/src/main/java/.../repository/ColaboradorJpaRepository.java`

```java
public interface ColaboradorJpaRepository extends JpaRepository<ColaboradorEntity, Long> {
    // ... métodos existentes ...
    
    Optional<ColaboradorEntity> findByCpf(String cpf);
}
```

#### **Passo 5: Criar Endpoint no Inbound**

**Arquivo**: `pessoascontratos-inbound/src/main/java/.../web/ColaboradorController.java`

```java
@RestController
@RequestMapping("/api/v1/colaboradores")
public class ColaboradorController {
    
    // ... campos existentes ...
    
    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar colaborador por CPF")
    public ResponseEntity<ColaboradorResponse> buscarPorCpf(
            @PathVariable String cpf) {
        Colaborador colaborador = colaboradorUseCase.buscarPorCpf(cpf);
        ColaboradorResponse response = mapper.toResponse(colaborador);
        return ResponseEntity.ok(response);
    }
}
```

#### **Passo 6: Testar**

```bash
# Compilar
mvn clean compile

# Executar
mvn spring-boot:run -pl people-flow-application

# Testar endpoint
curl -X GET http://localhost:8080/api/v1/colaboradores/cpf/12345678900 \
  -H "Authorization: Bearer {token}"
```

### Exemplo Prático: Criar Endpoint

#### Checklist Completo

- [ ] **Core**: Adicionar método no `UseCase` (interface)
- [ ] **Core**: Implementar método no `Service`
- [ ] **Core**: Adicionar método no `RepositoryPort` (se necessário)
- [ ] **Outbound**: Implementar método no `RepositoryAdapter`
- [ ] **Outbound**: Adicionar método no `JpaRepository` (se necessário)
- [ ] **Inbound**: Criar/atualizar `DTO` (se necessário)
- [ ] **Inbound**: Adicionar método no `Controller`
- [ ] **Inbound**: Atualizar `Mapper` (se necessário)
- [ ] **Testar**: Compilar e executar

### Boas Práticas

#### 1. **Sempre comece pelo Core**
- Defina interfaces (Ports) primeiro
- Implemente regras de negócio
- Não pense em HTTP ou banco de dados ainda

#### 2. **Use Value Objects para validações**
```java
// ❌ Ruim
public Colaborador criar(String cpf, String email) {
    // validações espalhadas...
}

// ✅ Bom
public Colaborador criar(Cpf cpf, Email email) {
    // Cpf e Email já validam internamente
}
```

#### 3. **Domain Events para desacoplamento**
```java
// No Service (core)
eventPublisher.publishEvent(
    new ColaboradorCriado(colaborador.getId(), colaborador.getNome())
);

// No EventListener (inbound)
@EventListener
public void handle(ColaboradorCriado event) {
    // Enviar email, notificar, etc.
}
```

#### 4. **Mappers separados por camada**
- `ColaboradorWebMapper`: DTO ↔ Domain (inbound)
- `ColaboradorJpaMapper`: Domain ↔ Entity (outbound)
- Nunca mapeie DTO diretamente para Entity!

#### 5. **Use Specifications para queries complexas**
```java
// No Outbound
public Page<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pageable pageable) {
    Specification<ColaboradorEntity> spec = ColaboradorSpecification
        .comFiltros(filter);
    return jpaRepository.findAll(spec, pageable)
        .map(mapper::toDomain);
}
```

#### 6. **Validações em camadas**
- **DTO**: Validação de formato (Bean Validation)
- **Domain**: Validação de regras de negócio
- **Service**: Validação de permissões e contexto

---

## 🚀 Como Executar

### Pré-requisitos

- Java 17+
- Maven 3.6+
- PostgreSQL 16+ (ou Docker)
- Docker e Docker Compose (opcional)

### Executar Localmente

#### 1. Iniciar Banco de Dados

```bash
docker-compose -f docker-compose.dev.yml up -d
```

#### 2. Compilar Projeto

```bash
# Compilar todos os módulos
mvn clean compile

# Ou compilar apenas o módulo application (compila dependências automaticamente)
mvn clean compile -pl people-flow-application -am
```

#### 3. Executar Aplicação

```bash
# Opção 1: Maven
mvn spring-boot:run -pl people-flow-application

# Opção 2: JAR
mvn clean package -DskipTests
java -jar people-flow-application/target/people-flow-application-0.1.0-SNAPSHOT.jar
```

#### 4. Acessar API

- **API Base**: http://localhost:8080/api/v1
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Actuator Health**: http://localhost:8080/actuator/health
- **Actuator Info**: http://localhost:8080/actuator/info

### Executar com Docker

```bash
# Desenvolvimento (apenas banco)
docker-compose -f docker-compose.dev.yml up

# Produção (banco + aplicação)
docker-compose -f docker-compose.prod.yml up --build
```

---

## 📦 Build e Deploy

### Comandos Maven Úteis

```bash
# Compilar todos os módulos
mvn clean compile

# Compilar módulo específico (e dependências)
mvn clean compile -pl pessoascontratos-core -am

# Executar testes
mvn test

# Executar testes de um módulo
mvn test -pl pessoascontratos-core

# Gerar JAR executável
mvn clean package -DskipTests

# Instalar no repositório local
mvn clean install

# Ver dependências
mvn dependency:tree -pl pessoascontratos-core
```

### Build Docker

```bash
# Build da imagem
docker build -t people-flow-api:latest .

# Executar container
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/people_flow_rh \
  people-flow-api:latest
```

---

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar testes de um módulo
mvn test -pl pessoascontratos-core

# Executar com cobertura
mvn clean test jacoco:report
```

---

## 📚 Documentação Adicional

- **API Swagger**: http://localhost:8080/swagger-ui.html (após iniciar)
- **Arquitetura Hexagonal**: [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

---

## 🔧 Configuração

Os arquivos de configuração estão em `people-flow-application/src/main/resources/`:

- `application.yml` - Configurações gerais
- `application-dev.yml` - Configurações de desenvolvimento
- `application-prod.yml` - Configurações de produção

### Variáveis de Ambiente Importantes

```bash
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/people_flow_rh
DATABASE_USERNAME=peopleflow
DATABASE_PASSWORD=peopleflow

# OAuth2
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=https://auth.example.com

# Profile
SPRING_PROFILES_ACTIVE=dev
```

---

## 🏛️ Domínios

### Pessoas e Contratos (`pessoascontratos`)

Gerencia colaboradores, contratos e informações relacionadas.

**Endpoints principais**:
- `POST /api/v1/colaboradores` - Criar colaborador
- `GET /api/v1/colaboradores/{id}` - Buscar por ID
- `GET /api/v1/colaboradores` - Listar com filtros e paginação
- `PUT /api/v1/colaboradores/{id}` - Atualizar
- `DELETE /api/v1/colaboradores/{id}` - Deletar (hard delete)
- `PATCH /api/v1/colaboradores/{id}/demitir` - Demitir
- `PATCH /api/v1/colaboradores/{id}/ativar` - Ativar
- `PATCH /api/v1/colaboradores/{id}/inativar` - Inativar
- `PATCH /api/v1/colaboradores/{id}/excluir` - Excluir (soft delete)

### Organização (`organizacao`)

Gerencia clientes, empresas, unidades, departamentos e centros de custo.

**Nota**: Atualmente contém apenas as entidades JPA (`organizacao-outbound`). O core e inbound serão implementados quando necessário.

---

## 🔐 Segurança

A API utiliza autenticação JWT via OAuth2 Resource Server.

### Configuração

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth.example.com
```

### Uso

1. Obtenha um token JWT no servidor de autenticação
2. Inclua no header: `Authorization: Bearer {token}`
3. Acesse endpoints protegidos

---

## 📝 Tecnologias

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring Security** (OAuth2 Resource Server)
- **PostgreSQL 16**
- **Flyway** (Migrações de banco)
- **MapStruct** (Mapeamento de objetos)
- **Lombok** (Redução de boilerplate)
- **SpringDoc OpenAPI** (Documentação Swagger)
- **Maven** (Multi-módulo)

---

## 🤝 Contribuindo

1. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
2. Siga a arquitetura hexagonal e as boas práticas
3. Adicione testes
4. Commit suas mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
5. Push para a branch (`git push origin feature/nova-funcionalidade`)
6. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença Apache 2.0.
