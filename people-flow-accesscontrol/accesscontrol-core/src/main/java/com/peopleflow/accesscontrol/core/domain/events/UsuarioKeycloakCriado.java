package com.peopleflow.accesscontrol.core.domain.events;

import java.time.LocalDateTime;

/**
 * Evento publicado quando um usuário é criado no Keycloak para um colaborador
 * 
 * Este evento é usado para sincronização reversa: quando o módulo Access Control
 * cria um usuário no Keycloak, ele publica este evento para que o módulo de
 * Colaborador possa vincular o keycloakUserId ao colaborador correspondente.
 */
public record UsuarioKeycloakCriado(
    String keycloakUserId,
    Long colaboradorId,
    String email,
    LocalDateTime ocorridoEm
) {
    /**
     * Construtor conveniente que define o timestamp automaticamente
     */
    public UsuarioKeycloakCriado(String keycloakUserId, Long colaboradorId, String email) {
        this(keycloakUserId, colaboradorId, email, LocalDateTime.now());
    }
}

