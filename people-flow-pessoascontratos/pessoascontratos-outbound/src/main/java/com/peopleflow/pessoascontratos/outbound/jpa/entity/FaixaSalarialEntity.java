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

import java.math.BigDecimal;

@Entity
@Table(name = "FAIXA_SALARIAL", schema = "PEOPLE_FLOW_RH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FaixaSalarialEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAIXA_SALARIAL_SEQ_GEN")
    @SequenceGenerator(
        name = "FAIXA_SALARIAL_SEQ_GEN",
        sequenceName = "FAIXA_SALARIAL_ID_SEQ",
        schema = "PEOPLE_FLOW_RH",
        allocationSize = 1
    )
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "FAIXA_MIN", nullable = false)
    private BigDecimal faixaMin;

    @Column(name = "FAIXA_MAX", nullable = false)
    private BigDecimal faixaMax;

    @Column(name = "MOEDA", nullable = false)
    private String moeda;

    @Column(name = "CLIENTE_ID", nullable = false)
    private Long clienteId;

    @Column(name = "CARGO_ID", nullable = false)
    private Long colaboradorId;
}