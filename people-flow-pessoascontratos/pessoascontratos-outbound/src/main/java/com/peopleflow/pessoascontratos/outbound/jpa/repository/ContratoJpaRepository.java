package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.ContratoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContratoJpaRepository extends JpaRepository<ContratoEntity, Long> {

    Optional<ContratoEntity> findByIdAndExcluidoEmIsNull(Long id);

    Page<ContratoEntity> findAllByColaboradorIdAndExcluidoEmIsNull(Long colaboradorId, Pageable pageable);

    @Query("SELECT COUNT(c) FROM ContratoEntity c WHERE c.jornadaId = :jornadaId AND c.excluidoEm IS NULL")
    long countAtivosPorJornadaId(@Param("jornadaId") Long jornadaId);
}
