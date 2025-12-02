package com.peopleflow.accesscontrol.outbound.keycloak.adapter;

import com.peopleflow.accesscontrol.core.ports.output.KeycloakRolePort;
import com.peopleflow.accesscontrol.outbound.keycloak.client.KeycloakAdminFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Adapter para operações de role no Keycloak usando Feign
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakRoleAdapter implements KeycloakRolePort {

    private final KeycloakAdminFeignClient keycloakClient;
    
    @Value("${keycloak.realm}")
    private String realm;
    
    @Value("${keycloak.client-id}")
    private String clientId;
    
    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;
    
    @Value("${keycloak.admin.password:admin}")
    private String adminPassword;

    @Cacheable(value = "keycloak-admin-token", unless = "#result == null")
    protected String getAdminToken() {
        Map<String, Object> tokenResponse = keycloakClient.getAdminToken(
            "admin-cli", adminUsername, adminPassword, "password"
        );
        return "Bearer " + tokenResponse.get("access_token");
    }

    @Override
    @Cacheable(value = "keycloak-client-uuid", key = "#root.target.clientId", unless = "#result == null")
    public String getClientUuid() {
        log.debug("Obtendo UUID do client: {}", clientId);
        
        String token = getAdminToken();
        List<Map<String, Object>> clients = keycloakClient.findClientByClientId(
            realm, clientId, token
        );
        
        if (clients.isEmpty()) {
            throw new RuntimeException("Client não encontrado: " + clientId);
        }
        
        return (String) clients.get(0).get("id");
    }

    @Override
    public void createClientRole(String roleName, String description) {
        log.info("Criando role no client: {}", roleName);
        
        String token = getAdminToken();
        String clientUuid = getClientUuid();
        
        Map<String, Object> role = Map.of(
            "name", roleName,
            "description", description != null ? description : ""
        );
        
        keycloakClient.createClientRole(realm, clientUuid, token, role);
        log.info("Role criada com sucesso: {}", roleName);
    }

    @Override
    public List<Map<String, Object>> listClientRoles() {
        log.debug("Listando roles do client");
        
        String token = getAdminToken();
        String clientUuid = getClientUuid();
        
        return keycloakClient.listClientRoles(realm, clientUuid, token);
    }

    @Override
    public Map<String, Object> findByName(String roleName) {
        log.debug("Buscando role por nome: {}", roleName);
        
        List<Map<String, Object>> roles = listClientRoles();
        return roles.stream()
            .filter(role -> roleName.equals(role.get("name")))
            .findFirst()
            .orElse(null);
    }

    @Override
    public void updateRole(String roleName, String newDescription) {
        log.info("Atualizando role: {}", roleName);
        // Keycloak Admin API não tem update direto de role
        // Precisaria deletar e recriar (não recomendado)
        log.warn("updateRole não implementado - API Keycloak não suporta atualização direta");
    }

    @Override
    public void deleteRole(String roleName) {
        log.info("Deletando role: {}", roleName);
        
        String token = getAdminToken();
        String clientUuid = getClientUuid();
        
        keycloakClient.deleteClientRole(realm, clientUuid, roleName, token);
        log.info("Role deletada com sucesso");
    }
}

