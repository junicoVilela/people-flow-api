package com.peopleflow.pessoascontratos.inbound.events;

import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorAtivado;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorAtualizado;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorCriado;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorDemitido;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorEvent;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorExcluido;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorImportado;
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
 * Listener para eventos de Colaborador
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
public class ColaboradorEventListener {
    
    private static final Logger log = LoggerFactory.getLogger(ColaboradorEventListener.class);
    
    /**
     * Reage ao evento de criação de colaborador
     * 
     * Executado APÓS o commit da transação.
     * Casos de uso:
     * - Enviar email de boas-vindas
     * - Criar acesso aos sistemas
     * - Notificar gerente/RH
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorCriado(ColaboradorCriado event) {
        log.info("📢 EVENTO: Colaborador criado - ID: {}, Nome: {}, Email: {}, Timestamp: {}", 
                 event.colaboradorId(), 
                 event.nomeColaborador(), 
                 event.email(),
                 event.ocorridoEm());
        
        // TODO: Aqui você pode:
        // - emailService.enviarBoasVindas(event.email())
        // - sistemaAcessoService.criarUsuario(event)
        // - notificationService.notificarGerente(event)
    }
    
    /**
     * Reage ao evento de atualização de colaborador
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorAtualizado(ColaboradorAtualizado event) {
        log.info("📢 EVENTO: Colaborador atualizado - ID: {}, Nome: {}, Campos: {}, Timestamp: {}", 
                 event.colaboradorId(), 
                 event.nomeColaborador(),
                 event.camposAlterados(),
                 event.ocorridoEm());
        
        // TODO: Aqui você pode:
        // - Sincronizar com sistemas externos
        // - Notificar mudanças importantes
    }
    
    /**
     * Reage ao evento de demissão
     * 
     * Executado de forma assíncrona para não bloquear a operação principal.
     * Casos de uso:
     * - Desativar acessos
     * - Enviar email de desligamento
     * - Notificar RH
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorDemitido(ColaboradorDemitido event) {
        log.info("📢 EVENTO: Colaborador demitido - ID: {}, Nome: {}, Data Demissão: {}, Timestamp: {}", 
                 event.colaboradorId(), 
                 event.nomeColaborador(),
                 event.dataDemissao(),
                 event.ocorridoEm());
        
        // TODO: Aqui você pode (processamento assíncrono):
        // - sistemaAcessoService.desativarUsuario(event.colaboradorId())
        // - emailService.enviarEmailDesligamento(event)
        // - rhService.processarRescisao(event)
    }
    
    /**
     * Reage ao evento de ativação
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorAtivado(ColaboradorAtivado event) {
        log.info("📢 EVENTO: Colaborador ativado - ID: {}, Nome: {}, Timestamp: {}", 
                 event.colaboradorId(), 
                 event.nomeColaborador(),
                 event.ocorridoEm());
        
        // TODO: Aqui você pode:
        // - Reativar acessos
        // - Notificar equipe
    }
    
    /**
     * Reage ao evento de inativação
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorInativado(ColaboradorInativado event) {
        log.info("📢 EVENTO: Colaborador inativado - ID: {}, Nome: {}, Timestamp: {}", 
                 event.colaboradorId(), 
                 event.nomeColaborador(),
                 event.ocorridoEm());
        
        // TODO: Aqui você pode:
        // - Suspender acessos temporariamente
        // - Ajustar sistemas de ponto
    }
    
    /**
     * Reage ao evento de exclusão (assíncrono)
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorExcluido(ColaboradorExcluido event) {
        log.info("📢 EVENTO: Colaborador excluído (soft delete) - ID: {}, Nome: {}, Timestamp: {}", 
                 event.colaboradorId(), 
                 event.nomeColaborador(),
                 event.ocorridoEm());
        
        // TODO: Aqui você pode:
        // - Remover acessos permanentemente
        // - Arquivar dados
    }
    
    /**
     * Reage ao evento de transferência de colaborador
     * 
     * Executado de forma assíncrona.
     * Casos de uso:
     * - Atualizar sistemas de acesso com nova empresa/departamento
     * - Notificar gerentes (origem e destino)
     * - Ajustar permissões
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorTransferido(ColaboradorTransferido event) {
        log.info("📢 EVENTO: Colaborador transferido - ID: {}, Nome: {}, De: Empresa {} para Empresa {}, Data: {}", 
                 event.colaboradorId(), 
                 event.nomeColaborador(),
                 event.empresaAnteriorId(),
                 event.novaEmpresaId(),
                 event.dataTransferencia());
        
        // TODO: Aqui você pode (processamento assíncrono):
        // - sistemaAcessoService.atualizarPermissoes(event.colaboradorId(), event.novaEmpresaId())
        // - emailService.notificarGerenteOrigem(event)
        // - emailService.notificarGerenteDestino(event)
        // - rhService.registrarMovimentacao(event)
    }
    
    /**
     * Reage ao evento de importação de colaborador de sistema legado
     * 
     * Executado de forma assíncrona.
     * Casos de uso:
     * - Auditar qualidade dos dados importados
     * - Métricas de migração
     * - Validações pós-importação
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorImportado(ColaboradorImportado event) {
        log.info("📢 EVENTO: Colaborador importado - ID: {}, Nome: {}, Matrícula Legado: {}, Nova: {}, Status Original: {}", 
                 event.colaboradorId(), 
                 event.nomeColaborador(),
                 event.matriculaLegado(),
                 event.matriculaNova(),
                 event.statusLegado());
        
        // TODO: Aqui você pode (processamento assíncrono):
        // - qualidadeDadosService.validarImportacao(event)
        // - metricsService.incrementarContadorImportacao()
        // - alertaService.notificarSeStatusIncomum(event.statusLegado())
        // - documentacaoService.registrarOrigemDados(event)
    }
    
    /**
     * Reage ao evento de reativação de colaborador
     * 
     * Executado de forma assíncrona.
     * Casos de uso:
     * - Restaurar acessos
     * - Notificar equipe
     * - Auditar (reativações frequentes podem indicar problema)
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleColaboradorReativado(ColaboradorReativado event) {
        log.info("📢 EVENTO: Colaborador reativado - ID: {}, Nome: {}, Admissão Anterior: {}, Nova: {}", 
                 event.colaboradorId(), 
                 event.nomeColaborador(),
                 event.dataAdmissaoAnterior(),
                 event.novaDataAdmissao());
        
        // TODO: Aqui você pode (processamento assíncrono):
        // - sistemaAcessoService.restaurarAcessos(event.colaboradorId())
        // - emailService.notificarReativacao(event)
        // - auditoriaService.verificarFrequenciaReativacoes(event.colaboradorId())
        // - alertaService.notificarSeReativacaoFrequente(event)
    }
    
    /**
     * Listener genérico que captura QUALQUER evento de Colaborador
     * 
     * Útil para logging centralizado ou métricas.
     */
    @EventListener
    public void handleQualquerEventoColaborador(ColaboradorEvent event) {
        log.debug("📊 EVENTO DE DOMÍNIO: {} - Colaborador ID: {} às {}", 
                  event.getClass().getSimpleName(),
                  event.colaboradorId(),
                  event.ocorridoEm());
        
        // TODO: Aqui você pode:
        // - metricsService.registrarEvento(event)
        // - analyticsService.rastrear(event)
    }
}

