package com.peopleflow.pessoascontratos.outbound.jpa.entity;

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
@Table(name = "CONTA_BANCARIA", schema = "PEOPLE_FLOW_RH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContaBancariaEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTA_BANCARIA_SEQ_GEN")
    @SequenceGenerator(
        name = "CONTA_BANCARIA_SEQ_GEN",
        sequenceName = "CONTA_BANCARIA_ID_SEQ",
        schema = "PEOPLE_FLOW_RH",
        allocationSize = 1
    )
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "BANCO", nullable = false)
    private String banco;

    @Column(name = "AGENCIA", nullable = false)
    private String agencia;

    @Column(name = "CONTA", nullable = false)
    private String conta;

    @Column(name = "TIPO", nullable = false)
    private String tipo;

    @Column(name = "PIX", nullable = false)
    private String pix;

    @Column(name = "COLABORADOR_ID", nullable = false)
    private Long colaboradorId;
}