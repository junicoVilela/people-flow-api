-- ============================
-- SEED DATA: Cargos de exemplo (necessários para cargo_role)
-- ============================
-- Insere cargos 1 a 6 para que os mapeamentos cargo_role tenham FK válida.
INSERT INTO people_flow_rh.cargo (id, nome, descricao, nivel) VALUES
(1, 'Gerente de RH', 'Gerente da área de Recursos Humanos', 'senior'),
(2, 'Analista de RH', 'Analista de Recursos Humanos', 'pleno'),
(3, 'Assistente de RH', 'Assistente administrativo de RH', 'junior'),
(4, 'Gestor de Área', 'Gestor de equipe/departamento', 'senior'),
(5, 'Colaborador Padrão', 'Colaborador operacional', 'junior'),
(6, 'Administrador de Sistema', 'Acesso administrativo completo', 'senior')
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Mapeamento Cargo → Roles
-- ============================

-- ============================
-- EXEMPLO: Cargo 1 - Gerente de RH
-- ============================
-- Permissões totais em colaboradores
INSERT INTO people_flow_rh.cargo_role (cargo_id, role_name, descricao) VALUES
(1, 'colaborador:criar', 'Criar novos colaboradores'),
(1, 'colaborador:ler', 'Visualizar dados de colaboradores'),
(1, 'colaborador:editar', 'Editar dados de colaboradores'),
(1, 'colaborador:deletar', 'Deletar colaboradores'),
(1, 'colaborador:aprovar', 'Aprovar cadastros e alterações'),

-- Permissões de gestão de benefícios
(1, 'beneficio:criar', 'Criar planos de benefícios'),
(1, 'beneficio:ler', 'Visualizar benefícios'),
(1, 'beneficio:editar', 'Editar benefícios'),
(1, 'beneficio:aprovar', 'Aprovar solicitações de benefícios'),

-- Permissões de férias
(1, 'ferias:ler', 'Visualizar férias'),
(1, 'ferias:aprovar', 'Aprovar solicitações de férias'),

-- Permissões de ponto
(1, 'ponto:ler', 'Visualizar marcações de ponto'),
(1, 'ponto:editar', 'Editar marcações de ponto'),

-- Permissões de folha
(1, 'folha:ler', 'Visualizar folhas de pagamento'),
(1, 'folha:processar', 'Processar folhas de pagamento'),

-- Permissões de relatórios
(1, 'relatorio:gerar', 'Gerar relatórios gerenciais'),

-- Permissões administrativas
(1, 'admin', 'Acesso administrativo completo')
ON CONFLICT (cargo_id, role_name) DO NOTHING;

-- ============================
-- EXEMPLO: Cargo 2 - Analista de RH
-- ============================
INSERT INTO people_flow_rh.cargo_role (cargo_id, role_name, descricao) VALUES
(2, 'colaborador:criar', 'Criar novos colaboradores'),
(2, 'colaborador:ler', 'Visualizar dados de colaboradores'),
(2, 'colaborador:editar', 'Editar dados de colaboradores'),

(2, 'beneficio:ler', 'Visualizar benefícios'),
(2, 'beneficio:editar', 'Gerenciar adesões de benefícios'),

(2, 'ferias:ler', 'Visualizar férias'),
(2, 'ferias:editar', 'Gerenciar solicitações de férias'),

(2, 'ponto:ler', 'Visualizar marcações de ponto'),

(2, 'folha:ler', 'Visualizar folhas de pagamento')
ON CONFLICT (cargo_id, role_name) DO NOTHING;

-- ============================
-- EXEMPLO: Cargo 3 - Assistente de RH
-- ============================
INSERT INTO people_flow_rh.cargo_role (cargo_id, role_name, descricao) VALUES
(3, 'colaborador:ler', 'Visualizar dados de colaboradores'),

(3, 'beneficio:ler', 'Visualizar benefícios'),

(3, 'ferias:ler', 'Visualizar férias'),

(3, 'ponto:ler', 'Visualizar marcações de ponto')
ON CONFLICT (cargo_id, role_name) DO NOTHING;

