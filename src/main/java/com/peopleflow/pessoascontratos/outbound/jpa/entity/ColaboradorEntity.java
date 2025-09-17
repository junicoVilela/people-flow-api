package com.peopleflow.pessoascontratos.outbound.jpa.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "COLABORADOR", schema = "PEOPLE_FLOW_RH")
public class ColaboradorEntity {

    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "CLIENTE_ID", nullable = false)
    private UUID clienteId;

    @Column(name = "EMPRESA_ID", nullable = false)
    private UUID empresaId;

    @Column(name = "DEPARTAMENTO_ID")
    private UUID departamentoId;

    @Column(name = "CENTRO_CUSTO_ID")
    private UUID centroCustoId;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "CPF")
    private String cpf;

    @Column(name = "MATRICULA")
    private String matricula;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "DATA_ADMISSAO")
    private LocalDate dataAdmissao;

    @Column(name = "DATA_DEMISSAO")
    private LocalDate dataDemissao;

    @Column(name = "STATUS", nullable = false)
    private String status;

    @Column(name = "CRIADO_EM", nullable = false)
    private Instant criadoEm;

    @Column(name = "ATUALIZADO_EM", nullable = false)
    private Instant atualizadoEm;

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

    @PrePersist
    public void prePersist() {
        if (criadoEm == null) {
            criadoEm = Instant.now();
        }
        if (atualizadoEm == null) {
            atualizadoEm = Instant.now();
        }
        if (status == null) {
            status = "ativo";
        }
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = Instant.now();
    }
} 