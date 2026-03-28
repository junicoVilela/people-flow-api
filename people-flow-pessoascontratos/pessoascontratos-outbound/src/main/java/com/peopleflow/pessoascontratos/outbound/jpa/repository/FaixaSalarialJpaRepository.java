package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.FaixaSalarialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FaixaSalarialJpaRepository extends JpaRepository<FaixaSalarialEntity, Long> {

    Optional<FaixaSalarialEntity> findByIdAndExcluidoEmIsNull(Long id);

    Page<FaixaSalarialEntity> findAllByCargoIdAndExcluidoEmIsNull(Long cargoId, Pageable pageable);

    long countByCargoIdAndExcluidoEmIsNull(Long cargoId);
}
