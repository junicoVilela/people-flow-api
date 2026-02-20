package com.peopleflow.accesscontrol.outbound.keycloak.adapter;

import com.peopleflow.accesscontrol.core.ports.output.KeycloakUsuarioPort;
import com.peopleflow.accesscontrol.outbound.keycloak.client.KeycloakAdminFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KeycloakUsuarioAdapter implements KeycloakUsuarioPort {

    private final KeycloakAdminFeignClient keycloakClient;
    
    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String keycloakClientId;

    @Value("${keycloak.admin.username:admin}")
    private String adminUsername;
    
    @Value("${keycloak.admin.password:admin}")
    private String adminPassword;

    @Cacheable(value = "keycloak-admin-token", unless = "#result == null")
    private String getAdminToken() {
        log.debug("Obtendo token de administrador do Keycloak");
        
        Map<String, Object> tokenResponse = keycloakClient.getAdminToken(
            "admin-cli",
            adminUsername,
            adminPassword,
            "password"
        );
        
        String accessToken = (String) tokenResponse.get("access_token");
        return "Bearer " + accessToken;
    }

    @Override
    public String createUser(String username, String email, String firstName, 
                            String lastName, Map<String, List<String>> attributes) {
        
        log.info("Criando usuário no Keycloak: {}", username);
        
        String token = getAdminToken();
        
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("enabled", true);
        user.put("emailVerified", true);
        
        if (attributes != null && !attributes.isEmpty()) {
            user.put("attributes", attributes);
        }
        
        keycloakClient.createUser(realm, token, user);
        
        List<Map<String, Object>> users = keycloakClient.searchUsers(
            realm, username, true, token
        );
        
        if (users.isEmpty()) {
            throw new RuntimeException("Usuário criado mas não encontrado: " + username);
        }
        
        String userId = (String) users.get(0).get("id");
        log.info("Usuário criado com sucesso. ID: {}", userId);
        
        return userId;
    }

    @Override
    public void setPassword(String userId, String password, boolean temporary) {
        log.info("Definindo senha para usuário: {}", userId);
        
        String token = getAdminToken();
        
        Map<String, Object> credential = Map.of(
            "type", "password",
            "value", password,
            "temporary", temporary
        );
        
        keycloakClient.resetPassword(realm, userId, token, credential);
        
        log.info("Senha definida com sucesso");
    }

    @Override
    public Map<String, Object> findByUsername(String username) {
        log.debug("Buscando usuário por username: {}", username);
        
        String token = getAdminToken();
        List<Map<String, Object>> users = keycloakClient.searchUsers(
            realm, username, true, token
        );
        
        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public Map<String, Object> findById(String userId) {
        log.debug("Buscando usuário por ID: {}", userId);
        
        String token = getAdminToken();
        return keycloakClient.getUser(realm, userId, token);
    }

    @Override
    public List<Map<String, Object>> listAll() {
        log.debug("Listando todos os usuários");
        
        String token = getAdminToken();
        return keycloakClient.listUsers(realm, token);
    }

    @Override
    public List<Map<String, Object>> findByAttribute(String attributeName, String attributeValue) {
        log.debug("Buscando usuários por atributo: {}={}", attributeName, attributeValue);
        
        String token = getAdminToken();
        List<Map<String, Object>> allUsers = keycloakClient.listUsers(realm, token);
        
        return allUsers.stream()
            .filter(user -> {
                @SuppressWarnings("unchecked")
                Map<String, List<String>> attributes = 
                    (Map<String, List<String>>) user.get("attributes");
                
                if (attributes == null) {
                    return false;
                }
                
                List<String> values = attributes.get(attributeName);
                return values != null && values.contains(attributeValue);
            })
            .collect(Collectors.toList());
    }

    @Override
    public void updateUser(String userId, Map<String, Object> userData) {
        log.info("Atualizando usuário: {}", userId);
        
        String token = getAdminToken();
        keycloakClient.updateUser(realm, userId, token, userData);
        
        log.info("Usuário atualizado com sucesso");
    }

    @Override
    public void enableUser(String userId) {
        log.info("Ativando usuário: {}", userId);
        
        String token = getAdminToken();
        Map<String, Object> user = Map.of("enabled", true);
        keycloakClient.updateUser(realm, userId, token, user);
    }

    @Override
    public void disableUser(String userId) {
        log.info("Desativando usuário: {}", userId);
        
        String token = getAdminToken();
        Map<String, Object> user = Map.of("enabled", false);
        keycloakClient.updateUser(realm, userId, token, user);
    }

    @Override
    public void deleteUser(String userId) {
        log.info("Deletando usuário: {}", userId);
        
        String token = getAdminToken();
        keycloakClient.deleteUser(realm, userId, token);
        
        log.info("Usuário deletado com sucesso");
    }

    @Override
    public void addToGroup(String userId, String groupId) {
        log.info("Adicionando usuário {} ao grupo {}", userId, groupId);
        
        String token = getAdminToken();
        keycloakClient.addUserToGroup(realm, userId, groupId, token);
    }

    @Override
    public void removeFromGroup(String userId, String groupId) {
        log.info("Removendo usuário {} do grupo {}", userId, groupId);
        
        String token = getAdminToken();
        keycloakClient.removeUserFromGroup(realm, userId, groupId, token);
    }

    @Override
    public List<Map<String, Object>> getUserGroups(String userId) {
        log.debug("Listando grupos do usuário: {}", userId);
        
        String token = getAdminToken();
        return keycloakClient.getUserGroups(realm, userId, token);
    }

    @Override
    public void logoutUser(String userId) {
        log.info("Fazendo logout do usuário: {}", userId);
        
        String token = getAdminToken();
        keycloakClient.logoutUser(realm, userId, token);
        
        log.info("Logout realizado com sucesso");
    }
    
    @Override
    public void updateUserAttribute(String userId, String attributeName, String attributeValue) {
        log.info("Atualizando atributo {} do usuário {}", attributeName, userId);
        
        String token = getAdminToken();
        
        Map<String, Object> user = keycloakClient.getUser(realm, userId, token);
        
        @SuppressWarnings("unchecked")
        Map<String, List<String>> attributes = (Map<String, List<String>>) user.get("attributes");
        
        if (attributes == null) {
            attributes = new HashMap<>();
        } else {
            attributes = new HashMap<>(attributes); // Cópia mutável
        }
        
        attributes.put(attributeName, List.of(attributeValue));
        
        Map<String, Object> updateData = Map.of("attributes", attributes);
        keycloakClient.updateUser(realm, userId, token, updateData);
        
        log.info("Atributo {} atualizado com sucesso", attributeName);
    }
    
    @Override
    public void assignClientRoles(String userId, List<String> roleNames) {
        log.info("Atribuindo roles {} ao usuário {}", roleNames, userId);
        
        String token = getAdminToken();
        
        List<Map<String, Object>> clients = keycloakClient.findClientByClientId(
            realm, keycloakClientId, token
        );
        
        if (clients.isEmpty()) {
            throw new RuntimeException("Client " + keycloakClientId + " não encontrado");
        }
        
        String clientUuid = (String) clients.get(0).get("id");
        
        List<Map<String, Object>> availableRoles = keycloakClient.getAvailableClientRoles(
            realm, userId, clientUuid, token
        );
        
        List<Map<String, Object>> rolesToAssign = availableRoles.stream()
            .filter(role -> roleNames.contains(role.get("name")))
            .toList();
        
        if (rolesToAssign.isEmpty()) {
            log.warn("Nenhuma das roles solicitadas está disponível: {}", roleNames);
            return;
        }
        
        keycloakClient.assignClientRolesToUser(
            realm, userId, clientUuid, token, rolesToAssign
        );
        
        log.info("Roles atribuídas com sucesso: {}", 
                rolesToAssign.stream().map(r -> r.get("name")).toList());
    }
    
    @Override
    public void sendPasswordSetupEmail(String userId) {
        log.info("Enviando email de configuração de senha para usuário: {}", userId);
        
        String token = getAdminToken();
        
        List<String> actions = List.of("UPDATE_PASSWORD");
        
        keycloakClient.executeActionsEmail(
            realm, userId, keycloakClientId, null, token, actions
        );
        
        log.info("Email de configuração de senha enviado com sucesso");
    }
}

