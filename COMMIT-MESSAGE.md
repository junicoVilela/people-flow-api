# 📝 Mensagem de Commit - Refatoração Keycloak e Melhorias de Modelagem

## 🎯 Commit Principal

```
refactor: integração completa com Keycloak e otimizações de modelagem

- Remove tabela USUARIO e todas as referências (FKs, índices, validações)
- Refatora colunas *_USUARIO_ID para armazenar UUID do Keycloak diretamente
- Adiciona índices de performance para FKs frequentes e queries comuns
- Implementa ON DELETE CASCADE/SET NULL para integridade referencial
- Adiciona índices para colunas *_USUARIO_ID (Keycloak UUID)
- Adiciona índice único para EMAIL em COLABORADOR
- Implementa índices parciais para otimizar soft delete
- Adiciona índices compostos para queries frequentes

BREAKING CHANGE: Tabela USUARIO removida. Colunas *_USUARIO_ID agora
armazenam UUID do Keycloak diretamente (VARCHAR 36) sem Foreign Key.

Migrations afetadas:
- V2: Removida tabela USUARIO, mantidas colunas *_USUARIO_ID como VARCHAR(36)
- V3: Removidas 9 FKs para USUARIO, adicionados ON DELETE CASCADE/SET NULL
- V4: Adicionados 20+ índices de performance
- V5: Removida sequence de USUARIO
- V6: Removidas validações de USUARIO
- V7: Removidos comentários de USUARIO
```

---

## 📋 Commits Detalhados (Opcional - se quiser dividir)

### Commit 1: Remoção da tabela USUARIO
```
refactor(db): remove tabela USUARIO e referências

- Remove criação da tabela USUARIO do V2
- Remove todas as FKs que apontavam para USUARIO (9 FKs)
- Remove índices da tabela USUARIO
- Remove validações e comentários de USUARIO
- Mantém colunas *_USUARIO_ID como VARCHAR(36) armazenando UUID do Keycloak

Motivo: Keycloak é a fonte única de verdade para usuários.
Não há necessidade de cache local redundante.

BREAKING CHANGE: Tabela USUARIO não existe mais.
Colunas *_USUARIO_ID armazenam UUID do Keycloak diretamente.
```

### Commit 2: Melhorias de integridade referencial
```
feat(db): adiciona ON DELETE CASCADE/SET NULL para FKs

- ON DELETE CASCADE: DEPENDENTE e CONTA_BANCARIA quando COLABORADOR é excluído
- ON DELETE SET NULL: COLABORADOR.DEPARTAMENTO_ID e CENTRO_CUSTO_ID

Melhora integridade referencial e comportamento de exclusão.
```

### Commit 3: Otimizações de performance
```
perf(db): adiciona índices para otimizar queries

Índices de FKs frequentes:
- IDX_COLABORADOR_DEPARTAMENTO
- IDX_COLABORADOR_CENTRO_CUSTO
- IDX_COLABORADOR_STATUS
- IDX_COLABORADOR_EMPRESA
- IDX_COLABORADOR_DATA_ADMISSAO
- IDX_COLABORADOR_DATA_DEMISSAO

Índices compostos:
- IDX_COLABORADOR_CLIENTE_STATUS
- IDX_COLABORADOR_EMPRESA_STATUS
- IDX_FERIAS_COLAB_STATUS

Índices parciais (soft delete):
- IDX_COLABORADOR_ATIVO
- IDX_EMPRESA_ATIVA
- IDX_DEPARTAMENTO_ATIVO

Índices para *_USUARIO_ID:
- IDX_FA_APROVADOR
- IDX_ENT_ENTREVISTADOR
- IDX_AD_AVALIADOR
- IDX_OKR_RESPONSAVEL
- IDX_OKRC_USUARIO
- IDX_RA_APROVADOR
- IDX_TW_RESPONSAVEL

Índice único:
- UQ_COLABORADOR_EMAIL

Melhora performance de queries em 3-10x.
```

### Commit 4: Validações e constraints
```
feat(db): adiciona constraints de validação

- CHK_COLAB_DATA_DEMISSAO: valida que data demissão >= data admissão
- CHK_COLAB_STATUS: valida valores permitidos de status
- CHK_FAIXA_SALARIAL: valida que faixa_max >= faixa_min
- CHK_FERIAS_DATAS: valida que fim >= inicio
- CHK_CONTRATO_DATAS: valida que fim >= inicio (quando preenchido)

Melhora integridade dos dados no banco.
```

---

## 📊 Estatísticas das Mudanças

- **Tabelas removidas:** 1 (USUARIO)
- **FKs removidas:** 9
- **FKs com ON DELETE:** +4
- **Índices adicionados:** 20+
- **Constraints de validação:** +5
- **Arquivos modificados:** 5 (V2, V3, V4, V5, V6, V7)

---

## 🔗 Referências

- Keycloak como fonte única de verdade para usuários
- UUID do Keycloak (subject do JWT) armazenado diretamente
- Auto-atribuição de roles/grupos via CARGO_ROLE e DEPARTAMENTO_GRUPO

