package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ColaboradorJpaRepository extends JpaRepository<ColaboradorEntity, Long>, 
                                                   JpaSpecificationExecutor<ColaboradorEntity> {
    
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByMatricula(String matricula);
    
    boolean existsByCpfAndIdNot(String cpf, Long id);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByMatriculaAndIdNot(String matricula, Long id);
} 