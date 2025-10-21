package com.peopleflow.pessoascontratos.core.model;

import com.peopleflow.pessoascontratos.core.valueobject.Cpf;
import com.peopleflow.pessoascontratos.core.valueobject.Email;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import lombok.Data;

import java.time.LocalDate;

@Data
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

    public Colaborador() {}

    public Colaborador(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf != null ? new Cpf(cpf) : null;
        this.email = email != null ? new Email(email) : null;
        this.status = StatusColaborador.ATIVO;
    }

    public void demitir(LocalDate dataDemissao) {
        this.dataDemissao = dataDemissao;
        this.status = StatusColaborador.DEMITIDO;
    }

    public void ativar() {
        this.status = StatusColaborador.ATIVO;
        this.dataDemissao = null;
    }

    public void inativar() {
        this.status = StatusColaborador.INATIVO;
    }

    public void excluir() {
        this.status = StatusColaborador.EXCLUIDO;
    }
} 