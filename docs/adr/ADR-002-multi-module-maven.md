# ADR-002: Multi-módulo Maven

## Status
✅ Aceito

## Contexto
O projeto People Flow API precisa:
- Separar domínios diferentes (Pessoas/Contratos, Organização)
- Enforçar dependências corretas entre módulos
- Facilitar build incremental
- Permitir reutilização de código comum

## Decisão
Adotamos estrutura **multi-módulo Maven** com separação por domínio e responsabilidade.

### Estrutura de Módulos

```
people-flow-api (parent)
├── people-flow-common          # Código compartilhado
├── people-flow-pessoascontratos
│   ├── pessoascontratos-core      # Regras de negócio
│   ├── pessoascontratos-inbound   # Adaptadores de entrada (HTTP)
│   └── pessoascontratos-outbound  # Adaptadores de saída (JPA)
├── people-flow-organizacao
│   └── organizacao-outbound       # Adaptadores de saída (JPA)
└── people-flow-application     # Aplicação Spring Boot
```

### Regras de Dependência

1. **Core não depende de nada externo**
   - Apenas `common` e `lombok`
   - Zero dependências de frameworks

2. **Inbound depende de Core**
   - Implementa ports de input
   - Pode usar Spring Web

3. **Outbound depende de Core**
   - Implementa ports de output
   - Pode usar Spring Data JPA

4. **Application depende de tudo**
   - Orquestra todos os módulos
   - Configura Spring Boot

## Consequências

### Positivas
- ✅ Separação clara de responsabilidades
- ✅ Build incremental (Maven compila apenas módulos alterados)
- ✅ Dependências explícitas e validadas
- ✅ Fácil adicionar novos domínios
- ✅ Core pode ser reutilizado em outros projetos

### Negativas
- ⚠️ Mais complexidade no build
- ⚠️ Mais arquivos `pom.xml` para manter
- ⚠️ Requer conhecimento de Maven multi-módulo

## Alternativas Consideradas

### Monolítico
- ❌ Tudo em um módulo
- ❌ Difícil enforçar separação
- ❌ Build mais lento

### Microserviços
- ✅ Separação total
- ❌ Overhead de infraestrutura
- ❌ Complexidade desnecessária para o tamanho atual

## Referências
- [Maven Multi-module Projects](https://maven.apache.org/guides/mini/guide-multiple-modules.html)
- [Domain-Driven Design - Bounded Contexts](https://martinfowler.com/bliki/BoundedContext.html)