-- ============================
-- EXEMPLO: Cargo 4 - Gestor de Área
-- ============================
INSERT INTO people_flow_rh.cargo_role (cargo_id, role_name, descricao) VALUES
(4, 'colaborador:ler', 'Visualizar colaboradores da sua equipe'),

(4, 'ferias:ler', 'Visualizar férias da equipe'),
(4, 'ferias:aprovar', 'Aprovar férias da equipe'),

(4, 'ponto:ler', 'Visualizar ponto da equipe'),

(4, 'avaliacao:criar', 'Criar avaliações de desempenho'),
(4, 'avaliacao:editar', 'Editar avaliações de desempenho'),

(4, 'reembolso:aprovar', 'Aprovar reembolsos da equipe')
ON CONFLICT (cargo_id, role_name) DO NOTHING;

-- ============================
-- EXEMPLO: Cargo 5 - Colaborador Padrão
-- ============================
INSERT INTO people_flow_rh.cargo_role (cargo_id, role_name, descricao) VALUES
(5, 'colaborador:ler', 'Visualizar próprios dados'),

(5, 'ferias:ler', 'Visualizar próprias férias'),
(5, 'ferias:criar', 'Solicitar férias'),

(5, 'ponto:ler', 'Visualizar próprio ponto'),
(5, 'ponto:marcar', 'Registrar marcações de ponto'),

(5, 'beneficio:ler', 'Visualizar próprios benefícios'),

(5, 'reembolso:criar', 'Solicitar reembolsos'),
(5, 'reembolso:ler', 'Visualizar próprios reembolsos')
ON CONFLICT (cargo_id, role_name) DO NOTHING;

-- ============================
-- EXEMPLO: Cargo 6 - Administrador de Sistema
-- ============================
INSERT INTO people_flow_rh.cargo_role (cargo_id, role_name, descricao) VALUES
(6, 'admin', 'Acesso administrativo completo ao sistema'),
(6, '*:*', 'Todas as permissões')
ON CONFLICT (cargo_id, role_name) DO NOTHING;

-- ============================
-- SEED DATA: Mapeamento Departamento → Grupo Keycloak
-- ============================
--
-- IMPORTANTE: Execute estas inserções APÓS criar os grupos no Keycloak!
--
-- Como obter os UUIDs dos grupos:
--   1. Acesse Keycloak Admin Console
--   2. Vá em: Realm → Groups
--   3. Clique no grupo e copie o ID da URL
--   
-- OU use a API:
--   curl -X GET "http://localhost:8081/admin/realms/peopleflow/groups" \
--     -H "Authorization: Bearer <admin-token>"
--

-- DESCOMENTE e ajuste os UUIDs reais após criar os grupos no Keycloak:

-- INSERT INTO people_flow_rh.departamento_grupo 
--   (departamento_id, keycloak_group_id, keycloak_group_name) 
-- VALUES
--   (1, '<UUID do grupo RH no Keycloak>', 'RH - Gestão'),
--   (2, '<UUID do grupo TI no Keycloak>', 'TI - Desenvolvimento'),
--   (3, '<UUID do grupo Financeiro no Keycloak>', 'Financeiro'),
--   (4, '<UUID do grupo Comercial no Keycloak>', 'Comercial')
-- ON CONFLICT (departamento_id) DO UPDATE SET
--   keycloak_group_id = EXCLUDED.keycloak_group_id,
--   keycloak_group_name = EXCLUDED.keycloak_group_name,
--   atualizado_em = clock_timestamp(),
--   atualizado_por = 'SYSTEM';

-- ============================
-- NOTA IMPORTANTE - CARGO_ROLE
-- ============================
-- 
-- Este é um exemplo de seed data. Em produção, você deve:
-- 
-- 1. Ajustar os cargo_id conforme seus cargos reais
-- 2. Definir suas próprias roles baseadas nos recursos do sistema
-- 3. Considerar usar um padrão de nomenclatura consistente (recurso:acao)
-- 4. Mapear as roles do banco com as roles configuradas no Keycloak
-- 
-- Para adicionar novos mapeamentos:
--   INSERT INTO people_flow_rh.cargo_role (cargo_id, role_name, descricao) 
--   VALUES (X, 'recurso:acao', 'Descrição da permissão')
--   ON CONFLICT (cargo_id, role_name) DO NOTHING;
--

