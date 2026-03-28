package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.NivelHierarquicoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NivelHierarquicoJpaRepository extends JpaRepository<NivelHierarquicoEntity, Long> {

    Optional<NivelHierarquicoEntity> findByIdAndExcluidoEmIsNull(Long id);

    List<NivelHierarquicoEntity> findAllByExcluidoEmIsNullOrderByOrdemAsc();

    boolean existsByNomeIgnoreCaseAndExcluidoEmIsNull(String nome);

    boolean existsByNomeIgnoreCaseAndExcluidoEmIsNullAndIdNot(String nome, Long id);
}
