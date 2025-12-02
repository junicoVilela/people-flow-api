package com.peopleflow.accesscontrol.core.application;

import com.peopleflow.accesscontrol.core.ports.output.KeycloakGrupoPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class GrupoService {

    private final KeycloakGrupoPort keycloakPort;

    public String criar(String name) {
        log.info("Criando grupo: {}", name);
        
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome do grupo não pode ser vazio");
        }
        
        return keycloakPort.createGroup(name);
    }

    public List<Map<String, Object>> listarTodos() {
        log.debug("Listando todos os grupos");
        return keycloakPort.listAll();
    }

    public Map<String, Object> buscarPorId(String groupId) {
        log.debug("Buscando grupo por ID: {}", groupId);
        return keycloakPort.findById(groupId);
    }

    public List<Map<String, Object>> buscarPorNome(String name) {
        log.debug("Buscando grupos por nome: {}", name);
        return keycloakPort.findByName(name);
    }

    public void atualizar(String groupId, String newName) {
        log.info("Atualizando grupo: {}", groupId);
        keycloakPort.updateGroup(groupId, newName);
    }

    public void deletar(String groupId) {
        log.info("Deletando grupo: {}", groupId);
        keycloakPort.deleteGroup(groupId);
    }

    public List<Map<String, Object>> listarMembros(String groupId) {
        log.debug("Listando membros do grupo: {}", groupId);
        return keycloakPort.getMembers(groupId);
    }
}

