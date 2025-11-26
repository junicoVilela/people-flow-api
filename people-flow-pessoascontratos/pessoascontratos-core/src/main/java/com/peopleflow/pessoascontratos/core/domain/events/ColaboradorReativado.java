package com.peopleflow.pessoascontratos.core.domain.events;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Evento: Colaborador excluído foi reativado
 * 
 * Casos de uso:
 * - Restaurar acessos
 * - Notificar RH e gerente
 * - Reativar integrações
 * - Auditar reativações (pode indicar processos incorretos)
 */
public record ColaboradorReativado(
    Long colaboradorId,
    String nomeColaborador,
    LocalDate dataAdmissaoAnterior,
    LocalDate novaDataAdmissao,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    public ColaboradorReativado(
            Long colaboradorId,
            String nomeColaborador,
            LocalDate dataAdmissaoAnterior,
            LocalDate novaDataAdmissao) {
        this(
            colaboradorId,
            nomeColaborador,
            dataAdmissaoAnterior,
            novaDataAdmissao,
            LocalDateTime.now()
        );
    }
}

