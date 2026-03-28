-- ============================
-- SEED DATA: Empresas
-- ============================
INSERT INTO people_flow_rh.empresa (id, nome, cnpj, status) VALUES
(1, 'People Flow Ltda', '11.222.333/0001-81', 'ativo'),
(2, 'Acme Consultoria', '33.444.555/0001-81', 'ativo')
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Unidades (empresa 1)
-- ============================
INSERT INTO people_flow_rh.unidade (id, empresa_id, nome, codigo, status) VALUES
(1, 1, 'Matriz São Paulo', 'MAT-SP', 'ativo'),
(2, 1, 'Filial Rio de Janeiro', 'FIL-RJ', 'ativo')
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Centro de Custo (empresa 1)
-- ============================
INSERT INTO people_flow_rh.centro_custo (id, empresa_id, codigo, nome, status) VALUES
(1, 1, 'CC-ADM', 'Administrativo', 'ativo'),
(2, 1, 'CC-RH', 'Recursos Humanos', 'ativo'),
(3, 1, 'CC-TI', 'Tecnologia', 'ativo')
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Departamentos (empresa 1, unidade 1)
-- ============================
INSERT INTO people_flow_rh.departamento (id, empresa_id, unidade_id, nome, codigo, status) VALUES
(1, 1, 1, 'Recursos Humanos', 'RH', 'ativo'),
(2, 1, 1, 'Tecnologia da Informação', 'TI', 'ativo'),
(3, 1, 1, 'Financeiro', 'FIN', 'ativo'),
(4, 1, 1, 'Comercial', 'COM', 'ativo')
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Áreas
-- ============================
INSERT INTO people_flow_rh.area (id, codigo, nome, descricao) VALUES
(1, 'RH',  'Recursos Humanos',        'Gestão de pessoas e processos de RH'),
(2, 'TI',  'Tecnologia da Informação','Desenvolvimento, infraestrutura e suporte de TI'),
(3, 'FIN', 'Financeiro',              'Controladoria, contabilidade e financeiro'),
(4, 'COM', 'Comercial',               'Vendas, marketing e relacionamento com clientes')
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Níveis Hierárquicos
-- ============================
INSERT INTO people_flow_rh.nivel_hierarquico (id, nome, descricao, ordem) VALUES
(1, 'Júnior',      'Profissional em início de carreira',               1),
(2, 'Pleno',       'Profissional com experiência intermediária',        2),
(3, 'Sênior',      'Profissional com ampla experiência e autonomia',   3),
(4, 'Especialista','Profissional com expertise técnica aprofundada',   4),
(5, 'Coordenador', 'Liderança de equipes operacionais',                5),
(6, 'Gerente',     'Gestão estratégica de área ou departamento',       6)
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Famílias de Cargo
-- ============================
INSERT INTO people_flow_rh.familia_cargo (id, nome, descricao) VALUES
(1, 'Gestão de Pessoas',   'Cargos relacionados a RH, recrutamento e desenvolvimento'),
(2, 'Gestão e Liderança',  'Cargos de gestão e coordenação de equipes'),
(3, 'Tecnologia',          'Cargos de desenvolvimento, dados e infraestrutura'),
(4, 'Administração',       'Cargos administrativos e de suporte operacional')
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Cargos de exemplo (necessários para cargo_role)
-- ============================
INSERT INTO people_flow_rh.cargo (id, codigo, nome, descricao, nivel_hierarquico_id, familia_cargo_id, departamento_id) VALUES
(1, 'GER-RH-SR',  'Gerente de RH',              'Gerente da área de Recursos Humanos',      6, 1, 1),
(2, 'ANA-RH-PL',  'Analista de RH',             'Analista de Recursos Humanos',             2, 1, 1),
(3, 'ASS-RH-JR',  'Assistente de RH',           'Assistente administrativo de RH',          1, 1, 1),
(4, 'GES-ARE-SR', 'Gestor de Área',             'Gestor de equipe/departamento',            5, 2, NULL),
(5, 'COL-PAD-JR', 'Colaborador Padrão',         'Colaborador operacional',                  1, 4, NULL),
(6, 'ADM-SIS-SR', 'Administrador de Sistema',   'Acesso administrativo completo ao sistema',3, 3, 2)
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Jornadas de trabalho
-- ============================
INSERT INTO people_flow_rh.jornada_trabalho (id, descricao, carga_semanal_horas) VALUES
(1, 'Integral padrão 44h semanais', 44.00),
(2, 'Parcial 30h semanais', 30.00)
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

