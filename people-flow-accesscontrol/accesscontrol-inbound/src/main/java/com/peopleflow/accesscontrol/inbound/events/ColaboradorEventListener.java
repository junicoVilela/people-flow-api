package com.peopleflow.accesscontrol.inbound.events;

import com.peopleflow.accesscontrol.core.application.UsuarioService;
import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorCriado;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * Quando um colaborador é criado, cria automaticamente um usuário no Keycloak
     * 
     * O usuário será criado com:
     * - username = email do colaborador
     * - firstName = nome
     * - lastName = sobrenome
     * - atributos customizados: clienteId, empresaId, colaboradorId
     */
    @Async
    @EventListener
    public void handleColaboradorCriado(ColaboradorCriado event) {
        log.info("🎧 Recebido evento ColaboradorCriado: ID={}, Nome={}", 
                event.colaboradorId(), event.nomeColaborador());
        
        try {
            // Extrair dados do evento
            String email = event.email();
            String[] nomes = event.nomeColaborador().split(" ", 2);
            String firstName = nomes[0];
            String lastName = nomes.length > 1 ? nomes[1] : "";
            
            // Verificar se usuário já existe
            Map<String, Object> existingUser = usuarioService.buscarPorUsername(email);
            if (existingUser != null) {
                log.warn("⚠️ Usuário já existe no Keycloak: {}", email);
                return;
            }
            
            // Atributos customizados para rastreamento
            Map<String, List<String>> attributes = Map.of(
                "colaboradorId", List.of(event.colaboradorId().toString()),
                "cpf", List.of(event.cpf())
            );
            
            // Criar usuário no Keycloak
            String userId = usuarioService.criar(
                email,           // username = email
                email,           // email
                firstName,       // nome
                lastName,        // sobrenome
                null,            // senha será definida depois (reset password)
                attributes       // atributos customizados
            );
            
            log.info("✅ Usuário criado no Keycloak com sucesso! userId={}, email={}", 
                    userId, email);
            
            // TODO: Adicionar usuário ao grupo padrão baseado no cargo/departamento
            // TODO: Enviar email para colaborador definir senha inicial
            
        } catch (Exception e) {
            log.error("❌ Erro ao criar usuário no Keycloak para colaborador ID={}", 
                     event.colaboradorId(), e);
            
            // Não lançar exceção para não afetar o fluxo principal
            // Em produção, considere enviar para uma fila de retry
        }
    }
}

