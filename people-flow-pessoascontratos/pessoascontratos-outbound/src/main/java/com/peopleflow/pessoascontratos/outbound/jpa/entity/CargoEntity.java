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
@Table(name = "CARGO", schema = "PEOPLE_FLOW_RH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CargoEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CARGO_SEQ_GEN")
    @SequenceGenerator(
        name = "CARGO_SEQ_GEN",
        sequenceName = "CARGO_ID_SEQ",
        schema = "PEOPLE_FLOW_RH",
        allocationSize = 1
    )
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "DESCRICAO")
    private String descricao;

    @Column(name = "NIVEL", nullable = false)
    private String nivel;

    @Column(name = "CLIENTE_ID", nullable = false)
    private Long clienteId;
}