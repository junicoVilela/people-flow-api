package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ColaboradorJpaRepository extends JpaRepository<ColaboradorEntity, Long>, 
                                                   JpaSpecificationExecutor<ColaboradorEntity> {
    
    boolean existsByCpfAndStatusNot(String cpf, String status);
    boolean existsByEmailAndStatusNot(String email, String status);
    boolean existsByMatriculaAndEmpresaIdAndStatusNot(String matricula, Long empresaId, String status);
    
    boolean existsByCpfAndIdNotAndStatusNot(String cpf, Long id, String status);
    boolean existsByEmailAndIdNotAndStatusNot(String email, Long id, String status);
    boolean existsByMatriculaAndEmpresaIdAndIdNotAndStatusNot(String matricula, Long empresaId, Long id, String status);

    @Query("SELECT COUNT(c) FROM ColaboradorEntity c WHERE c.cargoId = :cargoId AND c.excluidoEm IS NULL AND c.status <> 'excluido'")
    long countAtivosPorCargoId(@Param("cargoId") Long cargoId);
}