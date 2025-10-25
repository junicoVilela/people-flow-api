package com.peopleflow.pessoascontratos.inbound.events;

import com.peopleflow.pessoascontratos.core.model.events.ColaboradorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

/**
 * Listener dedicado à auditoria de eventos de Colaborador
 * 
 * Responsabilidades:
 * - Registrar todos os eventos em log de auditoria
 * - Manter histórico de mudanças
 * - Compliance e rastreabilidade
 * 
 * @Order(1) garante que auditoria é executada primeiro
 */
@Component
@Order(1) // Executa antes dos outros listeners
public class AuditoriaEventListener {
    
    private static final Logger log = LoggerFactory.getLogger(AuditoriaEventListener.class);
    
    /**
     * Captura QUALQUER evento de Colaborador para auditoria
     * 
     * Este método é genérico e registra todos os tipos de eventos.
     * Executado APÓS o commit para garantir que o evento realmente aconteceu.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void auditarEvento(ColaboradorEvent event) {
        String tipoEvento = event.getClass().getSimpleName();
        
        log.info("📝 AUDITORIA: Tipo: {}, Colaborador ID: {}, Nome: {}, Timestamp: {}", 
                 tipoEvento,
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.ocorridoEm());
        
        // TODO: Aqui você deve:
        // 1. Salvar em tabela de auditoria
        // auditoriaRepository.save(new RegistroAuditoria(
        //     entidade: "COLABORADOR",
        //     entidadeId: event.colaboradorId(),
        //     acao: tipoEvento,
        //     timestamp: event.ocorridoEm(),
        //     usuario: SecurityContextHolder.getContext().getAuthentication().getName()
        // ));
        
        // 2. Enviar para sistema de log centralizado (ELK, Splunk, etc)
        // elkService.registrar(event);
        
        // 3. Compliance e LGPD
        // lgpdService.registrarOperacao(event);
    }
    
    /**
     * Exemplo de como criar auditoria detalhada para evento específico
     */
    /*
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void auditarCriacaoDetalhada(ColaboradorCriado event) {
        log.info("📝 AUDITORIA DETALHADA - Novo colaborador cadastrado:");
        log.info("  - ID: {}", event.colaboradorId());
        log.info("  - Nome: {}", event.nomeColaborador());
        log.info("  - CPF: {}", event.cpf());
        log.info("  - Email: {}", event.email());
        log.info("  - Timestamp: {}", event.ocorridoEm());
        
        // Salvar detalhes completos em auditoria
        // auditoriaDetalhadaRepository.save(...)
    }
    */
}

