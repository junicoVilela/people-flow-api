package com.peopleflow.pessoascontratos.inbound.events;

import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorAtivado;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorAtualizado;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorCriado;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorDemitido;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorEvent;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorExcluido;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorInativado;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorReativado;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorTransferido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

/**
 * Listener para eventos de domínio de Colaborador (logging, notificações, métricas).
 *
 * Responsabilidades:
 * - Logging de eventos de domínio
 * - Notificações (futuro)
 * - Integrações assíncronas (futuro)
 *
 * @TransactionalEventListener garante que o evento só é processado
 * APÓS o commit da transação (evita inconsistências)
 */
@Component
public class ColaboradorDomainEventListener {

    private static final Logger log = LoggerFactory.getLogger(ColaboradorDomainEventListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorCriado(ColaboradorCriado event) {
        log.info("📢 EVENTO: Colaborador criado - ID: {}, Nome: {}, Email: {}, Timestamp: {}",
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.email(),
                 event.ocorridoEm());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorAtualizado(ColaboradorAtualizado event) {
        log.info("📢 EVENTO: Colaborador atualizado - ID: {}, Nome: {}, Campos: {}, Timestamp: {}",
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.camposAlterados(),
                 event.ocorridoEm());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorDemitido(ColaboradorDemitido event) {
        log.info("📢 EVENTO: Colaborador demitido - ID: {}, Nome: {}, Data Demissão: {}, Timestamp: {}",
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.dataDemissao(),
                 event.ocorridoEm());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorAtivado(ColaboradorAtivado event) {
        log.info("📢 EVENTO: Colaborador ativado - ID: {}, Nome: {}, Timestamp: {}",
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.ocorridoEm());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorInativado(ColaboradorInativado event) {
        log.info("📢 EVENTO: Colaborador inativado - ID: {}, Nome: {}, Timestamp: {}",
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.ocorridoEm());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorExcluido(ColaboradorExcluido event) {
        log.info("📢 EVENTO: Colaborador excluído (soft delete) - ID: {}, Nome: {}, Timestamp: {}",
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.ocorridoEm());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorTransferido(ColaboradorTransferido event) {
        log.info("📢 EVENTO: Colaborador transferido - ID: {}, Nome: {}, De: Empresa {} para Empresa {}, Data: {}",
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.empresaAnteriorId(),
                 event.novaEmpresaId(),
                 event.dataTransferencia());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorReativado(ColaboradorReativado event) {
        log.info("📢 EVENTO: Colaborador reativado - ID: {}, Nome: {}, Admissão Anterior: {}, Nova: {}",
                 event.colaboradorId(),
                 event.nomeColaborador(),
                 event.dataAdmissaoAnterior(),
                 event.novaDataAdmissao());
    }

    @EventListener
    public void handleQualquerEventoColaborador(ColaboradorEvent event) {
        log.debug("📊 EVENTO DE DOMÍNIO: {} - Colaborador ID: {} às {}",
                  event.getClass().getSimpleName(),
                  event.colaboradorId(),
                  event.ocorridoEm());
    }
}
