# ADR-005: Separação de dados People Flow vs Keycloak

## Status
✅ Aceito

## Contexto

O sistema utiliza **Keycloak** para identidade e acesso (usuários, grupos, roles, sessões) e um **banco próprio** para dados de negócio (People Flow). É essencial que:

- Não haja **duplicidade** de dados (ex.: usuário cadastrado nos dois lados).
- Cada dado fique no **local correto**: identidade no Keycloak, negócio no People Flow.

## Onde cada dado fica

### 1. Keycloak (banco/serviço do Keycloak – fora deste repositório)

| Dado | Responsável | Observação |
|------|-------------|------------|
| Usuários (login, senha, atributos de auth) | Keycloak | **Nunca** replicar em tabela USUARIO no People Flow |
| Grupos (hierarquia, membros) | Keycloak | People Flow só guarda **referência** (UUID + nome) |
| Roles (realm, client) | Keycloak | People Flow só guarda **nome da role** para mapeamento |
| Sessões / tokens | Keycloak | — |

### 2. People Flow (schema `PEOPLE_FLOW_RH` – migrations neste repositório)

| Dado | Tabela(s) | Observação |
|------|-----------|------------|
| Empresas, Unidades, Departamentos, Centros de Custo | EMPRESA, UNIDADE, DEPARTAMENTO, CENTRO_CUSTO | 100% negócio |
| Colaboradores (nome, CPF, matrícula, empresa, cargo, etc.) | COLABORADOR | Negócio; **vínculo** ao usuário via UUID |
| Contratos, cargos, jornadas, folha, etc. | Várias | 100% negócio |
| **Referência** Colaborador → Usuário Keycloak | COLABORADOR.KEYCLOAK_USER_ID | Apenas UUID (VARCHAR 36), **sem FK** |
| **Mapeamento** Cargo → Role Keycloak | CARGO_ROLE (CARGO_ID + ROLE_NAME) | Só nome da role para atribuição automática |
| **Mapeamento** Departamento → Grupo Keycloak | DEPARTAMENTO_GRUPO (DEPARTAMENTO_ID, KEYCLOAK_GROUP_ID, KEYCLOAK_GROUP_NAME) | Só referência ao grupo |
| Quem fez ação (auditoria) | Colunas *_USUARIO_ID, AUDITORIA.USUARIO_ID | Apenas UUID do Keycloak, **sem FK** |

### 3. Colunas que armazenam apenas UUID do Keycloak (sem FK)

Todas são `VARCHAR(36)` e **nunca** devem ter FK para tabela de usuário no People Flow:

- `COLABORADOR.KEYCLOAK_USER_ID`
- `FERIAS_APROVACAO.APROVADOR_USUARIO_ID`
- `ENTREVISTA.ENTREVISTADOR_USUARIO_ID`
- `AVALIACAO_DESEMPENHO.AVALIADOR_USUARIO_ID`
- `OKR.RESPONSAVEL_USUARIO_ID`
- `OKR_CHECKIN.USUARIO_ID`
- `REEMBOLSO_APROVACAO.APROVADOR_USUARIO_ID`
- `TAREFA_WORKFLOW.RESPONSAVEL_USUARIO_ID`
- `NOTIFICACAO_ENVIO.USUARIO_ID`
- `AUDITORIA.USUARIO_ID`

## Regras para evitar duplicidade

1. **Não criar tabela USUARIO (ou equivalente)** no banco People Flow. Dados de usuário (login, email de acesso, senha, etc.) vêm **somente** do Keycloak.

2. **Não criar FK** de colunas `*_USUARIO_ID` ou `KEYCLOAK_*` para nenhuma tabela no banco People Flow. Elas referenciam entidades que existem **apenas** no Keycloak.

3. **Tabelas de mapeamento** (CARGO_ROLE, DEPARTAMENTO_GRUPO) ficam no People Flow porque:
   - Referenciam entidades de negócio (CARGO, DEPARTAMENTO) por FK.
   - Guardam apenas **identificadores/nomes** do Keycloak (ROLE_NAME, KEYCLOAK_GROUP_ID, KEYCLOAK_GROUP_NAME), não cópia de dados.

4. Ao adicionar nova coluna que referencia “quem aprovou / quem fez”:
   - Use `*_USUARIO_ID VARCHAR(36)` e documente nos comentários da migration que é UUID do Keycloak (sem FK).

5. Ao integrar com Keycloak (criar usuário, grupo, role):
   - Use a **API do Keycloak** (Feign/outbound). O repositório de usuários/grupos/roles é o **Keycloak**, não o banco People Flow.

## Referências no código

- Comentários nas migrations: `V7__20251112_COMMENTS.sql`, `V3__20251112_CONSTRAINTS_FK_UQ.sql`, `V4__20251112_INDEXES.sql`.
- Módulo `people-flow-accesscontrol`: integração com Keycloak (usuário, grupo, role).
- `Colaborador.keycloakUserId`: vínculo colaborador ↔ usuário Keycloak.
