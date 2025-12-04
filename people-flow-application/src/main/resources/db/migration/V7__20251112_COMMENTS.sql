-- ============================
-- DOCUMENTAÇÃO - COMMENTS
-- ============================
--
-- Este arquivo adiciona documentação inline ao schema através de COMMENTS.
-- Facilita a manutenção e compreensão da estrutura do banco.
--

-- ============================
-- ORGANIZAÇÃO / NÚCLEO
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.CLIENTE IS 
'Tabela principal de clientes multi-tenant. Representa empresas/organizações que utilizam o sistema.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CLIENTE.CNPJ IS 'CNPJ no formato 99.999.999/9999-99';
COMMENT ON COLUMN PEOPLE_FLOW_RH.CLIENTE.STATUS IS 'Status do cliente: ativo, inativo, suspenso';

COMMENT ON TABLE PEOPLE_FLOW_RH.EMPRESA IS 
'Empresas pertencentes a um cliente. Um cliente pode ter múltiplas empresas (holdings, filiais).';

COMMENT ON COLUMN PEOPLE_FLOW_RH.EMPRESA.CNPJ IS 'CNPJ da empresa no formato 99.999.999/9999-99';
COMMENT ON COLUMN PEOPLE_FLOW_RH.EMPRESA.INSCRICAO_ESTADUAL IS 'Inscrição estadual da empresa';

COMMENT ON TABLE PEOPLE_FLOW_RH.UNIDADE IS 
'Unidades/filiais de uma empresa. Representa locais físicos ou operacionais.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.UNIDADE.CODIGO IS 'Código interno da unidade';

COMMENT ON TABLE PEOPLE_FLOW_RH.DEPARTAMENTO IS 
'Departamentos organizacionais. Podem estar vinculados a uma unidade específica ou serem corporativos.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.DEPARTAMENTO.UNIDADE_ID IS 'Unidade à qual o departamento pertence (NULL = departamento corporativo)';

COMMENT ON TABLE PEOPLE_FLOW_RH.CENTRO_CUSTO IS 
'Centros de custo para controle financeiro e contábil.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CENTRO_CUSTO.CODIGO IS 'Código do centro de custo (único por cliente)';

-- ============================
-- NOTA: Tabela USUARIO foi removida
-- Dados de usuários são gerenciados 100% pelo Keycloak
-- Colunas *_USUARIO_ID armazenam UUID do Keycloak diretamente (sem FK)
-- ============================
-- PESSOAS / COLABORADORES
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.COLABORADOR IS 
'Cadastro principal de colaboradores/funcionários da empresa.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR.CPF IS 'CPF no formato 999.999.999-99';
COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR.MATRICULA IS 'Matrícula única por empresa';
COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR.DATA_ADMISSAO IS 'Data de admissão do colaborador';
COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR.DATA_DEMISSAO IS 'Data de demissão (NULL se ativo)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR.STATUS IS 'Status: ativo, inativo, afastado, demitido, aposentado';

COMMENT ON TABLE PEOPLE_FLOW_RH.DEPENDENTE IS 
'Dependentes dos colaboradores para fins de benefícios e imposto de renda.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.DEPENDENTE.PARENTESCO IS 'Tipo de parentesco: conjuge, filho, filha, pai, mae, outro';
COMMENT ON COLUMN PEOPLE_FLOW_RH.DEPENDENTE.CPF IS 'CPF do dependente no formato 999.999.999-99';

COMMENT ON TABLE PEOPLE_FLOW_RH.CONTA_BANCARIA IS 
'Dados bancários dos colaboradores para pagamento.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CONTA_BANCARIA.TIPO IS 'Tipo de conta: corrente, poupanca, salario';
COMMENT ON COLUMN PEOPLE_FLOW_RH.CONTA_BANCARIA.PIX IS 'Chave PIX para pagamento';

-- ============================
-- JORNADA & CARGOS
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.JORNADA_TRABALHO IS 
'Jornadas de trabalho disponíveis (40h, 44h, 20h, etc).';

COMMENT ON COLUMN PEOPLE_FLOW_RH.JORNADA_TRABALHO.CARGA_SEMANAL_HORAS IS 'Carga horária semanal em horas';

