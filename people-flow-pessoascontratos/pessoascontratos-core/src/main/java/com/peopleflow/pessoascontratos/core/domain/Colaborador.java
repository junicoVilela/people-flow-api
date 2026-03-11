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
                                  Long empresaId, Long departamentoId, Long centroCustoId, Long cargoId) {
        return this.toBuilder()
            .nome(nome)
            .cpf(cpf)
            .email(email)
            .matricula(matricula)
            .dataAdmissao(dataAdmissao)
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
     * @param empresaId ID da empresa
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
            Long empresaId,
            Long departamentoId,
            Long centroCustoId,
            Long cargoId) {

        Cpf cpf = new Cpf(cpfString);
        Email email = new Email(emailString);

        LocalDate dataAdmissaoFinal = dataAdmissao != null ? dataAdmissao : LocalDate.now();

        return Colaborador.builder()
            .nome(nome)
            .cpf(cpf)
            .email(email)
            .matricula(matricula)
            .dataAdmissao(dataAdmissaoFinal)
            .status(StatusColaborador.ATIVO)
            .empresaId(empresaId)
            .departamentoId(departamentoId)
            .centroCustoId(centroCustoId)
            .cargoId(cargoId)
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
        
        if (!colaboradorOriginal.getStatus().isAtivo()) {
            throw new BusinessException("TRANSFERENCIA_INVALIDA",
                "Apenas colaboradores ativos podem ser transferidos. Status atual: " +
                colaboradorOriginal.getStatus().getValor());
        }

        if (novaEmpresaId == null) {
            throw new BusinessException("EMPRESA_DESTINO_OBRIGATORIA", 
                "Empresa de destino é obrigatória para transferência");
        }
        
        LocalDate dataTransferenciaFinal = dataTransferencia != null ? dataTransferencia : LocalDate.now();
        
        if (colaboradorOriginal.getDataAdmissao() != null &&
            dataTransferenciaFinal.isBefore(colaboradorOriginal.getDataAdmissao())) {
            throw new BusinessException("DATA_TRANSFERENCIA_INVALIDA", 
                "Data de transferência não pode ser anterior à data de admissão");
        }
        
        return colaboradorOriginal.toBuilder()
            .empresaId(novaEmpresaId)
            .departamentoId(novoDepartamentoId)
            .centroCustoId(novoCentroCustoId)
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
        
        if (!colaboradorExcluido.isExcluido()) {
            throw new BusinessException("REATIVACAO_INVALIDA", 
                "Apenas colaboradores excluídos podem ser reativados. Status atual: " + 
                colaboradorExcluido.getStatus().getValor());
        }
        
        LocalDate dataAdmissaoFinal = novaDataAdmissao != null ? novaDataAdmissao : LocalDate.now();
        
        if (dataAdmissaoFinal.isAfter(LocalDate.now())) {
            throw new BusinessException("DATA_ADMISSAO_FUTURA", 
                "Data de admissão não pode ser futura");
        }
        
        return colaboradorExcluido.toBuilder()
            .status(StatusColaborador.ATIVO)
            .dataAdmissao(dataAdmissaoFinal)
            .dataDemissao(null)  // Limpar data de demissão
            .build();
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

