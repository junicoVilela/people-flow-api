-- ============================
-- ORGANIZAÇÃO / NÚCLEO
-- ============================
ALTER TABLE public.empresa
  ADD CONSTRAINT fk_empresa_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.unidade
  ADD CONSTRAINT fk_unidade_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_unidade_empresa FOREIGN KEY (empresa_id) REFERENCES public.empresa(id);

ALTER TABLE public.departamento
  ADD CONSTRAINT fk_departamento_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_departamento_empresa FOREIGN KEY (empresa_id) REFERENCES public.empresa(id),
  ADD CONSTRAINT fk_departamento_unidade FOREIGN KEY (unidade_id) REFERENCES public.unidade(id);

ALTER TABLE public.centro_custo
  ADD CONSTRAINT fk_cc_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

-- ============================
-- SEGURANÇA / RBAC
-- ============================
ALTER TABLE public.usuario
  ADD CONSTRAINT fk_usuario_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.papel
  ADD CONSTRAINT fk_papel_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.permissao
  ADD CONSTRAINT fk_permissao_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.usuario_papel
  ADD CONSTRAINT fk_up_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_up_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id),
  ADD CONSTRAINT fk_up_papel FOREIGN KEY (papel_id) REFERENCES public.papel(id);

ALTER TABLE public.papel_permissao
  ADD CONSTRAINT fk_pp_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_pp_papel FOREIGN KEY (papel_id) REFERENCES public.papel(id),
  ADD CONSTRAINT fk_pp_permissao FOREIGN KEY (permissao_id) REFERENCES public.permissao(id);

-- ============================
-- PESSOAS / CONTRATOS
-- ============================
ALTER TABLE public.colaborador
  ADD CONSTRAINT fk_colab_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_colab_empresa FOREIGN KEY (empresa_id) REFERENCES public.empresa(id),
  ADD CONSTRAINT fk_colab_departamento FOREIGN KEY (departamento_id) REFERENCES public.departamento(id),
  ADD CONSTRAINT fk_colab_cc FOREIGN KEY (centro_custo_id) REFERENCES public.centro_custo(id);

ALTER TABLE public.dependente
  ADD CONSTRAINT fk_dep_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_dep_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

ALTER TABLE public.conta_bancaria
  ADD CONSTRAINT fk_cb_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_cb_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

ALTER TABLE public.jornada_trabalho
  ADD CONSTRAINT fk_jornada_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.cargo
  ADD CONSTRAINT fk_cargo_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.faixa_salarial
  ADD CONSTRAINT fk_faixa_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_faixa_cargo FOREIGN KEY (cargo_id) REFERENCES public.cargo(id);

ALTER TABLE public.colaborador_cargo
  ADD CONSTRAINT fk_cc_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_cc_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id),
  ADD CONSTRAINT fk_cc_cargo FOREIGN KEY (cargo_id) REFERENCES public.cargo(id),
  ADD CONSTRAINT fk_cc_cc FOREIGN KEY (centro_custo_id) REFERENCES public.centro_custo(id);

ALTER TABLE public.contrato
  ADD CONSTRAINT fk_contrato_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_contrato_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id),
  ADD CONSTRAINT fk_contrato_jornada FOREIGN KEY (jornada_id) REFERENCES public.jornada_trabalho(id),
  ADD CONSTRAINT fk_contrato_cargo FOREIGN KEY (cargo_id) REFERENCES public.cargo(id);

-- ============================
-- PONTO
-- ============================
ALTER TABLE public.marcacao_ponto
  ADD CONSTRAINT fk_mp_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_mp_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

ALTER TABLE public.calculo_ponto_dia
  ADD CONSTRAINT fk_cpd_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_cpd_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

ALTER TABLE public.ausencia
  ADD CONSTRAINT fk_aus_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_aus_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

-- ============================
-- FÉRIAS
-- ============================
ALTER TABLE public.ferias_solicitacao
  ADD CONSTRAINT fk_fs_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_fs_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

ALTER TABLE public.ferias_aprovacao
  ADD CONSTRAINT fk_fa_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_fa_sol FOREIGN KEY (solicitacao_id) REFERENCES public.ferias_solicitacao(id),
  ADD CONSTRAINT fk_fa_usuario FOREIGN KEY (aprovador_usuario_id) REFERENCES public.usuario(id);

ALTER TABLE public.ferias_saldo
  ADD CONSTRAINT fk_fsaldo_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_fsaldo_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

-- ============================
-- BENEFÍCIOS
-- ============================
ALTER TABLE public.plano_beneficio
  ADD CONSTRAINT fk_pb_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.adesao_beneficio
  ADD CONSTRAINT fk_ab_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_ab_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id),
  ADD CONSTRAINT fk_ab_plano FOREIGN KEY (plano_id) REFERENCES public.plano_beneficio(id);

-- ============================
-- RECRUTAMENTO
-- ============================
ALTER TABLE public.vaga
  ADD CONSTRAINT fk_vaga_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_vaga_dep FOREIGN KEY (departamento_id) REFERENCES public.departamento(id);

ALTER TABLE public.candidato
  ADD CONSTRAINT fk_cand_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.candidatura
  ADD CONSTRAINT fk_candd_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_candd_vaga FOREIGN KEY (vaga_id) REFERENCES public.vaga(id),
  ADD CONSTRAINT fk_candd_cand FOREIGN KEY (candidato_id) REFERENCES public.candidato(id);

ALTER TABLE public.entrevista
  ADD CONSTRAINT fk_ent_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_ent_candd FOREIGN KEY (candidatura_id) REFERENCES public.candidatura(id),
  ADD CONSTRAINT fk_ent_usuario FOREIGN KEY (entrevistador_usuario_id) REFERENCES public.usuario(id);

