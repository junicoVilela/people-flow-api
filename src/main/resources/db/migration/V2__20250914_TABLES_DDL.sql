-- ============================
-- ORGANIZAÇÃO / NÚCLEO
-- ============================
CREATE TABLE IF NOT EXISTS public.cliente (
    id UUID PRIMARY KEY,
    nome TEXT NOT NULL,
    cnpj VARCHAR(18),
    status TEXT NOT NULL DEFAULT 'ativo',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.empresa (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    nome TEXT NOT NULL,
    cnpj VARCHAR(18),
    inscricao_estadual TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.unidade (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    empresa_id UUID NOT NULL,
    nome TEXT NOT NULL,
    codigo TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.departamento (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    empresa_id UUID NOT NULL,
    unidade_id UUID,
    nome TEXT NOT NULL,
    codigo TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.centro_custo (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    codigo TEXT NOT NULL,
    nome TEXT NOT NULL,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- SEGURANÇA / RBAC
-- ============================
CREATE TABLE IF NOT EXISTS public.usuario (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    nome TEXT NOT NULL,
    email TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'ativo',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.papel (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    nome TEXT NOT NULL,
    descricao TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.permissao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    recurso TEXT NOT NULL,
    acao TEXT NOT NULL,
    descricao TEXT
);

CREATE TABLE IF NOT EXISTS public.usuario_papel (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    papel_id UUID NOT NULL,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.papel_permissao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    papel_id UUID NOT NULL,
    permissao_id UUID NOT NULL
);

-- ============================
-- PESSOAS / COLABORADORES
-- ============================
CREATE TABLE IF NOT EXISTS public.colaborador (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    empresa_id UUID NOT NULL,
    departamento_id UUID,
    centro_custo_id UUID,
    nome TEXT NOT NULL,
    cpf VARCHAR(14),
    matricula VARCHAR(20),
    email TEXT,
    data_admissao DATE,
    data_demissao DATE,
    status TEXT NOT NULL DEFAULT 'ativo',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.dependente (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    nome TEXT NOT NULL,
    parentesco TEXT NOT NULL,
    data_nascimento DATE,
    cpf VARCHAR(14)
);

CREATE TABLE IF NOT EXISTS public.conta_bancaria (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    banco TEXT NOT NULL,
    agencia TEXT NOT NULL,
    conta TEXT NOT NULL,
    tipo TEXT,
    pix TEXT
);

-- ============================
-- JORNADA & CARGOS
-- ============================
CREATE TABLE IF NOT EXISTS public.jornada_trabalho (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    descricao TEXT NOT NULL,
    carga_semanal_horas NUMERIC(5,2),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.cargo (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    nome TEXT NOT NULL,
    descricao TEXT,
    nivel TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.faixa_salarial (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    cargo_id UUID NOT NULL,
    faixa_min NUMERIC(14,2) NOT NULL,
    faixa_max NUMERIC(14,2) NOT NULL,
    moeda TEXT DEFAULT 'BRL',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.colaborador_cargo (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    cargo_id UUID NOT NULL,
    centro_custo_id UUID,
    salario NUMERIC(14,2),
    inicio DATE NOT NULL,
    fim DATE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- CONTRATOS
-- ============================
CREATE TABLE IF NOT EXISTS public.contrato (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    jornada_id UUID NOT NULL,
    cargo_id UUID NOT NULL,
    tipo TEXT,
    regime TEXT,
    salario_base NUMERIC(14,2),
    inicio DATE NOT NULL,
    fim DATE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- PONTO
-- ============================
CREATE TABLE IF NOT EXISTS public.marcacao_ponto (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    data_hora TIMESTAMPTZ NOT NULL,
    tipo TEXT NOT NULL, -- entrada/saida
    origem TEXT,        -- kiosk/mobile/import
    latitude NUMERIC(9,6),
    longitude NUMERIC(9,6),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.calculo_ponto_dia (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    data DATE NOT NULL,
    horas_trabalhadas NUMERIC(6,2) DEFAULT 0,
    horas_extras NUMERIC(6,2) DEFAULT 0,
    banco_horas_saldo NUMERIC(7,2) DEFAULT 0,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.ausencia (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    tipo TEXT NOT NULL,  -- atestado, falta, justificada
    inicio TIMESTAMPTZ NOT NULL,
    fim TIMESTAMPTZ NOT NULL,
    justificativa TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- FÉRIAS
-- ============================
CREATE TABLE IF NOT EXISTS public.ferias_solicitacao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    inicio DATE NOT NULL,
    fim DATE NOT NULL,
    abono BOOLEAN DEFAULT FALSE,
    status TEXT NOT NULL DEFAULT 'pendente',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.ferias_aprovacao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    solicitacao_id UUID NOT NULL,
    aprovador_usuario_id UUID NOT NULL,
    status TEXT NOT NULL, -- aprovado/reprovado
    observacao TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.ferias_saldo (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    dias_aquisitivos INTEGER DEFAULT 0,
    dias_disponiveis INTEGER DEFAULT 0,
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- BENEFÍCIOS
-- ============================
CREATE TABLE IF NOT EXISTS public.plano_beneficio (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    tipo TEXT NOT NULL, -- VT/VR/Saude/Outro
    nome TEXT NOT NULL,
    fornecedor TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.adesao_beneficio (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    plano_id UUID NOT NULL,
    inicio DATE NOT NULL,
    fim DATE,
    custo_mensal NUMERIC(12,2),
    cota_colaborador NUMERIC(12,2),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- RECRUTAMENTO
-- ============================
CREATE TABLE IF NOT EXISTS public.vaga (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    titulo TEXT NOT NULL,
    descricao TEXT,
    departamento_id UUID,
    localidade TEXT,
    remoto BOOLEAN DEFAULT FALSE,
    status TEXT NOT NULL DEFAULT 'aberta',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.candidato (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    nome TEXT NOT NULL,
    email TEXT,
    telefone TEXT,
    cpf VARCHAR(14),
    consentimento_lgpd BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.candidatura (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    vaga_id UUID NOT NULL,
    candidato_id UUID NOT NULL,
    data_candidatura DATE NOT NULL,
    etapa_atual TEXT,
    status TEXT NOT NULL DEFAULT 'em_analise',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.entrevista (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    candidatura_id UUID NOT NULL,
    entrevistador_usuario_id UUID NOT NULL,
    agendada_em TIMESTAMPTZ NOT NULL,
    tipo TEXT,
    feedback TEXT,
    resultado TEXT, -- aprovado/reprovado/pendente
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- DESEMPENHO
-- ============================
CREATE TABLE IF NOT EXISTS public.ciclo_desempenho (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    nome TEXT NOT NULL,
    inicio DATE NOT NULL,
    fim DATE NOT NULL,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.avaliacao_desempenho (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    ciclo_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    avaliador_usuario_id UUID NOT NULL,
    nota NUMERIC(4,2),
    comentarios TEXT,
    status TEXT NOT NULL DEFAULT 'rascunho',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- OKR
-- ============================
CREATE TABLE IF NOT EXISTS public.okr (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    tipo TEXT NOT NULL, -- organizacional/equipe/pessoal
    titulo TEXT NOT NULL,
    descricao TEXT,
    responsavel_usuario_id UUID,
    inicio DATE,
    fim DATE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.okr_resultado_chave (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    okr_id UUID NOT NULL,
    titulo TEXT NOT NULL,
    unidade TEXT, -- percentual/numero
    valor_alvo NUMERIC(14,2),
    valor_atual NUMERIC(14,2) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS public.okr_checkin (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    resultado_chave_id UUID NOT NULL,
    realizado_em TIMESTAMPTZ NOT NULL,
    valor NUMERIC(14,2),
    comentario TEXT,
    usuario_id UUID
);

-- ============================
-- PDI & COMPETÊNCIAS
-- ============================
CREATE TABLE IF NOT EXISTS public.pdi (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    ciclo_id UUID,
    objetivo TEXT NOT NULL,
    acoes JSONB NOT NULL DEFAULT '[]'::jsonb,
    status TEXT NOT NULL DEFAULT 'aberto',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.competencia_cargo (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    cargo_id UUID NOT NULL,
    nome TEXT NOT NULL,
    nivel_esperado INTEGER,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- REEMBOLSOS
-- ============================
CREATE TABLE IF NOT EXISTS public.politica_reembolso (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    categoria TEXT NOT NULL,
    valor_max_por_dia NUMERIC(12,2),
    moeda TEXT DEFAULT 'BRL',
    exige_documento BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS public.reembolso_solicitacao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    categoria TEXT NOT NULL,
    valor_total NUMERIC(14,2) NOT NULL DEFAULT 0,
    status TEXT NOT NULL DEFAULT 'pendente',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.reembolso_item (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    solicitacao_id UUID NOT NULL,
    descricao TEXT NOT NULL,
    valor NUMERIC(14,2) NOT NULL,
    data DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS public.reembolso_aprovacao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    solicitacao_id UUID NOT NULL,
    aprovador_usuario_id UUID NOT NULL,
    status TEXT NOT NULL, -- aprovado/reprovado
    observacao TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- FOLHA DE PAGAMENTO
-- ============================
CREATE TABLE IF NOT EXISTS public.rubrica (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    codigo TEXT NOT NULL,
    descricao TEXT NOT NULL,
    tipo TEXT NOT NULL, -- provento/desconto
    base_calculo JSONB DEFAULT '{}'::jsonb
);

CREATE TABLE IF NOT EXISTS public.folha_execucao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    competencia DATE NOT NULL, -- use o dia 1 do mês
    status TEXT NOT NULL DEFAULT 'aberta',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.folha_item (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    execucao_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    rubrica_id UUID NOT NULL,
    quantidade NUMERIC(12,2) DEFAULT 1,
    valor NUMERIC(14,2) NOT NULL
);

-- ============================
-- ESOCIAL
-- ============================
CREATE TABLE IF NOT EXISTS public.evento_esocial (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    tipo TEXT NOT NULL, -- S-1000, S-2200, S-1200...
    referencia_id UUID,
    status TEXT NOT NULL DEFAULT 'gerado',
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.payload_esocial (
    id UUID PRIMARY KEY,
    evento_id UUID NOT NULL,
    versao_layout TEXT,
    xml TEXT,
    recibo TEXT,
    protocolo TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- WORKFLOW
-- ============================
CREATE TABLE IF NOT EXISTS public.definicao_workflow (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    nome TEXT NOT NULL,
    descricao TEXT,
    modelo JSONB NOT NULL DEFAULT '{}'::jsonb,
    versao INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS public.instancia_workflow (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    definicao_id UUID NOT NULL,
    entidade TEXT NOT NULL,
    entidade_id UUID NOT NULL,
    status TEXT NOT NULL DEFAULT 'em_andamento',
    contexto JSONB NOT NULL DEFAULT '{}'::jsonb,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.tarefa_workflow (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    instancia_id UUID NOT NULL,
    responsavel_usuario_id UUID,
    etapa TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT 'aberta',
    prazo DATE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.log_workflow (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    instancia_id UUID NOT NULL,
    mensagem TEXT NOT NULL,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- AUDITORIA
-- ============================
CREATE TABLE IF NOT EXISTS public.evento_auditoria (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    usuario_id UUID,
    entidade TEXT NOT NULL,
    entidade_id UUID,
    acao TEXT NOT NULL, -- create/update/delete
    dados JSONB NOT NULL DEFAULT '{}'::jsonb,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- NOTIFICAÇÕES
-- ============================
CREATE TABLE IF NOT EXISTS public.template_notificacao (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    nome TEXT NOT NULL,
    canal TEXT NOT NULL CHECK (canal IN ('email','push','inapp')),
    assunto TEXT,
    corpo TEXT NOT NULL,
    variaveis JSONB NOT NULL DEFAULT '{}'::jsonb,
    versao INTEGER NOT NULL DEFAULT 1,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.notificacao_envio (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    template_id UUID,
    usuario_id UUID,
    colaborador_id UUID,
    canal TEXT NOT NULL CHECK (canal IN ('email','push','inapp')),
    assunto TEXT,
    corpo_renderizado TEXT NOT NULL,
    contexto JSONB NOT NULL DEFAULT '{}'::jsonb,
    status TEXT NOT NULL DEFAULT 'pendente' CHECK (status IN ('pendente','enviado','erro')),
    erro TEXT,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    enviado_em TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS public.documento_colaborador (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    tipo TEXT NOT NULL,
    nome_arquivo TEXT NOT NULL,
    mime_type TEXT NOT NULL,
    tamanho_bytes BIGINT NOT NULL CHECK (tamanho_bytes >= 0),
    storage_key TEXT NOT NULL,
    versao INTEGER NOT NULL DEFAULT 1 CHECK (versao > 0),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- ============================
-- SAÚDE OCUPACIONAL
-- ============================
CREATE TABLE IF NOT EXISTS public.aso (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    tipo TEXT NOT NULL CHECK (tipo IN ('admissional','periodico','retorno','mudanca','demissional')),
    realizado_em DATE NOT NULL,
    validade_ate DATE,
    clinica TEXT,
    resultado TEXT,
    documento_id UUID,
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS public.exame_agendamento (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    colaborador_id UUID NOT NULL,
    tipo_exame TEXT NOT NULL,
    clinica TEXT,
    agendado_em TIMESTAMPTZ NOT NULL,
    realizado_em TIMESTAMPTZ,
    compareceu BOOLEAN,
    status TEXT NOT NULL DEFAULT 'agendado' CHECK (status IN ('agendado','realizado','faltou','cancelado')),
    criado_em TIMESTAMPTZ NOT NULL DEFAULT now(),
    atualizado_em TIMESTAMPTZ NOT NULL DEFAULT now()
);

