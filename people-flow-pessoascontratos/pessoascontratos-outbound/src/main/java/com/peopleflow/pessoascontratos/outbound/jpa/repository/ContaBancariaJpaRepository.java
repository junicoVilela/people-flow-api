package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.ContaBancariaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ContaBancariaJpaRepository extends JpaRepository<ContaBancariaEntity, Long>,
        JpaSpecificationExecutor<ContaBancariaEntity> {

    Optional<ContaBancariaEntity> findByIdAndExcluidoEmIsNull(Long id);

    Page<ContaBancariaEntity> findAllByColaboradorIdAndExcluidoEmIsNull(Long colaboradorId, Pageable pageable);
}
