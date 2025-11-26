package com.peopleflow.pessoascontratos.core.domain.events;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Evento: Colaborador foi importado de sistema legado
 * 
 * Casos de uso:
 * - Auditar importações
 * - Validar qualidade dos dados migrados
 * - Métricas de migração
 * - Notificar responsáveis pela migração
 */
public record ColaboradorImportado(
    Long colaboradorId,
    String nomeColaborador,
    String matriculaLegado,
    String matriculaNova,
    String statusLegado,
    LocalDate dataAdmissaoOriginal,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    public ColaboradorImportado(
            Long colaboradorId,
            String nomeColaborador,
            String matriculaLegado,
            String matriculaNova,
            String statusLegado,
            LocalDate dataAdmissaoOriginal) {
        this(
            colaboradorId,
            nomeColaborador,
            matriculaLegado,
            matriculaNova,
            statusLegado,
            dataAdmissaoOriginal,
            LocalDateTime.now()
        );
    }
}