COMMENT ON TABLE PEOPLE_FLOW_RH.CARGO IS 
'Cargos disponíveis na organização.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CARGO.NIVEL IS 'Nível hierárquico do cargo (ex: junior, pleno, senior)';

COMMENT ON TABLE PEOPLE_FLOW_RH.FAIXA_SALARIAL IS 
'Faixas salariais definidas para cada cargo.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.FAIXA_SALARIAL.FAIXA_MIN IS 'Valor mínimo da faixa salarial';
COMMENT ON COLUMN PEOPLE_FLOW_RH.FAIXA_SALARIAL.FAIXA_MAX IS 'Valor máximo da faixa salarial';
COMMENT ON COLUMN PEOPLE_FLOW_RH.FAIXA_SALARIAL.MOEDA IS 'Moeda (BRL, USD, EUR)';

COMMENT ON TABLE PEOPLE_FLOW_RH.COLABORADOR_CARGO IS 
'Histórico de cargos dos colaboradores. Permite rastrear promoções e mudanças.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR_CARGO.INICIO IS 'Data de início no cargo';
COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR_CARGO.FIM IS 'Data de fim no cargo (NULL se atual)';

-- ============================
-- CONTRATOS
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.CONTRATO IS 
'Contratos de trabalho dos colaboradores. Pode haver múltiplos contratos (renovações, mudanças).';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CONTRATO.TIPO IS 'Tipo: CLT, PJ, estagio, temporario, intermitente';
COMMENT ON COLUMN PEOPLE_FLOW_RH.CONTRATO.REGIME IS 'Regime: integral, parcial, horista';
COMMENT ON COLUMN PEOPLE_FLOW_RH.CONTRATO.SALARIO_BASE IS 'Salário base do contrato';

-- ============================
-- PONTO
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.MARCACAO_PONTO IS 
'Registro de marcações de ponto dos colaboradores. Alto volume de dados.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.MARCACAO_PONTO.DATA_HORA IS 'Data e hora da marcação';
COMMENT ON COLUMN PEOPLE_FLOW_RH.MARCACAO_PONTO.TIPO IS 'Tipo: entrada, saida, entrada_intervalo, saida_intervalo';
COMMENT ON COLUMN PEOPLE_FLOW_RH.MARCACAO_PONTO.ORIGEM IS 'Origem: kiosk, mobile, web, import, manual';
COMMENT ON COLUMN PEOPLE_FLOW_RH.MARCACAO_PONTO.LATITUDE IS 'Latitude da geolocalização da marcação';
COMMENT ON COLUMN PEOPLE_FLOW_RH.MARCACAO_PONTO.LONGITUDE IS 'Longitude da geolocalização da marcação';

COMMENT ON TABLE PEOPLE_FLOW_RH.CALCULO_PONTO_DIA IS 
'Cálculo consolidado do ponto por dia. Armazena horas trabalhadas, extras, banco de horas.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CALCULO_PONTO_DIA.HORAS_TRABALHADAS IS 'Total de horas trabalhadas no dia';
COMMENT ON COLUMN PEOPLE_FLOW_RH.CALCULO_PONTO_DIA.HORAS_EXTRAS IS 'Total de horas extras no dia';
COMMENT ON COLUMN PEOPLE_FLOW_RH.CALCULO_PONTO_DIA.BANCO_HORAS_SALDO IS 'Saldo do banco de horas';

COMMENT ON TABLE PEOPLE_FLOW_RH.AUSENCIA IS 
'Registro de ausências dos colaboradores (faltas, atestados, licenças).';

COMMENT ON COLUMN PEOPLE_FLOW_RH.AUSENCIA.TIPO IS 'Tipo: atestado, falta, falta_justificada, licenca, suspensao';

-- ============================
-- FÉRIAS
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.FERIAS_SOLICITACAO IS 
'Solicitações de férias dos colaboradores.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.FERIAS_SOLICITACAO.ABONO IS 'Indica se há abono pecuniário (venda de férias)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.FERIAS_SOLICITACAO.STATUS IS 'Status: pendente, aprovada, reprovada, cancelada';

