package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.CargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CargoJpaRepository extends JpaRepository<CargoEntity, Long> {

    boolean existsByIdAndExcluidoEmIsNull(Long id);

    @Query("SELECT COUNT(c) FROM CargoEntity c WHERE c.nivelHierarquico.id = :nivelId AND c.excluidoEm IS NULL")
    long countAtivosPorNivelHierarquicoId(@Param("nivelId") Long nivelId);
}
