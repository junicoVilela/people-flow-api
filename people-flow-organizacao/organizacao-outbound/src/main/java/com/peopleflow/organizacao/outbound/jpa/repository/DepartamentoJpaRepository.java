package com.peopleflow.organizacao.outbound.jpa.repository;

import com.peopleflow.organizacao.outbound.jpa.entity.DepartamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentoJpaRepository extends JpaRepository<DepartamentoEntity, Long>,
        JpaSpecificationExecutor<DepartamentoEntity> {

    boolean existsByCodigoAndEmpresaIdAndStatusNot(String codigo, Long empresaId, String status);
    boolean existsByCodigoAndEmpresaIdAndIdNotAndStatusNot(String codigo, Long empresaId, Long id, String status);

    @Modifying
    @Query("UPDATE DepartamentoEntity e SET e.status = 'excluido', e.excluidoEm = CURRENT_TIMESTAMP WHERE e.empresaId = :empresaId AND e.status != 'excluido'")
    int excluirTodosPorEmpresaId(@Param("empresaId") Long empresaId);
}
