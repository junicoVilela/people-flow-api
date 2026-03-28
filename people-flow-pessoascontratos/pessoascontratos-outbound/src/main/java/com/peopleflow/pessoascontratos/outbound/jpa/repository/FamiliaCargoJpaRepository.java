package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.FamiliaCargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FamiliaCargoJpaRepository extends JpaRepository<FamiliaCargoEntity, Long> {

    Optional<FamiliaCargoEntity> findByIdAndExcluidoEmIsNull(Long id);

    List<FamiliaCargoEntity> findAllByExcluidoEmIsNullOrderByNomeAsc();

    boolean existsByNomeIgnoreCaseAndExcluidoEmIsNull(String nome);

    boolean existsByNomeIgnoreCaseAndExcluidoEmIsNullAndIdNot(String nome, Long id);

    boolean existsByIdAndExcluidoEmIsNull(Long id);
}
