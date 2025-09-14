-- ============================
-- ÍNDICES ÚNICOS DE INTEGRIDADE
-- ============================

-- Colaborador
CREATE UNIQUE INDEX IF NOT EXISTS uq_colaborador_cpf
    ON public.colaborador (cliente_id, cpf);

CREATE UNIQUE INDEX IF NOT EXISTS uq_colaborador_matricula
    ON public.colaborador (empresa_id, matricula);

-- Empresa
CREATE UNIQUE INDEX IF NOT EXISTS uq_empresa_cnpj
    ON public.empresa (cliente_id, cnpj);

-- Usuário
CREATE UNIQUE INDEX IF NOT EXISTS uq_usuario_email
    ON public.usuario (cliente_id, email);

-- Candidato
CREATE UNIQUE INDEX IF NOT EXISTS uq_candidato_cpf
    ON public.candidato (cliente_id, cpf);

-- ============================
-- ÍNDICES PARA BUSCA (FKs)
-- ============================

-- Organização
CREATE INDEX IF NOT EXISTS idx_empresa_cliente ON public.empresa(cliente_id);
CREATE INDEX IF NOT EXISTS idx_unidade_empresa ON public.unidade(empresa_id);
CREATE INDEX IF NOT EXISTS idx_departamento_empresa ON public.departamento(empresa_id);
CREATE INDEX IF NOT EXISTS idx_departamento_unidade ON public.departamento(unidade_id);

-- Segurança
CREATE INDEX IF NOT EXISTS idx_usuario_cliente ON public.usuario(cliente_id);
CREATE INDEX IF NOT EXISTS idx_usuario_email ON public.usuario(email);

-- Pessoas / Contratos
CREATE INDEX IF NOT EXISTS idx_colab_empresa ON public.colaborador(empresa_id);
CREATE INDEX IF NOT EXISTS idx_colab_departamento ON public.colaborador(departamento_id);
CREATE INDEX IF NOT EXISTS idx_colab_cc ON public.colaborador(centro_custo_id);

CREATE INDEX IF NOT EXISTS idx_dependente_colab ON public.dependente(colaborador_id);
CREATE INDEX IF NOT EXISTS idx_cb_colab ON public.conta_bancaria(colaborador_id);

CREATE INDEX IF NOT EXISTS idx_contrato_colab ON public.contrato(colaborador_id);
CREATE INDEX IF NOT EXISTS idx_contrato_cargo ON public.contrato(cargo_id);

CREATE INDEX IF NOT EXISTS idx_cc_colab ON public.colaborador_cargo(colaborador_id);
CREATE INDEX IF NOT EXISTS idx_cc_cargo ON public.colaborador_cargo(cargo_id);

-- Ponto
CREATE INDEX IF NOT EXISTS idx_mp_colab_data ON public.marcacao_ponto(colaborador_id, data_hora);
CREATE INDEX IF NOT EXISTS idx_cpd_colab_data ON public.calculo_ponto_dia(colaborador_id, data);
CREATE INDEX IF NOT EXISTS idx_aus_colab_data ON public.ausencia(colaborador_id, inicio, fim);

-- Férias
CREATE INDEX IF NOT EXISTS idx_fs_colab ON public.ferias_solicitacao(colaborador_id);
CREATE INDEX IF NOT EXISTS idx_fa_sol ON public.ferias_aprovacao(solicitacao_id);

-- Benefícios
CREATE INDEX IF NOT EXISTS idx_ab_colab ON public.adesao_beneficio(colaborador_id);

-- Recrutamento
CREATE INDEX IF NOT EXISTS idx_candd_vaga ON public.candidatura(vaga_id);
CREATE INDEX IF NOT EXISTS idx_candd_cand ON public.candidatura(candidato_id);
CREATE INDEX IF NOT EXISTS idx_ent_candd ON public.entrevista(candidatura_id);

-- Desempenho / OKR
CREATE INDEX IF NOT EXISTS idx_ad_colab ON public.avaliacao_desempenho(colaborador_id);
CREATE INDEX IF NOT EXISTS idx_okrrc_okr ON public.okr_resultado_chave(okr_id);
CREATE INDEX IF NOT EXISTS idx_okrc_resultado ON public.okr_checkin(resultado_chave_id);

CREATE INDEX IF NOT EXISTS idx_pdi_colab ON public.pdi(colaborador_id);
CREATE INDEX IF NOT EXISTS idx_comp_cargo ON public.competencia_cargo(cargo_id);

-- Reembolsos
CREATE INDEX IF NOT EXISTS idx_rs_colab ON public.reembolso_solicitacao(colaborador_id);
CREATE INDEX IF NOT EXISTS idx_ri_sol ON public.reembolso_item(solicitacao_id);

-- Folha
CREATE INDEX IF NOT EXISTS idx_fi_execucao ON public.folha_item(execucao_id);
CREATE INDEX IF NOT EXISTS idx_fi_colab ON public.folha_item(colaborador_id);

-- eSocial
CREATE INDEX IF NOT EXISTS idx_pesocial_evento ON public.payload_esocial(evento_id);

-- Workflow
CREATE INDEX IF NOT EXISTS idx_iw_definicao ON public.instancia_workflow(definicao_id);
CREATE INDEX IF NOT EXISTS idx_tw_instancia ON public.tarefa_workflow(instancia_id);
CREATE INDEX IF NOT EXISTS idx_lw_instancia ON public.log_workflow(instancia_id);

-- Auditoria
CREATE INDEX IF NOT EXISTS idx_ea_usuario ON public.evento_auditoria(usuario_id);

-- Notificações
CREATE INDEX IF NOT EXISTS idx_ne_template ON public.notificacao_envio(template_id);
CREATE INDEX IF NOT EXISTS idx_ne_usuario ON public.notificacao_envio(usuario_id);
CREATE INDEX IF NOT EXISTS idx_ne_colab ON public.notificacao_envio(colaborador_id);

-- Saúde Ocupacional
CREATE INDEX IF NOT EXISTS idx_aso_colab ON public.aso(colaborador_id);
CREATE INDEX IF NOT EXISTS idx_exame_colab ON public.exame_agendamento(colaborador_id);

