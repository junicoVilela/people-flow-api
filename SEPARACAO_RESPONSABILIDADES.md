# 🎯 Separação de Responsabilidades - Domain vs Application Service

## Visão Geral

Na arquitetura hexagonal com DDD, é fundamental separar corretamente as validações entre as camadas:

- **DomainService**: Regras de negócio puras (sem dependência de infraestrutura)
- **ApplicationService (UseCase)**: Orquestração e validações que dependem de infraestrutura

---

## 📦 Estrutura Atual

```
ColaboradorDomainService (Domain Layer)
├── validarDadosObrigatorios()     ✅ Regra de negócio
├── validarDemissao()              ✅ Regra de negócio
├── validarAtivacao()              ✅ Regra de negócio
└── validarExclusao()              ✅ Regra de negócio

ColaboradorService (Application Layer)
├── validarUnicidadeCpf()          ✅ Usa repositório
├── validarUnicidadeEmail()        ✅ Usa repositório
├── validarUnicidadeMatricula()    ✅ Usa repositório
├── validarUnicidade*ParaAtualizacao() ✅ Usa repositório
└── Orquestração dos casos de uso
```

---

## 🔍 Critérios de Separação

### ✅ Fica no **DomainService**

**Validações de regras de negócio que:**
- ❌ NÃO dependem de infraestrutura/repositório
- ✅ Podem ser testadas sem banco de dados
- ✅ Representam conhecimento do domínio
- ✅ São regras de negócio puras

**Exemplos:**

```java
@Service
public class ColaboradorDomainService {
    
    // ✅ Regra de negócio: campos obrigatórios
    public void validarDadosObrigatorios(Colaborador colaborador) {
        if (colaborador.getNome() == null) {
            throw new BusinessException("NOME_OBRIGATORIO", "Nome é obrigatório");
        }
    }
    
    // ✅ Regra de negócio: data de demissão
    public void validarDemissao(Colaborador colaborador, LocalDate dataDemissao) {
        if (dataDemissao.isBefore(colaborador.getDataAdmissao())) {
            throw new BusinessException("DATA_DEMISSAO_INVALIDA", 
                "Data de demissão não pode ser anterior à admissão");
        }
    }
    
    // ✅ Regra de negócio: status
    public void validarAtivacao(Colaborador colaborador) {
        if (colaborador.getStatus().isExcluido()) {
            throw new BusinessException("COLABORADOR_EXCLUIDO", 
                "Não é possível ativar colaborador excluído");
        }
    }
}
```

---

### ✅ Fica no **ApplicationService (UseCase)**

**Validações que:**
- ✅ Dependem de infraestrutura (repositório, APIs externas, etc.)
- ✅ Coordenam múltiplos serviços
- ✅ Gerenciam transações
- ✅ Orquestram o fluxo do caso de uso

**Exemplos:**

```java
@Service
@Transactional
public class ColaboradorService implements ColaboradorUseCase {
    
    private final ColaboradorRepositoryPort repository;
    private final ColaboradorDomainService domainService;
    
    // ✅ Validação que usa repositório (infraestrutura)
    private void validarUnicidadeCpf(Cpf cpf) {
        if (repository.existePorCpf(cpf.getValorNumerico())) {
            throw new DuplicateResourceException("CPF", cpf.getValor());
        }
    }
    
    // ✅ Validação que usa repositório
    private void validarUnicidadeEmail(Email email) {
        if (repository.existePorEmail(email.getValor())) {
            throw new DuplicateResourceException("Email", email.getValor());
        }
    }
    
    // ✅ Orquestração do caso de uso
    @Override
    public Colaborador criar(Colaborador colaborador) {
        // 1. Validações de domínio (regras de negócio)
        domainService.validarDadosObrigatorios(colaborador);
        
        // 2. Validações de infraestrutura (unicidade)
        validarUnicidadeCpf(colaborador.getCpf());
        validarUnicidadeEmail(colaborador.getEmail());
        
        // 3. Persistência
        return repository.salvar(colaborador);
    }
}
```

---

## 🎓 Benefícios da Separação

### 1️⃣ **Testabilidade**

```java
// ✅ Teste do DomainService - SEM banco de dados
@Test
void deveValidarDadosObrigatorios() {
    // Não precisa de mock do repositório!
    ColaboradorDomainService domainService = new ColaboradorDomainService();
    Colaborador colaborador = new Colaborador(null, "12345678901", "email@test.com");
    
    assertThrows(BusinessException.class, 
        () -> domainService.validarDadosObrigatorios(colaborador));
}

// ✅ Teste do ApplicationService - COM mock do repositório
@Test
void deveValidarUnicidadeCpf() {
    when(repository.existePorCpf(anyString())).thenReturn(true);
    
    assertThrows(DuplicateResourceException.class,
        () -> service.criar(colaborador));
}
```

