-- ============================
-- ÍNDICES DE PERFORMANCE
-- ============================
-- Otimiza queries frequentes e melhora performance de buscas

-- ============================
-- ÍNDICES SIMPLES (Buscas Exatas)
-- ============================

-- Busca por CPF (query frequente em validações e consultas)
CREATE INDEX IDX_COLABORADOR_CPF 
ON PEOPLE_FLOW_RH.COLABORADOR(CPF);

-- Busca por Email (query frequente em validações e consultas)
CREATE INDEX IDX_COLABORADOR_EMAIL 
ON PEOPLE_FLOW_RH.COLABORADOR(EMAIL);

-- Busca por Matrícula (query frequente, permite NULL)
CREATE INDEX IDX_COLABORADOR_MATRICULA 
ON PEOPLE_FLOW_RH.COLABORADOR(MATRICULA) 
WHERE MATRICULA IS NOT NULL;

-- Busca por Nome (frequente em listagens e ordenação)
CREATE INDEX IDX_COLABORADOR_NOME 
ON PEOPLE_FLOW_RH.COLABORADOR(NOME);

-- Busca por Status (filtro muito comum)
CREATE INDEX IDX_COLABORADOR_STATUS 
ON PEOPLE_FLOW_RH.COLABORADOR(STATUS);

-- ============================
-- ÍNDICES COMPOSTOS (Filtros Combinados)
-- ============================

-- Filtro comum: listar colaboradores de uma empresa por status
CREATE INDEX IDX_COLABORADOR_EMPRESA_STATUS 
ON PEOPLE_FLOW_RH.COLABORADOR(EMPRESA_ID, STATUS);

-- Filtro comum: filtrar por cliente e empresa
CREATE INDEX IDX_COLABORADOR_CLIENTE_EMPRESA 
ON PEOPLE_FLOW_RH.COLABORADOR(CLIENTE_ID, EMPRESA_ID);

-- Filtro comum: buscar por departamento
CREATE INDEX IDX_COLABORADOR_DEPARTAMENTO 
ON PEOPLE_FLOW_RH.COLABORADOR(DEPARTAMENTO_ID)
WHERE DEPARTAMENTO_ID IS NOT NULL;

-- Filtro comum: buscar por centro de custo
CREATE INDEX IDX_COLABORADOR_CENTRO_CUSTO 
ON PEOPLE_FLOW_RH.COLABORADOR(CENTRO_CUSTO_ID)
WHERE CENTRO_CUSTO_ID IS NOT NULL;

-- ============================
-- ÍNDICES PARA ORDENAÇÃO
-- ============================

-- Ordenação por data de admissão (relatórios)
CREATE INDEX IDX_COLABORADOR_DATA_ADMISSAO 
ON PEOPLE_FLOW_RH.COLABORADOR(DATA_ADMISSAO);

-- Ordenação por data de demissão (relatórios)
CREATE INDEX IDX_COLABORADOR_DATA_DEMISSAO 
ON PEOPLE_FLOW_RH.COLABORADOR(DATA_DEMISSAO)
WHERE DATA_DEMISSAO IS NOT NULL;

-- ============================
-- ÍNDICE TRIGRAM (Busca Parcial de Nome)
-- ============================
-- Melhora drasticamente performance de buscas com LIKE/ILIKE
-- Requer extensão pg_trgm (geralmente já instalada no PostgreSQL)

-- Criar extensão se não existir
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Índice trigram para busca parcial de nome (ILIKE '%João%')
CREATE INDEX IDX_COLABORADOR_NOME_TRGM 
ON PEOPLE_FLOW_RH.COLABORADOR 
USING gin (NOME gin_trgm_ops);

-- ============================
-- COMENTÁRIOS E MONITORAMENTO
-- ============================
-- Para verificar uso dos índices:
-- SELECT * FROM pg_stat_user_indexes WHERE schemaname = 'people_flow_rh' AND relname = 'colaborador';
--
-- Para verificar índices não utilizados:
-- SELECT * FROM pg_stat_user_indexes WHERE idx_scan = 0 AND schemaname = 'people_flow_rh';

