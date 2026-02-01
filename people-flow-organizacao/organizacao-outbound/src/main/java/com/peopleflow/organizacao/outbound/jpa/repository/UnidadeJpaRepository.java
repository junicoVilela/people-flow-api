package com.peopleflow.organizacao.outbound.jpa.repository;

import com.peopleflow.organizacao.outbound.jpa.entity.UnidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeJpaRepository extends JpaRepository<UnidadeEntity, Long>,
        JpaSpecificationExecutor<UnidadeEntity> {

    boolean existsByCodigoAndStatusNot(String codigo, String status);
    boolean existsByCodigoAndIdNotAndStatusNot(String codigo, Long id, String status);
}