COMMENT ON TABLE PEOPLE_FLOW_RH.FERIAS_APROVACAO IS 
'Histórico de aprovações/reprovações de férias. Permite múltiplos níveis.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.FERIAS_APROVACAO.STATUS IS 'Status: aprovado, reprovado';

COMMENT ON TABLE PEOPLE_FLOW_RH.FERIAS_SALDO IS 
'Saldo de férias dos colaboradores.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.FERIAS_SALDO.DIAS_AQUISITIVOS IS 'Dias adquiridos no período aquisitivo';
COMMENT ON COLUMN PEOPLE_FLOW_RH.FERIAS_SALDO.DIAS_DISPONIVEIS IS 'Dias disponíveis para uso';

-- ============================
-- BENEFÍCIOS
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.PLANO_BENEFICIO IS 
'Planos de benefícios oferecidos pela empresa (VT, VR, Saúde, etc).';

COMMENT ON COLUMN PEOPLE_FLOW_RH.PLANO_BENEFICIO.TIPO IS 'Tipo: VT, VR, VA, saude, odonto, seguro_vida, gym, outro';

COMMENT ON TABLE PEOPLE_FLOW_RH.ADESAO_BENEFICIO IS 
'Adesão dos colaboradores aos planos de benefícios.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.ADESAO_BENEFICIO.CUSTO_MENSAL IS 'Custo total mensal do benefício';
COMMENT ON COLUMN PEOPLE_FLOW_RH.ADESAO_BENEFICIO.COTA_COLABORADOR IS 'Valor descontado do colaborador';

-- ============================
-- RECRUTAMENTO
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.VAGA IS 
'Vagas abertas para recrutamento e seleção.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.VAGA.REMOTO IS 'Indica se a vaga permite trabalho remoto';
COMMENT ON COLUMN PEOPLE_FLOW_RH.VAGA.STATUS IS 'Status: aberta, em_andamento, suspensa, fechada, cancelada';

COMMENT ON TABLE PEOPLE_FLOW_RH.CANDIDATO IS 
'Cadastro de candidatos para processos seletivos.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CANDIDATO.CONSENTIMENTO_LGPD IS 'Consentimento para tratamento de dados pessoais (LGPD)';

COMMENT ON TABLE PEOPLE_FLOW_RH.CANDIDATURA IS 
'Candidaturas dos candidatos às vagas. Relaciona candidato com vaga.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CANDIDATURA.ETAPA_ATUAL IS 'Etapa atual do processo seletivo';
COMMENT ON COLUMN PEOPLE_FLOW_RH.CANDIDATURA.STATUS IS 'Status: em_analise, triagem, entrevista, aprovado, reprovado, desistente';

COMMENT ON TABLE PEOPLE_FLOW_RH.ENTREVISTA IS 
'Registro de entrevistas realizadas com os candidatos.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.ENTREVISTA.TIPO IS 'Tipo: triagem, tecnica, comportamental, final, online, presencial';
COMMENT ON COLUMN PEOPLE_FLOW_RH.ENTREVISTA.RESULTADO IS 'Resultado: aprovado, reprovado, pendente';

-- ============================
-- DESEMPENHO
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.CICLO_DESEMPENHO IS 
'Ciclos de avaliação de desempenho (ex: anual, semestral).';

COMMENT ON TABLE PEOPLE_FLOW_RH.AVALIACAO_DESEMPENHO IS 
'Avaliações de desempenho dos colaboradores.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.AVALIACAO_DESEMPENHO.NOTA IS 'Nota da avaliação (0 a 10)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.AVALIACAO_DESEMPENHO.STATUS IS 'Status: rascunho, em_avaliacao, concluida, cancelada';

-- ============================
-- OKR
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.OKR IS 
'Objectives and Key Results. Metas estratégicas da organização, equipes ou indivíduos.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.OKR.TIPO IS 'Tipo: organizacional, equipe, pessoal';

