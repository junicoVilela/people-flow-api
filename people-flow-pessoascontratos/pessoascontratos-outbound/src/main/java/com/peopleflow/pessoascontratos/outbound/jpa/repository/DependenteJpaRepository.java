package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.DependenteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DependenteJpaRepository extends JpaRepository<DependenteEntity, Long> {

    Optional<DependenteEntity> findByIdAndExcluidoEmIsNull(Long id);

    Page<DependenteEntity> findAllByColaboradorIdAndExcluidoEmIsNull(Long colaboradorId, Pageable pageable);
}
