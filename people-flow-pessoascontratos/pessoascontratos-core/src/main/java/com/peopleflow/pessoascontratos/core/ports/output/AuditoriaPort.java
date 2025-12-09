package com.peopleflow.pessoascontratos.core.ports.output;

import java.time.LocalDateTime;

/**
 * Port para registro de auditoria
 * 
 * Esta interface abstrai o registro de auditoria, permitindo que o core
 * registre eventos de auditoria sem conhecer a implementação de persistência.
 */
public interface AuditoriaPort {
    
    /**
     * Registra um evento de auditoria
     * 
     * @param entidade Nome da entidade (ex: "COLABORADOR")
     * @param entidadeId ID da entidade
     * @param acao Ação realizada (ex: "ColaboradorCriado")
     * @param usuarioId ID do usuário que realizou a ação
     * @param timestamp Data e hora do evento
     * @param dadosNovos Dados novos em formato JSON (opcional)
     * @param dadosAntigos Dados antigos em formato JSON (opcional)
     * @param ipAddress Endereço IP do cliente (opcional)
     * @param userAgent User-Agent do cliente (opcional)
     * @param empresaId ID da empresa (opcional)
     */
    void registrar(
        String entidade,
        Long entidadeId,
        String acao,
        String usuarioId,
        LocalDateTime timestamp,
        String dadosNovos,
        String dadosAntigos,
        String ipAddress,
        String userAgent,
        Long empresaId
    );
}