COMMENT ON TABLE PEOPLE_FLOW_RH.OKR_RESULTADO_CHAVE IS 
'Resultados-chave (Key Results) de um OKR. Métricas mensuráveis.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.OKR_RESULTADO_CHAVE.UNIDADE IS 'Unidade de medida: percentual, numero, moeda, binario';
COMMENT ON COLUMN PEOPLE_FLOW_RH.OKR_RESULTADO_CHAVE.VALOR_ALVO IS 'Valor alvo a ser atingido';
COMMENT ON COLUMN PEOPLE_FLOW_RH.OKR_RESULTADO_CHAVE.VALOR_ATUAL IS 'Valor atual/progresso';

COMMENT ON TABLE PEOPLE_FLOW_RH.OKR_CHECKIN IS 
'Check-ins de progresso dos OKRs. Registro temporal de avanço.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.OKR_CHECKIN.REALIZADO_EM IS 'Data/hora do check-in';
COMMENT ON COLUMN PEOPLE_FLOW_RH.OKR_CHECKIN.VALOR IS 'Valor registrado no check-in';

-- ============================
-- PDI & COMPETÊNCIAS
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.PDI IS 
'Plano de Desenvolvimento Individual dos colaboradores.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.PDI.ACOES IS 'Ações planejadas em formato JSONB';
COMMENT ON COLUMN PEOPLE_FLOW_RH.PDI.STATUS IS 'Status: aberto, em_andamento, concluido, cancelado';

COMMENT ON TABLE PEOPLE_FLOW_RH.COMPETENCIA_CARGO IS 
'Competências esperadas para cada cargo.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.COMPETENCIA_CARGO.NIVEL_ESPERADO IS 'Nível esperado da competência (1 a 5)';

-- ============================
-- REEMBOLSOS
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.POLITICA_REEMBOLSO IS 
'Políticas de reembolso da empresa por categoria de despesa.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.POLITICA_REEMBOLSO.CATEGORIA IS 'Categoria: alimentacao, transporte, hospedagem, material, treinamento, saude, outro';
COMMENT ON COLUMN PEOPLE_FLOW_RH.POLITICA_REEMBOLSO.VALOR_MAX_POR_DIA IS 'Valor máximo permitido por dia';

COMMENT ON TABLE PEOPLE_FLOW_RH.REEMBOLSO_SOLICITACAO IS 
'Solicitações de reembolso dos colaboradores.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.REEMBOLSO_SOLICITACAO.STATUS IS 'Status: pendente, em_analise, aprovado, reprovado, pago, cancelado';

COMMENT ON TABLE PEOPLE_FLOW_RH.REEMBOLSO_ITEM IS 
'Itens/despesas de uma solicitação de reembolso.';

COMMENT ON TABLE PEOPLE_FLOW_RH.REEMBOLSO_APROVACAO IS 
'Histórico de aprovações de reembolsos.';

-- ============================
-- FOLHA DE PAGAMENTO
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.RUBRICA IS 
'Rubricas da folha de pagamento (salário, INSS, FGTS, vale-transporte, etc).';

COMMENT ON COLUMN PEOPLE_FLOW_RH.RUBRICA.TIPO IS 'Tipo: provento (crédito) ou desconto (débito)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.RUBRICA.BASE_CALCULO IS 'Regras de cálculo em formato JSONB';

COMMENT ON TABLE PEOPLE_FLOW_RH.FOLHA_EXECUCAO IS 
'Execuções mensais da folha de pagamento.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.FOLHA_EXECUCAO.COMPETENCIA IS 'Mês/ano da folha (sempre dia 1)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.FOLHA_EXECUCAO.STATUS IS 'Status: aberta, em_calculo, calculada, em_aprovacao, aprovada, fechada';

COMMENT ON TABLE PEOPLE_FLOW_RH.FOLHA_ITEM IS 
'Itens individuais da folha (uma linha por colaborador por rubrica).';

COMMENT ON COLUMN PEOPLE_FLOW_RH.FOLHA_ITEM.QUANTIDADE IS 'Quantidade (ex: dias, horas)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.FOLHA_ITEM.VALOR IS 'Valor calculado do item';

