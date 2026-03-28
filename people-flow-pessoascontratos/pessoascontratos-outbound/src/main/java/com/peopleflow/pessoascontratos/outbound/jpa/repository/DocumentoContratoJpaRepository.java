package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.DocumentoContratoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentoContratoJpaRepository extends JpaRepository<DocumentoContratoEntity, Long> {

    Optional<DocumentoContratoEntity> findByIdAndExcluidoEmIsNull(Long id);

    Page<DocumentoContratoEntity> findAllByContratoIdAndExcluidoEmIsNull(Long contratoId, Pageable pageable);

    long countByContratoIdAndExcluidoEmIsNull(Long contratoId);
}
