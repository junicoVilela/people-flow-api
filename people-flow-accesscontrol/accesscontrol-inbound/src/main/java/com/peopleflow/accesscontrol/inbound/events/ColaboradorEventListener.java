package com.peopleflow.accesscontrol.inbound.events;

import com.peopleflow.accesscontrol.core.application.AutoAtribuicaoService;
import com.peopleflow.accesscontrol.core.application.UsuarioService;
import com.peopleflow.accesscontrol.core.domain.events.UsuarioKeycloakCriado;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorCriado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Event Listener que automatiza criação de usuários no Keycloak
 * quando colaboradores são criados no sistema
 * 
 * IMPORTANTE: Esta é a automação que integra o módulo de Colaborador com Keycloak
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ColaboradorEventListener {

    private final UsuarioService usuarioService;
    private final AutoAtribuicaoService autoAtribuicaoService;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * Quando um colaborador é criado, cria automaticamente um usuário no Keycloak
     * 
     * O usuário será criado com:
     * - username = email do colaborador
     * - firstName = nome
     * - lastName = sobrenome
     * - atributos customizados: empresaId, colaboradorId
     */
    @Async
    @EventListener
    public void handleColaboradorCriado(ColaboradorCriado event) {
        log.info("🎧 Recebido evento ColaboradorCriado: ID={}, Nome={}, RequerAcesso={}", 
                event.colaboradorId(), event.nomeColaborador(), event.requerAcessoSistema());
        
        // ✅ Verificar se o colaborador requer acesso ao sistema
        if (!event.requerAcessoSistema()) {
            log.info("Colaborador {} não requer acesso ao sistema. Usuário não será criado.", 
                    event.colaboradorId());
            return;
        }
        
        try {
            String email = event.email();
            String[] nomes = event.nomeColaborador().split(" ", 2);
            String firstName = nomes[0];
            String lastName = nomes.length > 1 ? nomes[1] : "";
            
            Map<String, Object> existingUser = usuarioService.buscarPorUsername(email);
            if (existingUser != null) {

                String userId = (String) existingUser.get("id");
                
                log.warn("⚠️ Usuário já existe no Keycloak: {}. Vinculando ao colaborador...", email);
                
                usuarioService.atualizarAtributo(
                    userId, 
                    "colaboradorId", 
                    event.colaboradorId().toString()
                );
                
                eventPublisher.publishEvent(
                    new UsuarioKeycloakCriado(userId, event.colaboradorId(), email)
                );
                
                log.info("✅ Usuário existente {} vinculado ao colaborador {}", 
                        userId, event.colaboradorId());
                return;
            }
            
            Map<String, List<String>> attributes = Map.of(
                "colaboradorId", List.of(event.colaboradorId().toString()),
                "cpf", List.of(event.cpf()),
                "empresaId", List.of(event.empresaId().toString())
            );
            
            String userId = usuarioService.criar(
                email,           // username = email
                email,           // email
                firstName,       // nome
                lastName,        // sobrenome
                null,            // senha será definida via email
                attributes       // atributos customizados
            );
            
            log.info("✅ Usuário {} criado no Keycloak para colaborador {}", 
                    userId, event.colaboradorId());
            
            // Auto-atribuição de roles por cargo
            if (event.cargoId() != null) {
                try {
                    autoAtribuicaoService.atribuirRolesPorCargo(userId, event.cargoId());
                } catch (Exception roleEx) {
                    log.error("❌ Erro ao auto-atribuir roles para userId={}, cargoId={}: {}", 
                            userId, event.cargoId(), roleEx.getMessage());
                }
            }
            
            // Auto-atribuição de grupo por departamento
            if (event.departamentoId() != null) {
                try {
                    autoAtribuicaoService.atribuirGrupoPorDepartamento(userId, event.departamentoId());
                } catch (Exception groupEx) {
                    log.error("❌ Erro ao auto-atribuir grupo para userId={}, departamentoId={}: {}", 
                            userId, event.departamentoId(), groupEx.getMessage());
                }
            }
            
            try {
                usuarioService.enviarEmailDefinirSenha(userId);
                log.info("📧 Email de configuração de senha enviado para {}", email);
            } catch (Exception e) {
                log.warn("⚠️ Erro ao enviar email de senha para {}: {}", 
                        email, e.getMessage());
            }
            
            eventPublisher.publishEvent(
                new UsuarioKeycloakCriado(userId, event.colaboradorId(), email)
            );
            
            log.info("✅ Processamento completo para colaborador {}", event.colaboradorId());
            
        } catch (Exception e) {
            log.error("❌ Erro ao criar usuário no Keycloak para colaborador ID={}: {}", 
                     event.colaboradorId(), e.getMessage(), e);
            
            // Não lançar exceção para não afetar o fluxo principal
            // Em produção, considere enviar para uma fila de retry
        }
    }
}

