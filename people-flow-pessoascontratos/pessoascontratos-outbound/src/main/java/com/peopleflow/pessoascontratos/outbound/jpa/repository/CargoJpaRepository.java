package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.CargoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CargoJpaRepository extends JpaRepository<CargoEntity, Long>, JpaSpecificationExecutor<CargoEntity> {

    boolean existsByIdAndExcluidoEmIsNull(Long id);

    Optional<CargoEntity> findByIdAndExcluidoEmIsNull(Long id);

    Page<CargoEntity> findAllByExcluidoEmIsNull(Pageable pageable);

    boolean existsByCodigoIgnoreCaseAndExcluidoEmIsNull(String codigo);

    boolean existsByCodigoIgnoreCaseAndExcluidoEmIsNullAndIdNot(String codigo, Long id);

    @Query("SELECT COUNT(c) FROM CargoEntity c WHERE c.nivelHierarquico.id = :nivelId AND c.excluidoEm IS NULL")
    long countAtivosPorNivelHierarquicoId(@Param("nivelId") Long nivelId);

    @Query("SELECT COUNT(c) FROM CargoEntity c WHERE c.familiaCargo.id = :familiaId AND c.excluidoEm IS NULL")
    long countAtivosPorFamiliaCargoId(@Param("familiaId") Long familiaId);

    @Query("SELECT COUNT(ct) FROM ContratoEntity ct WHERE ct.cargoId = :cargoId AND ct.excluidoEm IS NULL")
    long countContratosAtivosPorCargoId(@Param("cargoId") Long cargoId);
}
