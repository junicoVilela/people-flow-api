package com.peopleflow.pessoascontratos.core.domain.events;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Evento: Colaborador foi transferido entre empresas/departamentos
 * 
 * Casos de uso:
 * - Atualizar sistemas de acesso
 * - Notificar gerentes (antigo e novo)
 * - Ajustar permissões
 * - Registrar histórico de movimentações
 */
public record ColaboradorTransferido(
    Long colaboradorId,
    String nomeColaborador,
    Long empresaAnteriorId,
    Long novaEmpresaId,
    Long departamentoAnteriorId,
    Long novoDepartamentoId,
    LocalDate dataTransferencia,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    public ColaboradorTransferido(
            Long colaboradorId,
            String nomeColaborador,
            Long empresaAnteriorId,
            Long novaEmpresaId,
            Long departamentoAnteriorId,
            Long novoDepartamentoId,
            LocalDate dataTransferencia) {
        this(
            colaboradorId, 
            nomeColaborador, 
            empresaAnteriorId,
            novaEmpresaId,
            departamentoAnteriorId,
            novoDepartamentoId,
            dataTransferencia,
            LocalDateTime.now()
        );
    }
}

