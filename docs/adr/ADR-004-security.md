# ADR-004: Decisões sobre Segurança

## Status
✅ Aceito

## Contexto
O projeto precisa:
- Autenticar usuários via OAuth2/JWT
- Controlar acesso por empresa
- Validar permissões em operações de negócio
- Manter auditoria de operações

## Decisões

### 1. OAuth2 Resource Server com JWT

**Decisão:** Usar Spring Security OAuth2 Resource Server com validação de JWT.

**Justificativa:**
- Padrão da indústria
- Stateless (sem sessão)
- Escalável
- Integração nativa com Spring Security

### 2. Abstração de Segurança no Core

**Decisão:** Core não conhece Spring Security. Usa interface `SecurityContext`.

**Justificativa:**
- Mantém core independente de frameworks
- Facilita testes
- Permite trocar mecanismo de segurança

**Implementação:**
```java
// Core (interface)
public interface SecurityContext {
    boolean canAccessEmpresa(Long empresaId);
    boolean isGlobalAdmin();
}

// Application (implementação)
@Component
public class SpringSecurityContextAdapter implements SecurityContext {
    // Usa SecurityContextService do Spring
}
```

### 3. Validação de Permissões no Core

**Decisão:** Validação de permissões acontece no core (Application Layer).

**Justificativa:**
- Regra de negócio (quem pode acessar o quê)
- Não é responsabilidade da infraestrutura
- Garante que validação sempre acontece

**Exemplo:**
```java
public Colaborador criar(Colaborador colaborador) {
    // Valida permissão antes de criar
    accessValidator.validarAcessoEmpresa(colaborador.getEmpresaId());
    // ... resto da lógica
}
```

### 4. Filtros de Segurança Automáticos

**Decisão:** Aplicar filtros de segurança automaticamente em queries.

**Justificativa:**
- Previne vazamento de dados
- Transparente para o desenvolvedor
- Consistente em todas as queries

**Exemplo:**
```java
private ColaboradorFilter aplicarFiltrosDeSeguranca(ColaboradorFilter filter) {
    if (accessValidator.isAdmin()) {
        return filter; // Admin vê tudo
    }
    // Usuário comum só vê dados de sua empresa
    Long empresaIdUsuario = accessValidator.getEmpresaIdUsuario();
    return filter.toBuilder().empresaId(empresaIdUsuario).build();
}
```

### 5. Auditoria Automática

**Decisão:** Usar JPA Auditing para capturar criadoPor/atualizadoPor.

**Justificativa:**
- Automático e transparente
- Não precisa lembrar de preencher manualmente
- Consistente em todas as entidades

## Consequências

### Positivas
- ✅ Segurança robusta e padronizada
- ✅ Core independente de frameworks de segurança
- ✅ Fácil testar (mock de SecurityContext)
- ✅ Auditoria automática
- ✅ Prevenção de vazamento de dados

### Negativas
- ⚠️ Complexidade adicional
- ⚠️ Requer configuração de OAuth2
- ⚠️ Curva de aprendizado

## Alternativas Consideradas

### Spring Security Direto no Core
- ❌ Acopla core a Spring
- ❌ Difícil testar
- ❌ Viola Arquitetura Hexagonal

### Validação Apenas no Controller
- ❌ Pode ser esquecida
- ❌ Inconsistente
- ❌ Não protege chamadas internas

### Sem Abstração
- ❌ Core acoplado a Spring Security
- ❌ Difícil trocar mecanismo
- ❌ Testes complexos

## Referências
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [JWT Best Practices](https://datatracker.ietf.org/doc/html/rfc8725)

