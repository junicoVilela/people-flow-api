package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.JornadaTrabalhoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JornadaTrabalhoJpaRepository extends JpaRepository<JornadaTrabalhoEntity, Long> {

    boolean existsByIdAndExcluidoEmIsNull(Long id);
}
