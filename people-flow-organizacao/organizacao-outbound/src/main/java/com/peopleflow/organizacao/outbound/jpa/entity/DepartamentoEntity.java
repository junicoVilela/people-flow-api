package com.peopleflow.organizacao.outbound.jpa.entity;

import com.peopleflow.common.audit.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "DEPARTAMENTO", schema = "PEOPLE_FLOW_RH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DepartamentoEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEPARTAMENTO_SEQ_GEN")
    @SequenceGenerator(
        name = "DEPARTAMENTO_SEQ_GEN",
        sequenceName = "DEPARTAMENTO_ID_SEQ",
        schema = "PEOPLE_FLOW_RH",
        allocationSize = 1
    )
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "CODIGO")
    private String codigo;

    @Column(name = "EMPRESA_ID", nullable = false)
    private Long empresaId;

    @Column(name = "UNIDADE_ID")
    private Long unidadeId;

    @Column(name = "STATUS", nullable = false)
    private String status;
}