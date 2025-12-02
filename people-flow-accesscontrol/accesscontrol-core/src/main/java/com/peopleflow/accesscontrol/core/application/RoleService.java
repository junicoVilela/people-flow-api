package com.peopleflow.accesscontrol.core.application;

import com.peopleflow.accesscontrol.core.ports.output.KeycloakRolePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class RoleService {

    private final KeycloakRolePort keycloakPort;

    public void criar(String roleName, String description) {
        log.info("Criando role: {}", roleName);
        
        if (roleName == null || roleName.isBlank()) {
            throw new IllegalArgumentException("Nome da role não pode ser vazio");
        }
        
        if (!roleName.contains(":")) {
            log.warn("Role {} não segue padrão resource:action", roleName);
        }
        
        keycloakPort.createClientRole(roleName, description);
        log.info("Role criada com sucesso: {}", roleName);
    }

    public List<Map<String, Object>> listarTodas() {
        log.debug("Listando todas as roles do client");
        return keycloakPort.listClientRoles();
    }

    public Map<String, Object> buscarPorNome(String roleName) {
        log.debug("Buscando role: {}", roleName);
        return keycloakPort.findByName(roleName);
    }

    public void deletar(String roleName) {
        log.info("Deletando role: {}", roleName);
        keycloakPort.deleteRole(roleName);
    }
}