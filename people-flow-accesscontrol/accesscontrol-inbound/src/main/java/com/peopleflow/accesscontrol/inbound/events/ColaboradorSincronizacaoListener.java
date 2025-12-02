package com.peopleflow.accesscontrol.inbound.events;

import com.peopleflow.accesscontrol.core.application.UsuarioService;
import com.peopleflow.pessoascontratos.core.domain.events.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Event Listener que sincroniza mudanças de status do Colaborador com o Keycloak
 * 
 * Mantém consistência entre os domínios: quando um colaborador é inativado,
 * demitido ou excluído, o usuário correspondente no Keycloak também é afetado.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ColaboradorSincronizacaoListener {

    private final UsuarioService usuarioService;

    /**
     * Quando um colaborador é inativado, desativa o usuário no Keycloak
     */
    @Async
    @EventListener
    public void handleColaboradorInativado(ColaboradorInativado event) {
        log.info("🎧 Recebido evento ColaboradorInativado: ID={}", event.colaboradorId());
        
        String userId = buscarUsuarioIdPorColaborador(event.colaboradorId());
        if (userId != null) {
            try {
                usuarioService.desativar(userId);
                log.info("✅ Usuário {} desativado (colaborador inativado)", userId);
            } catch (Exception e) {
                log.error("❌ Erro ao desativar usuário {}: {}", userId, e.getMessage());
            }
        } else {
            log.debug("Colaborador {} não possui usuário vinculado", event.colaboradorId());
        }
    }

    /**
     * Quando um colaborador é ativado, reativa o usuário no Keycloak
     */
    @Async
    @EventListener
    public void handleColaboradorAtivado(ColaboradorAtivado event) {
        log.info("🎧 Recebido evento ColaboradorAtivado: ID={}", event.colaboradorId());
        
        String userId = buscarUsuarioIdPorColaborador(event.colaboradorId());
        if (userId != null) {
            try {
                usuarioService.ativar(userId);
                log.info("✅ Usuário {} reativado (colaborador ativado)", userId);
            } catch (Exception e) {
                log.error("❌ Erro ao reativar usuário {}: {}", userId, e.getMessage());
            }
        }
    }

    /**
     * Quando um colaborador é reativado, reativa o usuário no Keycloak
     */
    @Async
    @EventListener
    public void handleColaboradorReativado(ColaboradorReativado event) {
        log.info("🎧 Recebido evento ColaboradorReativado: ID={}", event.colaboradorId());
        
        String userId = buscarUsuarioIdPorColaborador(event.colaboradorId());
        if (userId != null) {
            try {
                usuarioService.ativar(userId);
                usuarioService.atualizarAtributo(userId, "reativadoEm", 
                        event.ocorridoEm().toString());
                log.info("✅ Usuário {} reativado (colaborador reativado)", userId);
            } catch (Exception e) {
                log.error("❌ Erro ao reativar usuário {}: {}", userId, e.getMessage());
            }
        }
    }

    /**
     * Quando um colaborador é demitido, desativa o usuário e registra a demissão
     */
    @Async
    @EventListener
    public void handleColaboradorDemitido(ColaboradorDemitido event) {
        log.info("🎧 Recebido evento ColaboradorDemitido: ID={}, Data={}", 
                event.colaboradorId(), event.dataDemissao());
        
        String userId = buscarUsuarioIdPorColaborador(event.colaboradorId());
        if (userId != null) {
            try {
                // Desativar usuário
                usuarioService.desativar(userId);
                
                // Registrar data de demissão nos atributos
                usuarioService.atualizarAtributo(userId, "dataDemissao", 
                        event.dataDemissao().toString());
                usuarioService.atualizarAtributo(userId, "demitidoEm", 
                        event.ocorridoEm().toString());
                
                log.info("✅ Usuário {} desativado (colaborador demitido em {})", 
                        userId, event.dataDemissao());
            } catch (Exception e) {
                log.error("❌ Erro ao processar demissão do usuário {}: {}", 
                        userId, e.getMessage());
            }
        }
    }

    /**
     * Quando um colaborador é excluído, desativa o usuário e marca como excluído
     * Não deleta o usuário para manter histórico de auditoria
     */
    @Async
    @EventListener
    public void handleColaboradorExcluido(ColaboradorExcluido event) {
        log.info("🎧 Recebido evento ColaboradorExcluido: ID={}", event.colaboradorId());
        
        String userId = buscarUsuarioIdPorColaborador(event.colaboradorId());
        if (userId != null) {
            try {
                // Desativar (não deletar) para manter histórico
                usuarioService.desativar(userId);
                
                // Marcar como excluído nos atributos
                usuarioService.atualizarAtributo(userId, "excluido", "true");
                usuarioService.atualizarAtributo(userId, "excluidoEm", 
                        event.ocorridoEm().toString());
                
                log.info("✅ Usuário {} desativado e marcado como excluído", userId);
            } catch (Exception e) {
                log.error("❌ Erro ao processar exclusão do usuário {}: {}", 
                        userId, e.getMessage());
            }
        }
    }

    /**
     * Quando um colaborador é atualizado, sincroniza dados no Keycloak (se necessário)
     */
    @Async
    @EventListener
    public void handleColaboradorAtualizado(ColaboradorAtualizado event) {
        log.debug("🎧 Recebido evento ColaboradorAtualizado: ID={}, Campos={}", 
                event.colaboradorId(), event.camposAlterados());
        
        // Por enquanto, apenas log. Implementar sincronização de dados específicos
        // (email, nome) se necessário no futuro
        // Isso requer adicionar mais informações ao evento ColaboradorAtualizado
    }

    /**
     * Busca o ID do usuário Keycloak associado a um colaborador
     */
    private String buscarUsuarioIdPorColaborador(Long colaboradorId) {
        try {
            List<Map<String, Object>> users = usuarioService.buscarPorAtributo(
                "colaboradorId", 
                colaboradorId.toString()
            );
            
            if (users.isEmpty()) {
                return null;
            }
            
            return (String) users.get(0).get("id");
        } catch (Exception e) {
            log.error("❌ Erro ao buscar usuário por colaboradorId {}: {}", 
                    colaboradorId, e.getMessage());
            return null;
        }
    }
}

