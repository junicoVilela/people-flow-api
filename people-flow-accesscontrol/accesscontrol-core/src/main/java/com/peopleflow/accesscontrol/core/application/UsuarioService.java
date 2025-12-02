package com.peopleflow.accesscontrol.core.application;

import com.peopleflow.accesscontrol.core.ports.output.KeycloakUsuarioPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class UsuarioService {

    private final KeycloakUsuarioPort keycloakPort;

    public String criar(String username, String email, String firstName, String lastName,
                       String password, Map<String, List<String>> attributes) {
        
        log.info("Criando usuário: {}", username);
        
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username não pode ser vazio");
        }
        
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email inválido");
        }
        
        String userId = keycloakPort.createUser(username, email, firstName, lastName, attributes);
        
        if (password != null && !password.isBlank()) {
            keycloakPort.setPassword(userId, password, false);
        }
        
        log.info("Usuário criado com sucesso: {} (ID: {})", username, userId);
        return userId;
    }

    public Map<String, Object> buscarPorUsername(String username) {
        log.debug("Buscando usuário: {}", username);
        return keycloakPort.findByUsername(username);
    }

    public Map<String, Object> buscarPorId(String userId) {
        log.debug("Buscando usuário por ID: {}", userId);
        return keycloakPort.findById(userId);
    }

    public List<Map<String, Object>> listarTodos() {
        log.debug("Listando todos os usuários");
        return keycloakPort.listAll();
    }

    public List<Map<String, Object>> buscarPorAtributo(String attributeName, String attributeValue) {
        log.debug("Buscando usuários por atributo: {}={}", attributeName, attributeValue);
        return keycloakPort.findByAttribute(attributeName, attributeValue);
    }

    public void atualizar(String userId, Map<String, Object> userData) {
        log.info("Atualizando usuário: {}", userId);
        keycloakPort.updateUser(userId, userData);
    }

    public void ativar(String userId) {
        log.info("Ativando usuário: {}", userId);
        keycloakPort.enableUser(userId);
    }

    public void desativar(String userId) {
        log.info("Desativando usuário: {}", userId);
        keycloakPort.disableUser(userId);
    }

    public void deletar(String userId) {
        log.info("Deletando usuário: {}", userId);
        keycloakPort.deleteUser(userId);
    }

    public void adicionarAoGrupo(String userId, String groupId) {
        log.info("Adicionando usuário {} ao grupo {}", userId, groupId);
        keycloakPort.addToGroup(userId, groupId);
    }

    public void removerDoGrupo(String userId, String groupId) {
        log.info("Removendo usuário {} do grupo {}", userId, groupId);
        keycloakPort.removeFromGroup(userId, groupId);
    }

    public void resetarSenha(String userId, String novaSenha, boolean temporaria) {
        log.info("Resetando senha do usuário: {}", userId);
        keycloakPort.setPassword(userId, novaSenha, temporaria);
    }

    public void forcarLogout(String userId) {
        log.info("Forçando logout do usuário: {}", userId);
        keycloakPort.logoutUser(userId);
    }
}

