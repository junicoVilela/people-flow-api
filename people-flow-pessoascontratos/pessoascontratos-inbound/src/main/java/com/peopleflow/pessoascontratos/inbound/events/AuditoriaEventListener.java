package com.peopleflow.pessoascontratos.inbound.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peopleflow.common.security.SecurityContextHelper;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorEvent;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.AuditoriaEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.AuditoriaJpaRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

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
@RequiredArgsConstructor
public class AuditoriaEventListener {
    
    private static final Logger log = LoggerFactory.getLogger(AuditoriaEventListener.class);
    private final AuditoriaJpaRepository auditoriaRepository;
    private final SecurityContextHelper securityHelper;
    private final ObjectMapper objectMapper;
    
    /**
     * Captura QUALQUER evento de Colaborador para auditoria
     * 
     * Este método é genérico e registra todos os tipos de eventos.
     * Executado APÓS o commit para garantir que o evento realmente aconteceu.
     * 
     * NOTA: Este método NÃO é @Async pois auditoria deve ser processada
     * de forma síncrona para garantir rastreabilidade imediata e compliance.
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void auditarEvento(ColaboradorEvent event) {
        String tipoEvento = event.getClass().getSimpleName();
        
        log.info("📝 AUDITORIA: Tipo: {}, Colaborador ID: {}, Nome: {}, Timestamp: {}", 
                 tipoEvento,
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.ocorridoEm());
        
        try {
            AuditoriaEntity auditoria = new AuditoriaEntity();
            auditoria.setEntidade("COLABORADOR");
            auditoria.setEntidadeId(event.colaboradorId());
            auditoria.setAcao(tipoEvento);
            auditoria.setUsuarioId(securityHelper.getSubject());
            auditoria.setTimestamp(event.ocorridoEm());
            
            // Obter IP e User-Agent se disponível
            ServletRequestAttributes attributes = 
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                auditoria.setIpAddress(obterIpAddress(request));
                auditoria.setUserAgent(request.getHeader("User-Agent"));
            }
            
            // Serializar dados do evento como JSON
            try {
                String dadosJson = objectMapper.writeValueAsString(event);
                auditoria.setDadosNovos(dadosJson);
            } catch (Exception e) {
                log.warn("Erro ao serializar dados do evento: {}", e.getMessage());
            }
            
            auditoriaRepository.save(auditoria);
            log.debug("✅ Registro de auditoria salvo: ID={}", auditoria.getId());
            
        } catch (Exception e) {
            log.error("❌ Erro ao salvar auditoria: {}", e.getMessage(), e);
            // Não lançar exceção para não afetar o fluxo principal
        }
    }
    
    private String obterIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
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

