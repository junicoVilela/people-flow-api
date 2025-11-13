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
@Table(name = "DOCUMENTO_COLABORADOR", schema = "PEOPLE_FLOW_RH")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DocumentoColaboradorEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_COLABORADOR_SEQ_GEN")
    @SequenceGenerator(
        name = "DOCUMENTO_COLABORADOR_SEQ_GEN",
        sequenceName = "DOCUMENTO_COLABORADOR_ID_SEQ",
        schema = "PEOPLE_FLOW_RH",
        allocationSize = 1
    )
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "TIPO", nullable = false)
    private String tipo;

    @Column(name = "MIME_TYPE", nullable = false)
    private String mimeType;

    @Column(name = "TAMANHO_BYTES", nullable = false)
    private Long tamanhoBytes;

    @Column(name = "STORAGE_KEY", nullable = false)
    private String storageKey;

    @Column(name = "VERSAO", nullable = false)
    private Integer versao;

    @Column(name = "CLIENTE_ID", nullable = false)
    private Long clienteId;

    @Column(name = "COLABORADOR_ID", nullable = false)
    private Long colaboradorId;
}