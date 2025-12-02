package com.peopleflow.pessoascontratos.inbound.events;

import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event Listener que recebe eventos do módulo Access Control
 * 
 * Quando um usuário Keycloak é criado para um colaborador, este listener
 * vincula o keycloakUserId ao colaborador correspondente.
 * 
 * Esta é a sincronização reversa: Access Control → Colaborador
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UsuarioKeycloakEventListener {

    private final ColaboradorUseCase colaboradorUseCase;

    /**
     * Quando um usuário Keycloak é criado, atualiza o colaborador com o keycloakUserId
     * 
     * NOTA: Este método espera receber um evento do tipo UsuarioKeycloakCriado
     * do módulo access control. Por ora, vamos usar uma abordagem genérica com Object.
     */
    @EventListener
    public void handleUsuarioKeycloakCriado(Object event) {
        try {
            // Usar reflection para obter os dados do evento
            // (necessário porque o evento está em outro módulo)
            var eventClass = event.getClass();
            
            if (!eventClass.getSimpleName().equals("UsuarioKeycloakCriado")) {
                return; // Não é o evento que esperamos
            }
            
            var keycloakUserIdMethod = eventClass.getMethod("keycloakUserId");
            var colaboradorIdMethod = eventClass.getMethod("colaboradorId");
            var emailMethod = eventClass.getMethod("email");
            
            String keycloakUserId = (String) keycloakUserIdMethod.invoke(event);
            Long colaboradorId = (Long) colaboradorIdMethod.invoke(event);
            String email = (String) emailMethod.invoke(event);
            
            log.info("🎧 Recebido evento UsuarioKeycloakCriado: colaboradorId={}, keycloakUserId={}", 
                    colaboradorId, keycloakUserId);
            
            // Buscar colaborador e vincular keycloakUserId
            var colaborador = colaboradorUseCase.buscarPorId(colaboradorId);
            var colaboradorAtualizado = colaborador.vincularUsuarioKeycloak(keycloakUserId);
            colaboradorUseCase.atualizar(colaboradorId, colaboradorAtualizado);
            
            log.info("✅ Colaborador {} vinculado ao usuário Keycloak {}", 
                    colaboradorId, keycloakUserId);
            
        } catch (NoSuchMethodException e) {
            // Evento não tem os métodos esperados, ignorar
            log.debug("Evento ignorado: {}", event.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("❌ Erro ao vincular usuário Keycloak ao colaborador: {}", 
                    e.getMessage(), e);
        }
    }
}