-- ============================
-- ESOCIAL
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.EVENTO_ESOCIAL IS 
'Eventos do eSocial gerados pelo sistema.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.EVENTO_ESOCIAL.TIPO IS 'Tipo do evento eSocial (S-1000, S-2200, S-1200, etc)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.EVENTO_ESOCIAL.REFERENCIA_ID IS 'ID da entidade que originou o evento';
COMMENT ON COLUMN PEOPLE_FLOW_RH.EVENTO_ESOCIAL.STATUS IS 'Status: gerado, enviado, processado, erro, rejeitado';

COMMENT ON TABLE PEOPLE_FLOW_RH.PAYLOAD_ESOCIAL IS 
'Payload XML dos eventos eSocial enviados.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.PAYLOAD_ESOCIAL.XML IS 'XML do evento eSocial';
COMMENT ON COLUMN PEOPLE_FLOW_RH.PAYLOAD_ESOCIAL.RECIBO IS 'Recibo retornado pelo eSocial';
COMMENT ON COLUMN PEOPLE_FLOW_RH.PAYLOAD_ESOCIAL.PROTOCOLO IS 'Protocolo de envio';

-- ============================
-- WORKFLOW
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.DEFINICAO_WORKFLOW IS 
'Definições de workflows do sistema (aprovações, processos).';

COMMENT ON COLUMN PEOPLE_FLOW_RH.DEFINICAO_WORKFLOW.MODELO IS 'Modelo do workflow em formato JSONB';
COMMENT ON COLUMN PEOPLE_FLOW_RH.DEFINICAO_WORKFLOW.VERSAO IS 'Versão da definição';

COMMENT ON TABLE PEOPLE_FLOW_RH.INSTANCIA_WORKFLOW IS 
'Instâncias em execução de workflows.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.INSTANCIA_WORKFLOW.ENTIDADE IS 'Tipo da entidade (ex: ferias_solicitacao)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.INSTANCIA_WORKFLOW.ENTIDADE_ID IS 'ID da entidade vinculada';
COMMENT ON COLUMN PEOPLE_FLOW_RH.INSTANCIA_WORKFLOW.CONTEXTO IS 'Dados de contexto em formato JSONB';

COMMENT ON TABLE PEOPLE_FLOW_RH.TAREFA_WORKFLOW IS 
'Tarefas pendentes de um workflow.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.TAREFA_WORKFLOW.ETAPA IS 'Nome da etapa do workflow';
COMMENT ON COLUMN PEOPLE_FLOW_RH.TAREFA_WORKFLOW.STATUS IS 'Status: aberta, em_andamento, concluida, cancelada';

COMMENT ON TABLE PEOPLE_FLOW_RH.LOG_WORKFLOW IS 
'Log de execução dos workflows. Alto volume de dados.';

-- ============================
-- AUDITORIA
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.EVENTO_AUDITORIA IS 
'Log de auditoria de todas as operações do sistema. Alto volume de dados.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.EVENTO_AUDITORIA.ENTIDADE IS 'Nome da tabela/entidade';
COMMENT ON COLUMN PEOPLE_FLOW_RH.EVENTO_AUDITORIA.ENTIDADE_ID IS 'ID do registro afetado';
COMMENT ON COLUMN PEOPLE_FLOW_RH.EVENTO_AUDITORIA.ACAO IS 'Ação realizada: create, update, delete';
COMMENT ON COLUMN PEOPLE_FLOW_RH.EVENTO_AUDITORIA.DADOS IS 'Dados da operação em formato JSONB';

-- ============================
-- NOTIFICAÇÕES
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.TEMPLATE_NOTIFICACAO IS 
'Templates de notificações do sistema (emails, push, in-app).';

COMMENT ON COLUMN PEOPLE_FLOW_RH.TEMPLATE_NOTIFICACAO.CANAL IS 'Canal: email, push, inapp';
COMMENT ON COLUMN PEOPLE_FLOW_RH.TEMPLATE_NOTIFICACAO.VARIAVEIS IS 'Variáveis do template em formato JSONB';
COMMENT ON COLUMN PEOPLE_FLOW_RH.TEMPLATE_NOTIFICACAO.VERSAO IS 'Versão do template';

