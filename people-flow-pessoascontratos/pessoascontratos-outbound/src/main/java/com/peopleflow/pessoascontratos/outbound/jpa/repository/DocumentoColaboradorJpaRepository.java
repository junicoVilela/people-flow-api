package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.DocumentoColaboradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DocumentoColaboradorJpaRepository extends JpaRepository<DocumentoColaboradorEntity, Long>,
        JpaSpecificationExecutor<DocumentoColaboradorEntity> {

    Optional<DocumentoColaboradorEntity> findByIdAndExcluidoEmIsNull(Long id);
}
