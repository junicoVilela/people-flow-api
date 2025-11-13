package com.peopleflow.pessoascontratos.core.domain.events;

import java.time.LocalDateTime;

/**
 * Evento: Colaborador foi excluído (soft delete)
 * 
 * Publicado quando um colaborador é marcado como excluído no sistema.
 * Diferente de deletar fisicamente, é uma exclusão lógica.
 * 
 * Casos de uso:
 * - Remover acessos permanentemente
 * - Arquivar dados
 * - Notificar sistemas de que o registro não é mais válido
 * - Registrar auditoria
 */
public record ColaboradorExcluido(
    Long colaboradorId,
    String nomeColaborador,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    /**
     * Construtor conveniente que define o timestamp automaticamente
     */
    public ColaboradorExcluido(Long colaboradorId, String nomeColaborador) {
        this(colaboradorId, nomeColaborador, LocalDateTime.now());
    }
}