COMMENT ON TABLE PEOPLE_FLOW_RH.NOTIFICACAO_ENVIO IS 
'Registro de notificações enviadas. Alto volume de dados.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.NOTIFICACAO_ENVIO.CORPO_RENDERIZADO IS 'Corpo da notificação com variáveis substituídas';
COMMENT ON COLUMN PEOPLE_FLOW_RH.NOTIFICACAO_ENVIO.CONTEXTO IS 'Contexto/variáveis em formato JSONB';
COMMENT ON COLUMN PEOPLE_FLOW_RH.NOTIFICACAO_ENVIO.STATUS IS 'Status: pendente, enviado, erro';

COMMENT ON TABLE PEOPLE_FLOW_RH.DOCUMENTO_COLABORADOR IS 
'Documentos digitais dos colaboradores.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.DOCUMENTO_COLABORADOR.TIPO IS 'Tipo: contrato, atestado, exame, certidao, comprovante, documento, outro';
COMMENT ON COLUMN PEOPLE_FLOW_RH.DOCUMENTO_COLABORADOR.STORAGE_KEY IS 'Chave no storage (S3, etc)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.DOCUMENTO_COLABORADOR.VERSAO IS 'Versão do documento';

-- ============================
-- SAÚDE OCUPACIONAL
-- ============================

COMMENT ON TABLE PEOPLE_FLOW_RH.ASO IS 
'Atestados de Saúde Ocupacional (ASO) dos colaboradores.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.ASO.TIPO IS 'Tipo: admissional, periodico, retorno, mudanca, demissional';
COMMENT ON COLUMN PEOPLE_FLOW_RH.ASO.VALIDADE_ATE IS 'Data de validade do ASO';

COMMENT ON TABLE PEOPLE_FLOW_RH.EXAME_AGENDAMENTO IS 
'Agendamento de exames ocupacionais.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.EXAME_AGENDAMENTO.TIPO_EXAME IS 'Tipo do exame (ex: admissional, audiometria, acuidade visual)';
COMMENT ON COLUMN PEOPLE_FLOW_RH.EXAME_AGENDAMENTO.COMPARECEU IS 'Indica se o colaborador compareceu';
COMMENT ON COLUMN PEOPLE_FLOW_RH.EXAME_AGENDAMENTO.STATUS IS 'Status: agendado, realizado, faltou, cancelado';

-- ============================
-- KEYCLOAK ACCESS CONTROL
-- ============================

COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR.KEYCLOAK_USER_ID IS 
'UUID do usuário correspondente no Keycloak. Vínculo para SSO e controle de acesso.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR.CRIADO_POR IS 
'Username do usuário que criou o registro (preferred_username do Keycloak)';

COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR.ATUALIZADO_POR IS 
'Username do usuário que atualizou o registro (preferred_username do Keycloak)';

COMMENT ON COLUMN PEOPLE_FLOW_RH.COLABORADOR.EXCLUIDO_POR IS 
'Username do usuário que excluiu o registro - soft delete (preferred_username do Keycloak)';

COMMENT ON TABLE PEOPLE_FLOW_RH.CARGO_ROLE IS 
'Mapeamento entre cargos e roles do Keycloak. Usado para atribuição automática de permissões quando colaborador é criado.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CARGO_ROLE.CARGO_ID IS 
'ID do cargo';

COMMENT ON COLUMN PEOPLE_FLOW_RH.CARGO_ROLE.ROLE_NAME IS 
'Nome da role no formato recurso:acao (ex: colaborador:criar, folha:aprovar)';

COMMENT ON TABLE PEOPLE_FLOW_RH.DEPARTAMENTO_GRUPO IS 
'Mapeamento entre departamentos e grupos do Keycloak. Usado para organização automática de usuários por departamento.';

COMMENT ON COLUMN PEOPLE_FLOW_RH.DEPARTAMENTO_GRUPO.DEPARTAMENTO_ID IS 
'ID do departamento';

COMMENT ON COLUMN PEOPLE_FLOW_RH.DEPARTAMENTO_GRUPO.KEYCLOAK_GROUP_ID IS 
'UUID do grupo correspondente no Keycloak';

COMMENT ON COLUMN PEOPLE_FLOW_RH.DEPARTAMENTO_GRUPO.KEYCLOAK_GROUP_NAME IS 
'Nome do grupo no Keycloak (para referência)';

