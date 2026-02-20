package com.peopleflow.accesscontrol.outbound.keycloak.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Feign Client para Keycloak Admin REST API
 * 
 * Documentação oficial: https://www.keycloak.org/docs-api/latest/rest-api/
 */
@FeignClient(
    name = "keycloak-admin",
    url = "${keycloak.auth-server-url}",
    configuration = KeycloakFeignConfiguration.class
)
public interface KeycloakAdminFeignClient {

    /**
     * Obtém token de admin no realm master. Body deve ser application/x-www-form-urlencoded
     * (client_id, username, password, grant_type=password).
     */
    @PostMapping(
        value = "/realms/master/protocol/openid-connect/token",
        consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    Map<String, Object> getAdminToken(@RequestBody Map<String, ?> formParams);

    @PostMapping("/admin/realms/{realm}/users")
    void createUser(
        @PathVariable("realm") String realm,
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody Map<String, Object> user
    );

    @GetMapping("/admin/realms/{realm}/users")
    List<Map<String, Object>> listUsers(
        @PathVariable("realm") String realm,
        @RequestHeader("Authorization") String bearerToken
    );

    @GetMapping("/admin/realms/{realm}/users")
    List<Map<String, Object>> searchUsers(
        @PathVariable("realm") String realm,
        @RequestParam("username") String username,
        @RequestParam(value = "exact", defaultValue = "false") boolean exact,
        @RequestHeader("Authorization") String bearerToken
    );

    @GetMapping("/admin/realms/{realm}/users/{userId}")
    Map<String, Object> getUser(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @RequestHeader("Authorization") String bearerToken
    );

    @PutMapping("/admin/realms/{realm}/users/{userId}")
    void updateUser(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody Map<String, Object> user
    );

    @DeleteMapping("/admin/realms/{realm}/users/{userId}")
    void deleteUser(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @RequestHeader("Authorization") String bearerToken
    );

    @PutMapping("/admin/realms/{realm}/users/{userId}/reset-password")
    void resetPassword(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody Map<String, Object> credential
    );

    @PutMapping("/admin/realms/{realm}/users/{userId}/groups/{groupId}")
    void addUserToGroup(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @PathVariable("groupId") String groupId,
        @RequestHeader("Authorization") String bearerToken
    );

    @DeleteMapping("/admin/realms/{realm}/users/{userId}/groups/{groupId}")
    void removeUserFromGroup(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @PathVariable("groupId") String groupId,
        @RequestHeader("Authorization") String bearerToken
    );

    @GetMapping("/admin/realms/{realm}/users/{userId}/groups")
    List<Map<String, Object>> getUserGroups(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @RequestHeader("Authorization") String bearerToken
    );

    @PostMapping("/admin/realms/{realm}/users/{userId}/logout")
    void logoutUser(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @RequestHeader("Authorization") String bearerToken
    );

    @PostMapping("/admin/realms/{realm}/groups")
    void createGroup(
        @PathVariable("realm") String realm,
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody Map<String, Object> group
    );

    @GetMapping("/admin/realms/{realm}/groups")
    List<Map<String, Object>> listGroups(
        @PathVariable("realm") String realm,
        @RequestHeader("Authorization") String bearerToken
    );

    @GetMapping("/admin/realms/{realm}/groups/{groupId}")
    Map<String, Object> getGroup(
        @PathVariable("realm") String realm,
        @PathVariable("groupId") String groupId,
        @RequestHeader("Authorization") String bearerToken
    );

    @PutMapping("/admin/realms/{realm}/groups/{groupId}")
    void updateGroup(
        @PathVariable("realm") String realm,
        @PathVariable("groupId") String groupId,
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody Map<String, Object> group
    );

    @DeleteMapping("/admin/realms/{realm}/groups/{groupId}")
    void deleteGroup(
        @PathVariable("realm") String realm,
        @PathVariable("groupId") String groupId,
        @RequestHeader("Authorization") String bearerToken
    );

    @GetMapping("/admin/realms/{realm}/groups/{groupId}/members")
    List<Map<String, Object>> getGroupMembers(
        @PathVariable("realm") String realm,
        @PathVariable("groupId") String groupId,
        @RequestHeader("Authorization") String bearerToken
    );

    @PostMapping("/admin/realms/{realm}/clients/{clientUuid}/roles")
    void createClientRole(
        @PathVariable("realm") String realm,
        @PathVariable("clientUuid") String clientUuid,
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody Map<String, Object> role
    );

    @GetMapping("/admin/realms/{realm}/clients/{clientUuid}/roles")
    List<Map<String, Object>> listClientRoles(
        @PathVariable("realm") String realm,
        @PathVariable("clientUuid") String clientUuid,
        @RequestHeader("Authorization") String bearerToken
    );

    @DeleteMapping("/admin/realms/{realm}/clients/{clientUuid}/roles/{roleName}")
    void deleteClientRole(
        @PathVariable("realm") String realm,
        @PathVariable("clientUuid") String clientUuid,
        @PathVariable("roleName") String roleName,
        @RequestHeader("Authorization") String bearerToken
    );

    @GetMapping("/admin/realms/{realm}/clients")
    List<Map<String, Object>> findClientByClientId(
        @PathVariable("realm") String realm,
        @RequestParam("clientId") String clientId,
        @RequestHeader("Authorization") String bearerToken
    );

    @PostMapping("/admin/realms/{realm}/users/{userId}/role-mappings/clients/{clientUuid}")
    void assignClientRolesToUser(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @PathVariable("clientUuid") String clientUuid,
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody List<Map<String, Object>> roles
    );

    @GetMapping("/admin/realms/{realm}/users/{userId}/role-mappings/clients/{clientUuid}/available")
    List<Map<String, Object>> getAvailableClientRoles(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @PathVariable("clientUuid") String clientUuid,
        @RequestHeader("Authorization") String bearerToken
    );

    @PutMapping("/admin/realms/{realm}/users/{userId}/execute-actions-email")
    void executeActionsEmail(
        @PathVariable("realm") String realm,
        @PathVariable("userId") String userId,
        @RequestParam(required = false) String clientId,
        @RequestParam(required = false) String redirectUri,
        @RequestHeader("Authorization") String bearerToken,
        @RequestBody List<String> actions
    );
}