### 2️⃣ **Reutilização**

```java
// ✅ DomainService pode ser usado em múltiplos casos de uso
public class ColaboradorService {
    public Colaborador criar(Colaborador c) {
        domainService.validarDadosObrigatorios(c); // ← reutilizado
        // ...
    }
    
    public Colaborador atualizar(Long id, Colaborador c) {
        domainService.validarDadosObrigatorios(c); // ← reutilizado
        // ...
    }
}
```

### 3️⃣ **Separação de Preocupações**

```java
// ✅ DomainService: conhecimento do negócio
"Nome é obrigatório"
"Data de demissão não pode ser anterior à admissão"
"Não é possível ativar colaborador excluído"

// ✅ ApplicationService: coordenação técnica
"CPF já cadastrado" (depende de consulta ao banco)
"Email já cadastrado" (depende de consulta ao banco)
Gerenciamento de transações
```

### 4️⃣ **Independência de Framework**

```java
// ✅ DomainService é puro Java
// Pode ser movido para outro framework sem mudanças
public class ColaboradorDomainService {
    public void validarDemissao(Colaborador c, LocalDate data) {
        // Sem anotações Spring
        // Sem dependência de JPA
        // Apenas lógica de negócio
    }
}
```

---

## 📊 Comparação Detalhada

| Aspecto | DomainService | ApplicationService |
|---------|--------------|-------------------|
| **Dependências** | Nenhuma infraestrutura | Repositórios, APIs, etc. |
| **Testabilidade** | Testes unitários puros | Testes com mocks |
| **Conhecimento** | Regras de negócio | Orquestração técnica |
| **Reutilização** | Alta (entre casos de uso) | Específico do caso de uso |
| **Complexidade** | Baixa (lógica pura) | Alta (coordenação) |
| **Transações** | ❌ Não gerencia | ✅ Gerencia (@Transactional) |
| **Framework** | ❌ Independente | ✅ Dependente (Spring) |

---

## 🚫 Anti-Padrões (O que EVITAR)

### ❌ DomainService com Repositório
```java
// ❌ ERRADO!
@Service
public class ColaboradorDomainService {
    
    private final ColaboradorRepositoryPort repository; // ← NÃO!
    
    public void validarUnicidade(Cpf cpf) {
        if (repository.existePorCpf(cpf)) { // ← Viola princípios DDD
            throw new BusinessException("CPF duplicado");
        }
    }
}
```

### ❌ ApplicationService com Regras de Negócio Complexas
```java
// ❌ ERRADO!
@Service
public class ColaboradorService {
    
    public Colaborador demitir(Long id, LocalDate data) {
        Colaborador c = repository.findById(id);
        
        // ❌ Lógica de negócio no Application Service!
        if (data.isBefore(c.getDataAdmissao())) {
            throw new BusinessException("Data inválida");
        }
        if (c.getStatus().isDemitido()) {
            throw new BusinessException("Já demitido");
        }
        
        c.demitir(data);
        return repository.save(c);
    }
}
```

**✅ CORRETO:**
```java
@Service
public class ColaboradorService {
    
    public Colaborador demitir(Long id, LocalDate data) {
        Colaborador c = repository.findById(id);
        
        // ✅ Delega validações para o DomainService
        domainService.validarDemissao(c, data);
        
        c.demitir(data);
        return repository.save(c);
    }
}
```

---

## 📝 Checklist de Validação

Ao criar uma nova validação, pergunte-se:

### ✅ Deve ir para DomainService se:
- [ ] É uma regra de negócio pura?
- [ ] Pode ser testada sem banco de dados?
- [ ] Não depende de repositório/infraestrutura?
- [ ] Será reutilizada em múltiplos casos de uso?
- [ ] Representa conhecimento do domínio?

### ✅ Deve ir para ApplicationService se:
- [ ] Precisa consultar o banco de dados?
- [ ] Depende de API externa?
- [ ] Envolve orquestração de múltiplos serviços?
- [ ] Gerencia transações?
- [ ] É específico de um caso de uso?

---

## 🎯 Conclusão

A refatoração realizada moveu `validarDadosObrigatorios()` para `ColaboradorDomainService` porque:

✅ É regra de negócio pura  
✅ Não depende de infraestrutura  
✅ Pode ser testada sem mocks  
✅ Representa conhecimento do domínio  
✅ Será reutilizada em criar() e atualizar()  

As validações de unicidade permaneceram em `ColaboradorService` porque:

✅ Dependem do repositório  
✅ Precisam consultar o banco de dados  
✅ São validações de infraestrutura  
✅ Não podem ser testadas sem o banco  

---

**Esta separação é fundamental para manter a arquitetura limpa, testável e alinhada com os princípios DDD!** 🚀

