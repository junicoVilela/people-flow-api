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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Listener que integra Colaborador com Keycloak: cria usuário no Keycloak
 * quando um colaborador é criado e marcado como "requer acesso ao sistema".
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ColaboradorKeycloakEventListener {

    private final UsuarioService usuarioService;
    private final AutoAtribuicaoService autoAtribuicaoService;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @EventListener
    public void handleColaboradorCriado(ColaboradorCriado event) {
        log.info("🎧 Recebido evento ColaboradorCriado: ID={}, Nome={}, RequerAcesso={}",
                event.colaboradorId(), event.nomeColaborador(), event.requerAcessoSistema());

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

            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("colaboradorId", List.of(event.colaboradorId().toString()));
            attributes.put("cpf", List.of(event.cpf()));
            if (event.empresaId() != null) {
                attributes.put("empresaId", List.of(event.empresaId().toString()));
            }

            String userId = usuarioService.criar(
                email,
                email,
                firstName,
                lastName,
                null,
                attributes
            );

            log.info("✅ Usuário {} criado no Keycloak para colaborador {}",
                    userId, event.colaboradorId());

            if (event.cargoId() != null) {
                try {
                    autoAtribuicaoService.atribuirRolesPorCargo(userId, event.cargoId());
                } catch (Exception roleEx) {
                    log.error("❌ Erro ao auto-atribuir roles para userId={}, cargoId={}: {}",
                            userId, event.cargoId(), roleEx.getMessage());
                }
            }

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
        }
    }
}
