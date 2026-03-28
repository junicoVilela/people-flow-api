package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.DocumentoContratoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DocumentoContratoJpaRepository extends JpaRepository<DocumentoContratoEntity, Long>,
        JpaSpecificationExecutor<DocumentoContratoEntity> {

    Optional<DocumentoContratoEntity> findByIdAndExcluidoEmIsNull(Long id);

    long countByContratoIdAndExcluidoEmIsNull(Long contratoId);
}