-- Permissões de níveis hierárquicos de cargo
(1, 'nivel_hierarquico:criar', 'Criar níveis hierárquicos de cargo'),
(1, 'nivel_hierarquico:ler', 'Consultar níveis hierárquicos de cargo'),
(1, 'nivel_hierarquico:editar', 'Editar níveis hierárquicos de cargo'),
(1, 'nivel_hierarquico:excluir', 'Excluir níveis hierárquicos de cargo'),

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
(6, '*:*', 'Todas as permissões'),

(6, 'nivel_hierarquico:criar', 'Criar níveis hierárquicos de cargo'),
(6, 'nivel_hierarquico:ler', 'Consultar níveis hierárquicos de cargo'),
(6, 'nivel_hierarquico:editar', 'Editar níveis hierárquicos de cargo'),
(6, 'nivel_hierarquico:excluir', 'Excluir níveis hierárquicos de cargo')
ON CONFLICT (cargo_id, role_name) DO NOTHING;

-- ============================
-- SEED DATA: Colaboradores (empresa 1, departamento, centro_custo, cargo)
-- ============================
INSERT INTO people_flow_rh.colaborador (id, empresa_id, departamento_id, centro_custo_id, nome, cpf, matricula, email, data_admissao, status, cargo_id) VALUES
(1, 1, 1, 2, 'Maria Silva', '529.982.247-25', '001', 'maria.silva@peopleflow.com', '2023-01-15', 'ativo', 1),
(2, 1, 1, 2, 'João Santos', '734.767.604-56', '002', 'joao.santos@peopleflow.com', '2023-03-01', 'ativo', 2),
(3, 1, 1, 2, 'Ana Costa', '048.129.384-08', '003', 'ana.costa@peopleflow.com', '2023-06-10', 'ativo', 3),
(4, 1, 2, 3, 'Pedro Oliveira', '423.738.230-39', '004', 'pedro.oliveira@peopleflow.com', '2023-02-20', 'ativo', 4),
(5, 1, 2, 3, 'Carla Lima', '576.788.234-79', '005', 'carla.lima@peopleflow.com', '2023-04-01', 'ativo', 5),
(6, 1, 3, 1, 'Roberto Alves', '123.456.789-00', '006', 'roberto.alves@peopleflow.com', '2022-11-01', 'ativo', 6),
(7, 1, 4, 1, 'Fernanda Souza', '371.498.283-37', '007', 'fernanda.souza@peopleflow.com', '2024-01-08', 'ativo', 5)
ON CONFLICT (id) DO NOTHING;

-- ============================
-- SEED DATA: Contratos (colaborador, jornada, cargo)
-- ============================
INSERT INTO people_flow_rh.contrato (id, colaborador_id, jornada_id, cargo_id, tipo, regime, salario_base, inicio, fim) VALUES
(1, 1, 1, 1, 'CLT', 'integral', 15000.00, '2023-01-15', NULL),
(2, 2, 1, 2, 'CLT', 'integral', 9800.00, '2023-03-01', NULL),
(3, 3, 2, 3, 'CLT', 'parcial', 5500.00, '2023-06-10', NULL),
(4, 4, 1, 4, 'CLT', 'integral', 8800.00, '2023-02-20', NULL),
(5, 5, 1, 5, 'CLT', 'integral', 4200.00, '2023-04-01', NULL),
(6, 6, 1, 6, 'CLT', 'integral', 18000.00, '2022-11-01', NULL),
(7, 7, 1, 5, 'CLT', 'integral', 4100.00, '2024-01-08', NULL)
ON CONFLICT (id) DO NOTHING;

-- Ajusta sequences para o próximo ID após os seeds (evita conflito em novas inserções)
SELECT setval('people_flow_rh.empresa_id_seq',           (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.empresa));
SELECT setval('people_flow_rh.unidade_id_seq',           (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.unidade));
SELECT setval('people_flow_rh.centro_custo_id_seq',      (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.centro_custo));
SELECT setval('people_flow_rh.departamento_id_seq',      (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.departamento));
SELECT setval('people_flow_rh.colaborador_id_seq',       (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.colaborador));
SELECT setval('people_flow_rh.area_id_seq',              (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.area));
SELECT setval('people_flow_rh.nivel_hierarquico_id_seq', (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.nivel_hierarquico));
SELECT setval('people_flow_rh.familia_cargo_id_seq',     (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.familia_cargo));
SELECT setval('people_flow_rh.cargo_id_seq',             (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.cargo));
SELECT setval('people_flow_rh.jornada_trabalho_id_seq',  (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.jornada_trabalho));
SELECT setval('people_flow_rh.contrato_id_seq',          (SELECT COALESCE(MAX(id), 1) FROM people_flow_rh.contrato));

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

