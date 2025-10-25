package com.peopleflow.pessoascontratos.core.model.events;

import java.time.LocalDateTime;

/**
 * Evento: Colaborador foi criado no sistema
 * 
 * Publicado quando um novo colaborador é cadastrado.
 * 
 * Casos de uso:
 * - Enviar email de boas-vindas
 * - Criar acesso aos sistemas
 * - Notificar gerente/RH
 * - Registrar auditoria
 * - Integrar com sistemas externos
 */
public record ColaboradorCriado(
    Long colaboradorId,
    String nomeColaborador,
    String cpf,
    String email,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    /**
     * Construtor conveniente que define o timestamp automaticamente
     */
    public ColaboradorCriado(Long colaboradorId, String nomeColaborador, String cpf, String email) {
        this(colaboradorId, nomeColaborador, cpf, email, LocalDateTime.now());
    }
}

