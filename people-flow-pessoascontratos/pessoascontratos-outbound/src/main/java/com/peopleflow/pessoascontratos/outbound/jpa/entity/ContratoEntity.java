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
import java.time.LocalDate;

@Entity
@Table(name = "CONTRATO", schema = "PEOPLE_FLOW_RH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContratoEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CONTRATO_SEQ_GEN")
    @SequenceGenerator(
        name = "CONTRATO_SEQ_GEN",
        sequenceName = "CONTRATO_ID_SEQ",
        schema = "PEOPLE_FLOW_RH",
        allocationSize = 1
    )
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "TIPO")
    private String tipo;

    @Column(name = "REGIME")
    private String regime;

    @Column(name = "SALARIO_BASE")
    private BigDecimal salarioBase;

    @Column(name = "INICIO", nullable = false)
    private LocalDate inicio;

    @Column(name = "FIM")
    private LocalDate fim;

    @Column(name = "COLABORADOR_ID", nullable = false)
    private Long colaboradorId;

    @Column(name = "JORNADA_ID", nullable = false)
    private Long jornadaId;

    @Column(name = "CARGO_ID", nullable = false)
    private Long cargoId;
}