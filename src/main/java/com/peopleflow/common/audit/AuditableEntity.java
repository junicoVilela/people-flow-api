package com.peopleflow.common.audit;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Data
@MappedSuperclass
public abstract class AuditableEntity {

    @CreatedBy
    @Column(name = "CRIADO_POR", nullable = false, updatable = false)
    private String criadoPor;

    @CreatedDate
    @Column(name = "CRIADO_EM", nullable = false, updatable = false)
    private Instant criadoEm;

    @LastModifiedBy
    @Column(name = "ATUALIZADO_POR", nullable = false)
    private String atualizadoPor;

    @LastModifiedDate
    @Column(name = "ATUALIZADO_EM", nullable = false)
    private Instant atualizadoEm;

}
