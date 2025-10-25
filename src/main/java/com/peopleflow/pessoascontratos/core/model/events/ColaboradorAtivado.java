package com.peopleflow.pessoascontratos.core.model.events;

import java.time.LocalDateTime;

/**
 * Evento: Colaborador foi ativado
 * 
 * Publicado quando um colaborador inativo ou demitido é reativado.
 * 
 * Casos de uso:
 * - Reativar acessos aos sistemas
 * - Notificar equipe
 * - Atualizar sistemas de ponto
 * - Registrar auditoria
 */
public record ColaboradorAtivado(
    Long colaboradorId,
    String nomeColaborador,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    /**
     * Construtor conveniente que define o timestamp automaticamente
     */
    public ColaboradorAtivado(Long colaboradorId, String nomeColaborador) {
        this(colaboradorId, nomeColaborador, LocalDateTime.now());
    }
}

