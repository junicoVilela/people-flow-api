package com.peopleflow.organizacao.outbound.jpa.repository;

import com.peopleflow.organizacao.outbound.jpa.entity.CentroCustoEntity;
import com.peopleflow.organizacao.outbound.jpa.entity.UnidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroCustoJpaRepository extends JpaRepository<CentroCustoEntity, Long>,
        JpaSpecificationExecutor<CentroCustoEntity> {
}
