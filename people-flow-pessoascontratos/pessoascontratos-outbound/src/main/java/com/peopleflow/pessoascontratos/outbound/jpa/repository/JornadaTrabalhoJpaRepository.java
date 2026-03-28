package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.JornadaTrabalhoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JornadaTrabalhoJpaRepository extends JpaRepository<JornadaTrabalhoEntity, Long> {

    boolean existsByIdAndExcluidoEmIsNull(Long id);

    Optional<JornadaTrabalhoEntity> findByIdAndExcluidoEmIsNull(Long id);

    List<JornadaTrabalhoEntity> findAllByExcluidoEmIsNullOrderByDescricaoAsc();
}
