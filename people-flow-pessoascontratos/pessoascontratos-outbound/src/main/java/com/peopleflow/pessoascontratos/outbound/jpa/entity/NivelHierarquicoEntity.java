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
@Table(name = "NIVEL_HIERARQUICO", schema = "PEOPLE_FLOW_RH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class NivelHierarquicoEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NIVEL_HIERARQUICO_SEQ_GEN")
    @SequenceGenerator(
            name = "NIVEL_HIERARQUICO_SEQ_GEN",
            sequenceName = "NIVEL_HIERARQUICO_ID_SEQ",
            schema = "PEOPLE_FLOW_RH",
            allocationSize = 1
    )
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "DESCRICAO", length = 500)
    private String descricao;

    @Column(name = "ORDEM", nullable = false)
    private Integer ordem;
}
