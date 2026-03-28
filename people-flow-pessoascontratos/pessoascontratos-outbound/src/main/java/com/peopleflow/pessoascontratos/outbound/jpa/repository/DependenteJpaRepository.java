package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.DependenteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DependenteJpaRepository extends JpaRepository<DependenteEntity, Long>, JpaSpecificationExecutor<DependenteEntity> {

    Optional<DependenteEntity> findByIdAndExcluidoEmIsNull(Long id);

    Page<DependenteEntity> findAllByColaboradorIdAndExcluidoEmIsNull(Long colaboradorId, Pageable pageable);
}
