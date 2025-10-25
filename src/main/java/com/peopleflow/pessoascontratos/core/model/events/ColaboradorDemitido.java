package com.peopleflow.pessoascontratos.core.model.events;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Evento: Colaborador foi demitido
 * 
 * Publicado quando um colaborador é desligado da empresa.
 * 
 * Casos de uso:
 * - Desativar acessos aos sistemas
 * - Enviar email de desligamento
 * - Notificar RH e gerente
 * - Processar rescisão
 * - Registrar auditoria
 * - Integrar com folha de pagamento
 */
public record ColaboradorDemitido(
    Long colaboradorId,
    String nomeColaborador,
    LocalDate dataDemissao,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    /**
     * Construtor conveniente que define o timestamp automaticamente
     */
    public ColaboradorDemitido(Long colaboradorId, String nomeColaborador, LocalDate dataDemissao) {
        this(colaboradorId, nomeColaborador, dataDemissao, LocalDateTime.now());
    }
}

