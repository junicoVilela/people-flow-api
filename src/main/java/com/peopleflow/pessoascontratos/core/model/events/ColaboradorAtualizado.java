package com.peopleflow.pessoascontratos.core.model.events;

import java.time.LocalDateTime;

/**
 * Evento: Colaborador teve seus dados atualizados
 * 
 * Publicado quando informações do colaborador são modificadas.
 * 
 * Casos de uso:
 * - Registrar histórico de alterações
 * - Notificar sistemas dependentes
 * - Sincronizar com sistemas externos
 * - Auditoria de mudanças
 */
public record ColaboradorAtualizado(
    Long colaboradorId,
    String nomeColaborador,
    String camposAlterados,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    /**
     * Construtor conveniente que define o timestamp automaticamente
     */
    public ColaboradorAtualizado(Long colaboradorId, String nomeColaborador, String camposAlterados) {
        this(colaboradorId, nomeColaborador, camposAlterados, LocalDateTime.now());
    }
}

