package com.peopleflow.pessoascontratos.core.model;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.pessoascontratos.core.valueobject.Cpf;
import com.peopleflow.pessoascontratos.core.valueobject.Email;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade de domínio Colaborador (Rich Domain Model)
 * 
 * Este modelo é IMUTÁVEL - todos os métodos de negócio retornam uma NOVA instância.
 * As validações de invariantes estão contidas no próprio modelo.
 */
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Colaborador {
    private Long id;
    private Long clienteId;
    private Long empresaId;
    private Long departamentoId;
    private Long centroCustoId;
    private String nome;
    private Cpf cpf;
    private String matricula;
    private Email email;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private StatusColaborador status;

    /**
     * Construtor para criação simplificada de novo colaborador
     * @deprecated Use o Builder para maior controle e clareza
     */
    @Deprecated(since = "0.2.0", forRemoval = true)
    public Colaborador(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf != null ? new Cpf(cpf) : null;
        this.email = email != null ? new Email(email) : null;
        this.status = StatusColaborador.ATIVO;
        validarInvariantes();
    }

    /**
     * Construtor customizado para Builder com validações
     */
    public static class ColaboradorBuilder {
        public Colaborador build() {
            // Define valores padrão
            if (status == null) {
                status = StatusColaborador.ATIVO;
            }
            
            Colaborador colaborador = new Colaborador(
                id, clienteId, empresaId, departamentoId, centroCustoId,
                nome, cpf, matricula, email, dataAdmissao, dataDemissao, status
            );
            
            // Valida invariantes apenas se não for um objeto em construção (tem nome)
            if (colaborador.nome != null) {
                colaborador.validarInvariantes();
            }
            
            return colaborador;
        }
    }

    /**
     * Valida as invariantes do modelo de domínio
     * Um Colaborador deve SEMPRE estar em estado válido
     */
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

        // Validação de regra de negócio: data de demissão deve ser após admissão
        if (dataDemissao != null && dataAdmissao != null && dataDemissao.isBefore(dataAdmissao)) {
            throw new BusinessException("DATA_DEMISSAO_INVALIDA", 
                "Data de demissão não pode ser anterior à data de admissão");
        }
    }

    /**
     * Demite o colaborador em uma determinada data
     * 
     * @param dataDemissao Data da demissão
     * @return Nova instância do Colaborador com status DEMITIDO (imutável)
     * @throws BusinessException se a data de demissão for inválida ou colaborador já estiver demitido
     */
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
        
        if (status.isDemitido()) {
            throw new BusinessException("COLABORADOR_JA_DEMITIDO", 
                "Colaborador já está demitido");
        }
    }

    /**
     * Ativa o colaborador
     * 
     * @return Nova instância do Colaborador com status ATIVO (imutável)
     * @throws BusinessException se o colaborador estiver excluído
     */
    public Colaborador ativar() {
        if (status.isExcluido()) {
            throw new BusinessException("COLABORADOR_EXCLUIDO", 
                "Não é possível ativar colaborador excluído");
        }
        
        return this.toBuilder()
            .status(StatusColaborador.ATIVO)
            .dataDemissao(null)
            .build();
    }

    /**
     * Inativa o colaborador
     * 
     * @return Nova instância do Colaborador com status INATIVO (imutável)
     */
    public Colaborador inativar() {
        return this.toBuilder()
            .status(StatusColaborador.INATIVO)
            .build();
    }

    /**
     * Exclui logicamente o colaborador (soft delete)
     * 
     * @return Nova instância do Colaborador com status EXCLUIDO (imutável)
     * @throws BusinessException se o colaborador já estiver excluído
     */
    public Colaborador excluir() {
        if (status.isExcluido()) {
            throw new BusinessException("COLABORADOR_JA_EXCLUIDO", 
                "Colaborador já está excluído");
        }
        
        return this.toBuilder()
            .status(StatusColaborador.EXCLUIDO)
            .build();
    }

    /**
     * Atualiza os dados do colaborador mantendo imutabilidade
     * 
     * @param nome Nome atualizado
     * @param cpf CPF atualizado
     * @param email Email atualizado
     * @param matricula Matrícula atualizada
     * @param dataAdmissao Data de admissão atualizada
     * @return Nova instância do Colaborador com dados atualizados
     */
    public Colaborador atualizar(String nome, Cpf cpf, Email email, String matricula, LocalDate dataAdmissao) {
        return this.toBuilder()
            .nome(nome)
            .cpf(cpf)
            .email(email)
            .matricula(matricula)
            .dataAdmissao(dataAdmissao)
            .build();
    }

    /**
     * Verifica se o colaborador está ativo
     */
    public boolean isAtivo() {
        return status.isAtivo();
    }

    /**
     * Verifica se o colaborador está demitido
     */
    public boolean isDemitido() {
        return status.isDemitido();
    }

    /**
     * Verifica se o colaborador está excluído
     */
    public boolean isExcluido() {
        return status.isExcluido();
    }
} 