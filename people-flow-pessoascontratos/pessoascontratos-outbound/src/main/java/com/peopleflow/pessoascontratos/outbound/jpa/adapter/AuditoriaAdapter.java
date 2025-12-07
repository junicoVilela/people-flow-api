package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.pessoascontratos.core.ports.output.AuditoriaPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.AuditoriaEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.AuditoriaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Adapter que implementa AuditoriaPort usando JPA
 * 
 * Este adaptador converte chamadas do port para operações JPA,
 * permitindo que o core registre auditoria sem conhecer JPA.
 */
@Component
@RequiredArgsConstructor
public class AuditoriaAdapter implements AuditoriaPort {
    
    private final AuditoriaJpaRepository repository;
    
    @Override
    public void registrar(
            String entidade,
            Long entidadeId,
            String acao,
            String usuarioId,
            LocalDateTime timestamp,
            String dadosNovos,
            String dadosAntigos,
            String ipAddress,
            String userAgent,
            Long clienteId,
            Long empresaId) {
        
        AuditoriaEntity entity = new AuditoriaEntity();
        entity.setEntidade(entidade);
        entity.setEntidadeId(entidadeId);
        entity.setAcao(acao);
        entity.setUsuarioId(usuarioId);
        entity.setTimestamp(timestamp);
        entity.setDadosNovos(dadosNovos);
        entity.setDadosAntigos(dadosAntigos);
        entity.setIpAddress(ipAddress);
        entity.setUserAgent(userAgent);
        entity.setClienteId(clienteId);
        entity.setEmpresaId(empresaId);
        
        repository.save(entity);
    }
}

