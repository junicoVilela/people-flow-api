package com.peopleflow.pessoascontratos.outbound.jpa.repository;

import com.peopleflow.pessoascontratos.outbound.jpa.entity.AuditoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaJpaRepository extends JpaRepository<AuditoriaEntity, Long> {
    
    List<AuditoriaEntity> findByEntidadeAndEntidadeId(String entidade, Long entidadeId);
    
    List<AuditoriaEntity> findByUsuarioId(String usuarioId);
    
    List<AuditoriaEntity> findByClienteId(Long clienteId);
    
    List<AuditoriaEntity> findByTimestampBetween(LocalDateTime inicio, LocalDateTime fim);
}

