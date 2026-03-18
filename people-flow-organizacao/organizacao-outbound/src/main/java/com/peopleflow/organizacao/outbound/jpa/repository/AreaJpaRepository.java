package com.peopleflow.organizacao.outbound.jpa.repository;

import com.peopleflow.organizacao.outbound.jpa.entity.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaJpaRepository extends JpaRepository<AreaEntity, Long>,
        JpaSpecificationExecutor<AreaEntity> {

    boolean existsByCodigoAndStatusNot(String codigo, String status);
    boolean existsByCodigoAndIdNotAndStatusNot(String codigo, Long id, String status);
}
