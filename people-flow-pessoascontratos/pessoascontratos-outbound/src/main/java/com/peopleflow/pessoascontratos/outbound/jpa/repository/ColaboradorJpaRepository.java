package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
} 