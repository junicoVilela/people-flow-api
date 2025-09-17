package com.peopleflow.pessoascontratos.inbound.web.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class ColaboradorResponse {
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

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    
    public UUID getClienteId() { return clienteId; }
    public void setClienteId(UUID clienteId) { this.clienteId = clienteId; }
    
    public UUID getEmpresaId() { return empresaId; }
    public void setEmpresaId(UUID empresaId) { this.empresaId = empresaId; }
    
    public UUID getDepartamentoId() { return departamentoId; }
    public void setDepartamentoId(UUID departamentoId) { this.departamentoId = departamentoId; }
    
    public UUID getCentroCustoId() { return centroCustoId; }
    public void setCentroCustoId(UUID centroCustoId) { this.centroCustoId = centroCustoId; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDate getDataAdmissao() { return dataAdmissao; }
    public void setDataAdmissao(LocalDate dataAdmissao) { this.dataAdmissao = dataAdmissao; }
    
    public LocalDate getDataDemissao() { return dataDemissao; }
    public void setDataDemissao(LocalDate dataDemissao) { this.dataDemissao = dataDemissao; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Instant getCriadoEm() { return criadoEm; }
    public void setCriadoEm(Instant criadoEm) { this.criadoEm = criadoEm; }
    
    public Instant getAtualizadoEm() { return atualizadoEm; }
    public void setAtualizadoEm(Instant atualizadoEm) { this.atualizadoEm = atualizadoEm; }
} 