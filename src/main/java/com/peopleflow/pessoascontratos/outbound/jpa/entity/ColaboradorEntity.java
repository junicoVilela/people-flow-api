package com.peopleflow.pessoascontratos.outbound.jpa.entity;

import com.peopleflow.common.audit.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "COLABORADOR", schema = "PEOPLE_FLOW_RH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ColaboradorEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "CLIENTE_ID", nullable = false)
    private Long clienteId;

    @Column(name = "EMPRESA_ID", nullable = false)
    private Long empresaId;

    @Column(name = "DEPARTAMENTO_ID")
    private Long departamentoId;

    @Column(name = "CENTRO_CUSTO_ID")
    private Long centroCustoId;

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
}