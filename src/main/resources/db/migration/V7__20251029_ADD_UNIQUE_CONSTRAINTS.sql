-- ============================
-- CONSTRAINTS DE UNICIDADE
-- ============================
-- Garante integridade de dados e previne race conditions
-- ao adicionar constraints no nível do banco de dados

-- Unicidade de CPF por empresa
-- Permite o mesmo CPF em empresas diferentes (se for o caso)
-- Se quiser unicidade global, remova a coluna EMPRESA_ID do índice
CREATE UNIQUE INDEX UQ_COLABORADOR_CPF_EMPRESA 
ON PEOPLE_FLOW_RH.COLABORADOR(CPF, EMPRESA_ID);

-- Unicidade de Email por empresa
-- Permite o mesmo email em empresas diferentes (se for o caso)
-- Se quiser unicidade global, remova a coluna EMPRESA_ID do índice
CREATE UNIQUE INDEX UQ_COLABORADOR_EMAIL_EMPRESA 
ON PEOPLE_FLOW_RH.COLABORADOR(EMAIL, EMPRESA_ID);

-- Unicidade de Matrícula por empresa (permite NULL)
-- WHERE MATRICULA IS NOT NULL permite que múltiplos registros tenham matrícula NULL
CREATE UNIQUE INDEX UQ_COLABORADOR_MATRICULA_EMPRESA 
ON PEOPLE_FLOW_RH.COLABORADOR(MATRICULA, EMPRESA_ID)
WHERE MATRICULA IS NOT NULL;

-- ============================
-- COMENTÁRIOS IMPORTANTES
-- ============================
-- Se um colaborador NÃO PODE estar em múltiplas empresas ao mesmo tempo,
-- use constraints globais ao invés das acima:
--
-- CREATE UNIQUE INDEX UQ_COLABORADOR_CPF_GLOBAL ON PEOPLE_FLOW_RH.COLABORADOR(CPF);
-- CREATE UNIQUE INDEX UQ_COLABORADOR_EMAIL_GLOBAL ON PEOPLE_FLOW_RH.COLABORADOR(EMAIL);
-- CREATE UNIQUE INDEX UQ_COLABORADOR_MATRICULA_GLOBAL ON PEOPLE_FLOW_RH.COLABORADOR(MATRICULA) WHERE MATRICULA IS NOT NULL;

