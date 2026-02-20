package com.peopleflow.accesscontrol.outbound.keycloak.adapter;

import com.peopleflow.accesscontrol.core.ports.output.KeycloakGrupoPort;
import com.peopleflow.accesscontrol.outbound.keycloak.client.KeycloakAdminFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakGrupoAdapter implements KeycloakGrupoPort {

    private final KeycloakAdminFeignClient keycloakClient;
    
    @Value("${keycloak.realm}")
    private String realm;
    
    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;
    
    @Value("${keycloak.admin.password:admin}")
    private String adminPassword;

    @Cacheable(value = "keycloak-admin-token", unless = "#result == null")
    protected String getAdminToken() {
        Map<String, ?> form = Map.of(
            "client_id", "admin-cli",
            "username", adminUsername,
            "password", adminPassword,
            "grant_type", "password"
        );
        Map<String, Object> tokenResponse = keycloakClient.getAdminToken(form);
        return "Bearer " + tokenResponse.get("access_token");
    }

    @Override
    public String createGroup(String name) {
        log.info("Criando grupo no Keycloak: {}", name);
        
        String token = getAdminToken();
        Map<String, Object> group = Map.of("name", name);
        
        keycloakClient.createGroup(realm, token, group);
        
        // Buscar grupo criado para obter ID
        List<Map<String, Object>> groups = findByName(name);
        
        if (groups.isEmpty()) {
            throw new RuntimeException("Grupo criado mas não encontrado: " + name);
        }
        
        String groupId = (String) groups.get(0).get("id");
        log.info("Grupo criado com sucesso. ID: {}", groupId);
        
        return groupId;
    }

    @Override
    public List<Map<String, Object>> listAll() {
        log.debug("Listando todos os grupos");
        
        String token = getAdminToken();
        return keycloakClient.listGroups(realm, token);
    }

    @Override
    public Map<String, Object> findById(String groupId) {
        log.debug("Buscando grupo por ID: {}", groupId);
        
        String token = getAdminToken();
        return keycloakClient.getGroup(realm, groupId, token);
    }

    @Override
    public List<Map<String, Object>> findByName(String name) {
        log.debug("Buscando grupos por nome: {}", name);
        
        String token = getAdminToken();
        List<Map<String, Object>> allGroups = keycloakClient.listGroups(realm, token);
        
        // Filtrar por nome
        return allGroups.stream()
            .filter(group -> name.equals(group.get("name")))
            .toList();
    }

    @Override
    public void updateGroup(String groupId, String newName) {
        log.info("Atualizando grupo: {} para {}", groupId, newName);
        
        String token = getAdminToken();
        Map<String, Object> group = Map.of("name", newName);
        
        keycloakClient.updateGroup(realm, groupId, token, group);
        log.info("Grupo atualizado com sucesso");
    }

    @Override
    public void deleteGroup(String groupId) {
        log.info("Deletando grupo: {}", groupId);
        
        String token = getAdminToken();
        keycloakClient.deleteGroup(realm, groupId, token);
        
        log.info("Grupo deletado com sucesso");
    }

    @Override
    public List<Map<String, Object>> getMembers(String groupId) {
        log.debug("Listando membros do grupo: {}", groupId);
        
        String token = getAdminToken();
        return keycloakClient.getGroupMembers(realm, groupId, token);
    }

    @Override
    public void assignRoleToGroup(String groupId, String roleName) {
        log.info("Atribuindo role {} ao grupo {}", roleName, groupId);
        // Implementação específica via API do Keycloak
        // Requer múltiplas chamadas - pode ser implementado depois
        log.warn("assignRoleToGroup ainda não implementado via Feign");
    }

    @Override
    public void removeRoleFromGroup(String groupId, String roleName) {
        log.info("Removendo role {} do grupo {}", roleName, groupId);
        // Implementação específica via API do Keycloak
        log.warn("removeRoleFromGroup ainda não implementado via Feign");
    }
}

