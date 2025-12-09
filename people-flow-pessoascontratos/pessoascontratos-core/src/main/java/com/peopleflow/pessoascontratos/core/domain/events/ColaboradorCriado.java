package com.peopleflow.pessoascontratos.core.domain.events;

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
    Long cargoId,
    Long departamentoId,
    Long empresaId,
    boolean requerAcessoSistema,
    LocalDateTime ocorridoEm
) implements ColaboradorEvent {
    
    /**
     * Construtor conveniente que define o timestamp automaticamente
     */
    public ColaboradorCriado(Long colaboradorId, String nomeColaborador, String cpf, String email,
                            Long cargoId, Long departamentoId, Long empresaId,
                            boolean requerAcessoSistema) {
        this(colaboradorId, nomeColaborador, cpf, email, cargoId, departamentoId, 
             empresaId, requerAcessoSistema, LocalDateTime.now());
    }
    
    /**
     * Construtor legado (compatibilidade)
     * @deprecated Use o construtor com todos os parâmetros
     */
    @Deprecated
    public ColaboradorCriado(Long colaboradorId, String nomeColaborador, String cpf, String email) {
        this(colaboradorId, nomeColaborador, cpf, email, null, null, null, false, LocalDateTime.now());
    }
}

