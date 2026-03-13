package com.peopleflow.pessoascontratos.outbound.jpa.entity;

import com.peopleflow.common.audit.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "CODIGO", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "NOME", nullable = false, length = 150)
    private String nome;

    @Column(name = "DESCRICAO", length = 500)
    private String descricao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "NIVEL_HIERARQUICO_ID", nullable = false)
    private NivelHierarquicoEntity nivelHierarquico;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FAMILIA_CARGO_ID", nullable = false)
    private FamiliaCargoEntity familiaCargo;

    @Column(name = "DEPARTAMENTO_ID")
    private Long departamentoId;
}