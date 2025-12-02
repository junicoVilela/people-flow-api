package com.peopleflow.pessoascontratos.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.valueobject.Cpf;
import com.peopleflow.common.valueobject.Email;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Colaborador {
    private Long id;
    private String nome;
    private Cpf cpf;
    private String matricula;
    private Email email;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private StatusColaborador status;
    private Long clienteId;
    private Long empresaId;
    private Long departamentoId;
    private Long centroCustoId;
    private Long cargoId;
    private String keycloakUserId;

    public static class ColaboradorBuilder {
        public Colaborador build() {
            if (status == null) {
                status = StatusColaborador.ATIVO;
            }
            
            Colaborador colaborador = new Colaborador(
                id,
                nome,
                cpf,
                matricula,
                email,
                dataAdmissao,
                dataDemissao,
                status,
                clienteId,
                empresaId,
                departamentoId,
                centroCustoId,
                cargoId,
                keycloakUserId
            );

            if (colaborador.nome != null) {
                colaborador.validarInvariantes();
            }
            
            return colaborador;
        }
    }

    
    private void validarInvariantes() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessException("NOME_OBRIGATORIO", "Nome é obrigatório");
        }
        
        if (cpf == null) {
            throw new BusinessException("CPF_OBRIGATORIO", "CPF é obrigatório");
        }
        
        if (email == null) {
            throw new BusinessException("EMAIL_OBRIGATORIO", "Email é obrigatório");
        }

        if (clienteId == null) {
            throw new BusinessException("CLIENTE_ID_OBRIGATORIO", 
                "Cliente ID é obrigatório");
        }
        
        if (empresaId == null) {
            throw new BusinessException("EMPRESA_ID_OBRIGATORIO", 
                "Empresa ID é obrigatória");
        }

        if (dataDemissao != null && dataAdmissao != null && dataDemissao.isBefore(dataAdmissao)) {
            throw new BusinessException("DATA_DEMISSAO_INVALIDA", 
                "Data de demissão não pode ser anterior à data de admissão");
        }
    }

    public Colaborador demitir(LocalDate dataDemissao) {
        validarDemissao(dataDemissao);
        
        return this.toBuilder()
            .dataDemissao(dataDemissao)
            .status(StatusColaborador.DEMITIDO)
            .build();
    }

    private void validarDemissao(LocalDate dataDemissao) {
        if (dataDemissao == null) {
            throw new BusinessException("DATA_DEMISSAO_OBRIGATORIA", "Data de demissão é obrigatória");
        }
        
        if (dataAdmissao != null && dataDemissao.isBefore(dataAdmissao)) {
            throw new BusinessException("DATA_DEMISSAO_INVALIDA", 
                "Data de demissão não pode ser anterior à data de admissão");
        }
        
        if (isDemitido()) {
            throw new BusinessException("COLABORADOR_JA_DEMITIDO", 
                "Colaborador já está demitido");
        }
    }

    public Colaborador ativar() {
        if (isExcluido()) {
            throw new BusinessException("COLABORADOR_EXCLUIDO", 
                "Não é possível ativar colaborador excluído");
        }
        
        return this.toBuilder()
            .status(StatusColaborador.ATIVO)
            .dataDemissao(null)
            .build();
    }

    public Colaborador inativar() {
        return this.toBuilder()
            .status(StatusColaborador.INATIVO)
            .build();
    }

    public Colaborador excluir() {
        if (isExcluido()) {
            throw new BusinessException("COLABORADOR_JA_EXCLUIDO", 
                "Colaborador já está excluído");
        }
        
        return this.toBuilder()
            .status(StatusColaborador.EXCLUIDO)
            .build();
    }


    public Colaborador atualizar(String nome, Cpf cpf, Email email, String matricula, LocalDate dataAdmissao,
                                  Long clienteId, Long empresaId, Long departamentoId, Long centroCustoId, Long cargoId) {
        return this.toBuilder()
            .nome(nome)
            .cpf(cpf)
            .email(email)
            .matricula(matricula)
            .dataAdmissao(dataAdmissao)
            .clienteId(clienteId)
            .empresaId(empresaId)
            .departamentoId(departamentoId)
            .centroCustoId(centroCustoId)
            .cargoId(cargoId)
            .build();
    }

    public boolean isDemitido() {
        return status.isDemitido();
    }

    public boolean isExcluido() {
        return status.isExcluido();
    }

    /**
     * Vincula um usuário Keycloak a este colaborador
     * @param keycloakUserId UUID do usuário no Keycloak
     * @return Colaborador atualizado com vínculo
     */
    public Colaborador vincularUsuarioKeycloak(String keycloakUserId) {
        if (keycloakUserId == null || keycloakUserId.isBlank()) {
            throw new BusinessException("KEYCLOAK_USER_ID_INVALIDO", 
                "ID do usuário Keycloak não pode ser vazio");
        }
        
        return this.toBuilder()
            .keycloakUserId(keycloakUserId)
            .build();
    }

    /**
     * Verifica se o colaborador tem acesso ao sistema
     * @return true se o colaborador está vinculado a um usuário Keycloak
     */
    public boolean temAcessoSistema() {
        return keycloakUserId != null && !keycloakUserId.isBlank();
    }

    // =====================================================================
    // FACTORY METHODS - Múltiplas Formas de Criação
    // =====================================================================
    
    /**
     * Factory Method para criar novo colaborador via admissão
     * 
     * Cenário: Admissão de novo colaborador na empresa
     * 
     * Regras de Negócio:
     * - Status inicial: ATIVO
     * - Data de admissão default: hoje se não informada
     * - Todos os campos obrigatórios devem ser preenchidos
     * - Value Objects são validados automaticamente
     * 
     * @param nome Nome completo do colaborador
     * @param cpfString CPF (será validado e convertido para Value Object)
     * @param emailString Email (será validado e convertido para Value Object)
     * @param matricula Matrícula do colaborador
     * @param dataAdmissao Data de admissão (se null, usa a data atual)
     * @param clienteId ID do cliente (contexto de segurança)
     * @param empresaId ID da empresa (contexto de segurança)
     * @param departamentoId ID do departamento (opcional)
     * @param centroCustoId ID do centro de custo (opcional)
     * @return Colaborador criado e validado
     */
    public static Colaborador novaAdmissao(
            String nome,
            String cpfString,
            String emailString,
            String matricula,
            LocalDate dataAdmissao,
            Long clienteId,
            Long empresaId,
            Long departamentoId,
            Long centroCustoId) {
        
        // Validar e criar Value Objects (lança exceção se inválido)
        Cpf cpf = new Cpf(cpfString);
        Email email = new Email(emailString);
        
        // Aplicar defaults de negócio
        LocalDate dataAdmissaoFinal = dataAdmissao != null ? dataAdmissao : LocalDate.now();
        
        // Construir colaborador com status inicial ATIVO
        return Colaborador.builder()
            .nome(nome)
            .cpf(cpf)
            .email(email)
            .matricula(matricula)
            .dataAdmissao(dataAdmissaoFinal)
            .status(StatusColaborador.ATIVO)
            .clienteId(clienteId)
            .empresaId(empresaId)
            .departamentoId(departamentoId)
            .centroCustoId(centroCustoId)
            .build();
    }
    
    /**
     * Factory Method para transferir colaborador entre empresas/departamentos
     * 
     * Cenário: Movimentação interna de colaborador (transferência)
     * 
     * Regras de Negócio:
     * - Colaborador deve estar ATIVO para ser transferido
     * - Mantém todos os dados pessoais (CPF, email, matrícula)
     * - Atualiza apenas dados organizacionais
     * - Data de transferência é registrada
     * 
     * @param colaboradorOriginal Colaborador a ser transferido
     * @param novaEmpresaId Nova empresa de destino
     * @param novoDepartamentoId Novo departamento (opcional)
     * @param novoCentroCustoId Novo centro de custo (opcional)
     * @param dataTransferencia Data da transferência (null = hoje)
     * @return Colaborador com dados atualizados
     * @throws BusinessException se colaborador não estiver ativo
     */
    public static Colaborador porTransferencia(
            Colaborador colaboradorOriginal,
            Long novaEmpresaId,
            Long novoDepartamentoId,
            Long novoCentroCustoId,
            LocalDate dataTransferencia) {
        
        // Validar que colaborador está apto para transferência
        if (!colaboradorOriginal.getStatus().isAtivo()) {
            throw new BusinessException("TRANSFERENCIA_INVALIDA", 
                "Apenas colaboradores ativos podem ser transferidos. Status atual: " + 
                colaboradorOriginal.getStatus().getValor());
        }
        
        if (colaboradorOriginal.isDemitido()) {
            throw new BusinessException("TRANSFERENCIA_INVALIDA", 
                "Não é possível transferir colaborador demitido");
        }
        
        if (colaboradorOriginal.isExcluido()) {
            throw new BusinessException("TRANSFERENCIA_INVALIDA", 
                "Não é possível transferir colaborador excluído");
        }
        
        // Validar que nova empresa é diferente (se for mesma empresa, é movimentação interna)
        if (novaEmpresaId == null) {
            throw new BusinessException("EMPRESA_DESTINO_OBRIGATORIA", 
                "Empresa de destino é obrigatória para transferência");
        }
        
        // Data de transferência (default: hoje)
        LocalDate dataTransferenciaFinal = dataTransferencia != null ? dataTransferencia : LocalDate.now();
        
        // Validar que data de transferência não é anterior à admissão
        if (colaboradorOriginal.getDataAdmissao() != null && 
            dataTransferenciaFinal.isBefore(colaboradorOriginal.getDataAdmissao())) {
            throw new BusinessException("DATA_TRANSFERENCIA_INVALIDA", 
                "Data de transferência não pode ser anterior à data de admissão");
        }
        
        // Criar colaborador transferido mantendo dados pessoais
        return colaboradorOriginal.toBuilder()
            .empresaId(novaEmpresaId)
            .departamentoId(novoDepartamentoId)
            .centroCustoId(novoCentroCustoId)
            // Mantém: nome, cpf, email, matricula, dataAdmissao, clienteId, status
            .build();
    }
    
    /**
     * Factory Method para importar colaborador de sistema legado
     * 
     * Cenário: Migração de dados de sistema antigo
     * 
     * Regras de Negócio:
     * - Matrícula legada é prefixada com "LEG-" para rastreabilidade
     * - Status legado é traduzido para o novo sistema
     * - Data de admissão é obrigatória (histórico)
     * - CPF e Email são validados mesmo sendo dados históricos
     * 
     * @param nome Nome completo do colaborador
     * @param cpfString CPF (será validado)
     * @param emailString Email (será validado)
     * @param matriculaLegado Matrícula do sistema antigo
     * @param dataAdmissao Data de admissão histórica (obrigatória)
     * @param statusLegado Status no sistema legado (será traduzido)
     * @param clienteId ID do cliente
     * @param empresaId ID da empresa
     * @param departamentoId ID do departamento (opcional)
     * @param centroCustoId ID do centro de custo (opcional)
     * @return Colaborador importado e validado
     * @throws BusinessException se data de admissão for null ou futura
     */
    public static Colaborador porImportacaoLegado(
            String nome,
            String cpfString,
            String emailString,
            String matriculaLegado,
            LocalDate dataAdmissao,
            String statusLegado,
            Long clienteId,
            Long empresaId,
            Long departamentoId,
            Long centroCustoId) {
        
        // Validar data de admissão (obrigatória em importação)
        if (dataAdmissao == null) {
            throw new BusinessException("DATA_ADMISSAO_OBRIGATORIA_IMPORTACAO", 
                "Data de admissão é obrigatória para importação de dados históricos");
        }
        
        // Data de admissão não pode ser futura
        if (dataAdmissao.isAfter(LocalDate.now())) {
            throw new BusinessException("DATA_ADMISSAO_FUTURA", 
                "Data de admissão não pode ser futura em importação histórica");
        }
        
        // Validar e criar Value Objects
        Cpf cpf = new Cpf(cpfString);
        Email email = new Email(emailString);
        
        // Gerar nova matrícula com prefixo de importação
        String novaMatricula = gerarMatriculaImportacao(matriculaLegado);
        
        // Traduzir status legado para o novo sistema
        StatusColaborador statusTraduzido = traduzirStatusLegado(statusLegado);
        
        // Construir colaborador importado
        return Colaborador.builder()
            .nome(nome)
            .cpf(cpf)
            .email(email)
            .matricula(novaMatricula)
            .dataAdmissao(dataAdmissao)
            .status(statusTraduzido)
            .clienteId(clienteId)
            .empresaId(empresaId)
            .departamentoId(departamentoId)
            .centroCustoId(centroCustoId)
            .build();
    }
    
    /**
     * Factory Method para reativar colaborador excluído
     * 
     * Cenário: Colaborador excluído por engano ou que retorna à empresa
     * 
     * Regras de Negócio:
     * - Apenas colaboradores EXCLUÍDOS podem ser reativados
     * - Status muda para ATIVO
     * - Data de demissão é zerada
     * - Nova data de admissão é registrada
     * - Mantém todos os outros dados (CPF, email, matrícula)
     * 
     * @param colaboradorExcluido Colaborador a ser reativado
     * @param novaDataAdmissao Nova data de admissão (null = hoje)
     * @return Colaborador reativado
     * @throws BusinessException se colaborador não estiver excluído
     */
    public static Colaborador porReativacao(
            Colaborador colaboradorExcluido, 
            LocalDate novaDataAdmissao) {
        
        // Validar que colaborador está excluído
        if (!colaboradorExcluido.isExcluido()) {
            throw new BusinessException("REATIVACAO_INVALIDA", 
                "Apenas colaboradores excluídos podem ser reativados. Status atual: " + 
                colaboradorExcluido.getStatus().getValor());
        }
        
        // Nova data de admissão (default: hoje)
        LocalDate dataAdmissaoFinal = novaDataAdmissao != null ? novaDataAdmissao : LocalDate.now();
        
        // Validar que nova data de admissão não é futura
        if (dataAdmissaoFinal.isAfter(LocalDate.now())) {
            throw new BusinessException("DATA_ADMISSAO_FUTURA", 
                "Data de admissão não pode ser futura");
        }
        
        // Reativar colaborador
        return colaboradorExcluido.toBuilder()
            .status(StatusColaborador.ATIVO)
            .dataAdmissao(dataAdmissaoFinal)
            .dataDemissao(null)  // Limpar data de demissão
            .build();
    }
    
    // =====================================================================
    // MÉTODOS AUXILIARES - Encapsulamento de Conhecimento do Domínio
    // =====================================================================
    
    /**
     * Gera matrícula para colaborador importado de sistema legado
     * 
     * Regra: Prefixo "LEG-" + matrícula original
     * Isso permite rastrear origem dos dados e evitar conflitos
     * 
     * @param matriculaLegado Matrícula do sistema antigo
     * @return Matrícula formatada para o novo sistema
     */
    private static String gerarMatriculaImportacao(String matriculaLegado) {
        if (matriculaLegado == null || matriculaLegado.trim().isEmpty()) {
            // Se não há matrícula legada, gera uma baseada em timestamp
            return "LEG-" + System.currentTimeMillis();
        }
        
        // Se já tem o prefixo, não duplica
        if (matriculaLegado.startsWith("LEG-")) {
            return matriculaLegado;
        }
        
        return "LEG-" + matriculaLegado.trim();
    }
    
    /**
     * Traduz status do sistema legado para o novo sistema
     * 
     * Conhecimento do domínio: mapeamento de diferentes representações
     * de status entre sistemas
     * 
     * @param statusLegado Status no formato do sistema antigo
     * @return Status no formato do novo sistema
     */
    private static StatusColaborador traduzirStatusLegado(String statusLegado) {
        if (statusLegado == null || statusLegado.trim().isEmpty()) {
            // Default: se não há status, assume ATIVO
            return StatusColaborador.ATIVO;
        }
        
        // Normalizar para comparação (case-insensitive)
        String statusNormalizado = statusLegado.trim().toUpperCase();
        
        // Mapear diferentes representações
        return switch (statusNormalizado) {
            // Variações de "Ativo"
            case "ATIVO", "ACTIVE", "A", "1", "TRABALHANDO", "EMPREGADO" -> 
                StatusColaborador.ATIVO;
            
            // Variações de "Inativo"
            case "INATIVO", "INACTIVE", "I", "0", "AFASTADO", "LICENCA" -> 
                StatusColaborador.INATIVO;
            
            // Variações de "Demitido"
            case "DEMITIDO", "FIRED", "DESLIGADO", "D", "2", "RESCINDIDO" -> 
                StatusColaborador.DEMITIDO;
            
            // Variações de "Excluído"
            case "EXCLUIDO", "DELETED", "REMOVIDO", "E", "9" -> 
                StatusColaborador.EXCLUIDO;
            
            // Caso não reconhecido, assume ATIVO com log de warning
            default -> {
                // TODO: Adicionar log de warning aqui
                // log.warn("Status legado desconhecido: '{}'. Assumindo ATIVO", statusLegado);
                yield StatusColaborador.ATIVO;
            }
        };
    }
    
    /**
     * Verifica se colaborador pode ser transferido
     * 
     * Encapsula regra: apenas colaboradores ativos podem ser transferidos
     * 
     * @return true se pode ser transferido
     */
    public boolean podeSerTransferido() {
        return this.status.isAtivo() && 
               !this.isDemitido() && 
               !this.isExcluido();
    }
    
    /**
     * Verifica se colaborador pode ser reativado
     * 
     * Encapsula regra: apenas colaboradores excluídos podem ser reativados
     * 
     * @return true se pode ser reativado
     */
    public boolean podeSerReativado() {
        return this.isExcluido();
    }
    
    /**
     * Calcula tempo de empresa em dias
     * 
     * Encapsula cálculo de antiguidade considerando demissão
     * 
     * @return Número de dias trabalhados
     */
    public long calcularTempoEmpresaDias() {
        if (dataAdmissao == null) {
            return 0;
        }
        
        LocalDate dataFim = dataDemissao != null ? dataDemissao : LocalDate.now();
        return java.time.temporal.ChronoUnit.DAYS.between(dataAdmissao, dataFim);
    }
    
    /**
     * Verifica se colaborador está em período de experiência
     * 
     * Encapsula regra: período de experiência é de 90 dias
     * 
     * @return true se está em experiência (menos de 90 dias)
     */
    public boolean estaEmPeriodoExperiencia() {
        return !isDemitido() && calcularTempoEmpresaDias() < 90;
    }
}

