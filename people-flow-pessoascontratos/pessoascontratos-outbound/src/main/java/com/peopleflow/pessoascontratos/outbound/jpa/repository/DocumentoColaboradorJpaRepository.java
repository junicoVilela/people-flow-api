package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.DocumentoColaboradorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentoColaboradorJpaRepository extends JpaRepository<DocumentoColaboradorEntity, Long> {

    Optional<DocumentoColaboradorEntity> findByIdAndExcluidoEmIsNull(Long id);

    Page<DocumentoColaboradorEntity> findAllByColaboradorIdAndExcluidoEmIsNull(Long colaboradorId, Pageable pageable);
}
