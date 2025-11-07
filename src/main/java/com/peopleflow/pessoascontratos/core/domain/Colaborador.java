package com.peopleflow.pessoascontratos.core.domain;

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
     * Construtor customizado para Builder com validações
     */
    public static class ColaboradorBuilder {
        public Colaborador build() {
            if (status == null) {
                status = StatusColaborador.ATIVO;
            }
            
            Colaborador colaborador = new Colaborador(
                id, clienteId, empresaId, departamentoId, centroCustoId,
                nome, cpf, matricula, email, dataAdmissao, dataDemissao, status
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
        
        if (status.isDemitido()) {
            throw new BusinessException("COLABORADOR_JA_DEMITIDO", 
                "Colaborador já está demitido");
        }
    }

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

    public Colaborador inativar() {
        return this.toBuilder()
            .status(StatusColaborador.INATIVO)
            .build();
    }

    public Colaborador excluir() {
        if (status.isExcluido()) {
            throw new BusinessException("COLABORADOR_JA_EXCLUIDO", 
                "Colaborador já está excluído");
        }
        
        return this.toBuilder()
            .status(StatusColaborador.EXCLUIDO)
            .build();
    }


    public Colaborador atualizar(String nome, Cpf cpf, Email email, String matricula, LocalDate dataAdmissao) {
        return this.toBuilder()
            .nome(nome)
            .cpf(cpf)
            .email(email)
            .matricula(matricula)
            .dataAdmissao(dataAdmissao)
            .build();
    }

    public boolean isAtivo() {
        return status.isAtivo();
    }

    public boolean isDemitido() {
        return status.isDemitido();
    }

    public boolean isExcluido() {
        return status.isExcluido();
    }
}

