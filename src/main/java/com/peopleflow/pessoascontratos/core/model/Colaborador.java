package com.peopleflow.pessoascontratos.core.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class Colaborador {
    private UUID id;
    private UUID clienteId;
    private UUID empresaId;
    private UUID departamentoId;
    private UUID centroCustoId;
    private String nome;
    private String cpf;
    private String matricula;
    private String email;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private String status;
    private Instant criadoEm;
    private Instant atualizadoEm;

    // Construtor padrão
    public Colaborador() {}

    // Construtor com parâmetros principais
    public Colaborador(String nome, String cpf, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.status = "ativo";
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getClienteId() { return clienteId; }
    public UUID getEmpresaId() { return empresaId; }
    public UUID getDepartamentoId() { return departamentoId; }
    public UUID getCentroCustoId() { return centroCustoId; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getMatricula() { return matricula; }
    public String getEmail() { return email; }
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public LocalDate getDataDemissao() { return dataDemissao; }
    public String getStatus() { return status; }
    public Instant getCriadoEm() { return criadoEm; }
    public Instant getAtualizadoEm() { return atualizadoEm; }

    // Setters
    public void setId(UUID id) { this.id = id; }
    public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }
    public void setEmpresaId(UUID empresaId) { this.empresaId = empresaId; }
    public void setDepartamentoId(UUID departamentoId) { this.departamentoId = departamentoId; }
    public void setCentroCustoId(UUID centroCustoId) { this.centroCustoId = centroCustoId; }
    public void setNome(String nome) { this.nome = nome; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void setEmail(String email) { this.email = email; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    public void setDataDemissao(LocalDate dataDemissao) { this.dataDemissao = dataDemissao; }
    public void setStatus(String status) { this.status = status; }
    public void setCriadoEm(Instant criadoEm) { this.criadoEm = criadoEm; }
    public void setAtualizadoEm(Instant atualizadoEm) { this.atualizadoEm = atualizadoEm; }
} 