-- ============================
-- DESEMPENHO & OKR & PDI
-- ============================
ALTER TABLE public.ciclo_desempenho
  ADD CONSTRAINT fk_cd_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.avaliacao_desempenho
  ADD CONSTRAINT fk_ad_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_ad_ciclo FOREIGN KEY (ciclo_id) REFERENCES public.ciclo_desempenho(id),
  ADD CONSTRAINT fk_ad_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id),
  ADD CONSTRAINT fk_ad_usuario FOREIGN KEY (avaliador_usuario_id) REFERENCES public.usuario(id);

ALTER TABLE public.okr
  ADD CONSTRAINT fk_okr_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_okr_usuario FOREIGN KEY (responsavel_usuario_id) REFERENCES public.usuario(id);

ALTER TABLE public.okr_resultado_chave
  ADD CONSTRAINT fk_okrrc_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_okrrc_okr FOREIGN KEY (okr_id) REFERENCES public.okr(id);

ALTER TABLE public.okr_checkin
  ADD CONSTRAINT fk_okrc_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_okrc_resultado FOREIGN KEY (resultado_chave_id) REFERENCES public.okr_resultado_chave(id),
  ADD CONSTRAINT fk_okrc_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);

ALTER TABLE public.pdi
  ADD CONSTRAINT fk_pdi_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_pdi_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id),
  ADD CONSTRAINT fk_pdi_ciclo FOREIGN KEY (ciclo_id) REFERENCES public.ciclo_desempenho(id);

ALTER TABLE public.competencia_cargo
  ADD CONSTRAINT fk_cc_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_cc_cargo FOREIGN KEY (cargo_id) REFERENCES public.cargo(id);

-- ============================
-- REEMBOLSOS
-- ============================
ALTER TABLE public.politica_reembolso
  ADD CONSTRAINT fk_pr_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.reembolso_solicitacao
  ADD CONSTRAINT fk_rs_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_rs_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

ALTER TABLE public.reembolso_item
  ADD CONSTRAINT fk_ri_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_ri_sol FOREIGN KEY (solicitacao_id) REFERENCES public.reembolso_solicitacao(id);

ALTER TABLE public.reembolso_aprovacao
  ADD CONSTRAINT fk_ra_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_ra_sol FOREIGN KEY (solicitacao_id) REFERENCES public.reembolso_solicitacao(id),
  ADD CONSTRAINT fk_ra_usuario FOREIGN KEY (aprovador_usuario_id) REFERENCES public.usuario(id);

-- ============================
-- FOLHA
-- ============================
ALTER TABLE public.rubrica
  ADD CONSTRAINT fk_rub_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.folha_execucao
  ADD CONSTRAINT fk_fe_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.folha_item
  ADD CONSTRAINT fk_fi_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_fi_execucao FOREIGN KEY (execucao_id) REFERENCES public.folha_execucao(id),
  ADD CONSTRAINT fk_fi_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id),
  ADD CONSTRAINT fk_fi_rubrica FOREIGN KEY (rubrica_id) REFERENCES public.rubrica(id);

-- ============================
-- ESOCIAL
-- ============================
ALTER TABLE public.evento_esocial
  ADD CONSTRAINT fk_esocial_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.payload_esocial
  ADD CONSTRAINT fk_pesocial_evento FOREIGN KEY (evento_id) REFERENCES public.evento_esocial(id);

-- ============================
-- WORKFLOW
-- ============================
ALTER TABLE public.definicao_workflow
  ADD CONSTRAINT fk_dw_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.instancia_workflow
  ADD CONSTRAINT fk_iw_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_iw_definicao FOREIGN KEY (definicao_id) REFERENCES public.definicao_workflow(id);

ALTER TABLE public.tarefa_workflow
  ADD CONSTRAINT fk_tw_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_tw_instancia FOREIGN KEY (instancia_id) REFERENCES public.instancia_workflow(id),
  ADD CONSTRAINT fk_tw_usuario FOREIGN KEY (responsavel_usuario_id) REFERENCES public.usuario(id);

ALTER TABLE public.log_workflow
  ADD CONSTRAINT fk_lw_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_lw_instancia FOREIGN KEY (instancia_id) REFERENCES public.instancia_workflow(id);

-- ============================
-- AUDITORIA
-- ============================
ALTER TABLE public.evento_auditoria
  ADD CONSTRAINT fk_ea_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_ea_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id);

-- ============================
-- NOTIFICAÇÕES
-- ============================
ALTER TABLE public.template_notificacao
  ADD CONSTRAINT fk_tmp_notif_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id);

ALTER TABLE public.notificacao_envio
  ADD CONSTRAINT fk_ne_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_ne_template FOREIGN KEY (template_id) REFERENCES public.template_notificacao(id),
  ADD CONSTRAINT fk_ne_usuario FOREIGN KEY (usuario_id) REFERENCES public.usuario(id),
  ADD CONSTRAINT fk_ne_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

ALTER TABLE public.documento_colaborador
  ADD CONSTRAINT fk_doc_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_doc_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);

-- ============================
-- SAÚDE OCUPACIONAL
-- ============================
ALTER TABLE public.aso
  ADD CONSTRAINT fk_aso_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_aso_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id),
  ADD CONSTRAINT fk_aso_doc FOREIGN KEY (documento_id) REFERENCES public.documento_colaborador(id);

ALTER TABLE public.exame_agendamento
  ADD CONSTRAINT fk_exame_cliente FOREIGN KEY (cliente_id) REFERENCES public.cliente(id),
  ADD CONSTRAINT fk_exame_colab FOREIGN KEY (colaborador_id) REFERENCES public.colaborador(id);
