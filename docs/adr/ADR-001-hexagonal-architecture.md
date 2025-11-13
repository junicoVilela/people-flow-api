# ADR-001: Arquitetura Hexagonal (Ports & Adapters)

## Status
✅ Aceito

## Contexto
O projeto People Flow API precisa de uma arquitetura que:
- Seja testável sem dependências de frameworks
- Permita trocar tecnologias sem reescrever regras de negócio
- Facilite a manutenção e evolução do código
- Seja clara sobre responsabilidades de cada camada

## Decisão
Adotamos a **Arquitetura Hexagonal** (também conhecida como Ports & Adapters), proposta por Alistair Cockburn.

### Princípios Aplicados

1. **Core Independente de Frameworks**
   - O módulo `core` não conhece Spring, JPA, HTTP, etc.
   - Apenas interfaces (ports) são definidas no core
   - Implementações (adapters) ficam nos módulos de infraestrutura

2. **Separação de Responsabilidades**
   ```
   Core (Domínio)
   ├── domain/        # Entidades e regras de negócio
   ├── application/    # Casos de uso
   ├── ports/
   │   ├── input/     # Interfaces que o core expõe
   │   └── output/     # Interfaces que o core consome
   └── valueobject/    # Objetos de valor
   
   Infrastructure
   ├── inbound/       # Adaptadores de entrada (HTTP, CLI, etc.)
   └── outbound/      # Adaptadores de saída (JPA, REST, etc.)
   ```

3. **Dependency Inversion**
   - Core define interfaces (ports)
   - Infrastructure implementa interfaces (adapters)
   - Core nunca depende de infrastructure

## Consequências

### Positivas
- ✅ Core pode ser testado sem Spring/JPA
- ✅ Fácil trocar banco de dados (basta criar novo adapter)
- ✅ Fácil trocar framework web (basta criar novo adapter)
- ✅ Regras de negócio isoladas e reutilizáveis
- ✅ Melhor testabilidade (mocks simples)

### Negativas
- ⚠️ Mais código (interfaces + implementações)
- ⚠️ Curva de aprendizado inicial
- ⚠️ Requer disciplina para manter separação

## Alternativas Consideradas

### Arquitetura em Camadas Tradicional
- ❌ Core acoplado a frameworks
- ❌ Difícil testar sem Spring
- ❌ Difícil trocar tecnologias

### Clean Architecture
- ✅ Similar à Hexagonal
- ⚠️ Mais complexa para projetos menores
- ✅ Escolhemos Hexagonal por ser mais simples e direta

## Referências
- [Alistair Cockburn - Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)
- [Martin Fowler - Hexagonal Architecture](https://martinfowler.com/bliki/HexagonalArchitecture.html)

