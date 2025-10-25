package com.peopleflow.pessoascontratos.core.model.events;

import java.time.LocalDateTime;

/**
 * Evento: Colaborador foi inativado
 * 
 * Publicado quando um colaborador é temporariamente inativado
 * (férias prolongadas, afastamento, licença, etc).
 * 
 * Casos de uso:
 * - Suspender acessos temporariamente
 * - Notificar equipe
 * - Ajustar sistemas de ponto
 * - Registrar auditoria
 */
public record ColaboradorInativado(
    Long colaboradorId,
    String nomeColaborador,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    /**
     * Construtor conveniente que define o timestamp automaticamente
     */
    public ColaboradorInativado(Long colaboradorId, String nomeColaborador) {
        this(colaboradorId, nomeColaborador, LocalDateTime.now());
    }
}

