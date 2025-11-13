# ADR-003: MapStruct para Mapeamento de Objetos

## Status
✅ Aceito

## Contexto
O projeto precisa mapear objetos entre diferentes camadas:
- DTOs HTTP ↔ Entidades de Domínio
- Entidades JPA ↔ Entidades de Domínio
- Filtros HTTP ↔ Filtros de Domínio

Mapeamento manual é:
- ❌ Verboso e repetitivo
- ❌ Propenso a erros
- ❌ Difícil de manter

## Decisão
Adotamos **MapStruct** para geração automática de mappers em tempo de compilação.

### Por que MapStruct?

1. **Performance**
   - Gera código Java puro (sem reflection)
   - Zero overhead em runtime
   - Mais rápido que outras soluções

2. **Type Safety**
   - Validação em tempo de compilação
   - Erros detectados antes de executar
   - IDE autocomplete funciona

3. **Simplicidade**
   - Anotações simples
   - Integração com Lombok
   - Geração automática

### Exemplo de Uso

```java
@Mapper(componentModel = "spring")
public interface ColaboradorWebMapper {
    Colaborador toDomain(ColaboradorRequest request);
    ColaboradorResponse toResponse(Colaborador colaborador);
    ColaboradorFilter toDomain(ColaboradorFilterRequest request);
}
```

MapStruct gera a implementação automaticamente.

## Consequências

### Positivas
- ✅ Código limpo e declarativo
- ✅ Performance excelente
- ✅ Type safety
- ✅ Fácil de manter
- ✅ Integração com Lombok

### Negativas
- ⚠️ Requer recompilação para ver mudanças
- ⚠️ Curva de aprendizado inicial
- ⚠️ Dependência adicional

## Alternativas Consideradas

### Mapeamento Manual
- ❌ Muito código boilerplate
- ❌ Propenso a erros
- ❌ Difícil de manter

### ModelMapper / Orika
- ✅ Automático
- ❌ Usa reflection (mais lento)
- ❌ Erros só em runtime
- ❌ Menos type safety

### Dozer
- ✅ Automático
- ❌ Projeto descontinuado
- ❌ Reflection-based

## Referências
- [MapStruct Documentation](https://mapstruct.org/)
- [MapStruct vs ModelMapper](https://www.baeldung.com/java-performance-mapping-frameworks)

