package com.peopleflow.accesscontrol.core.application;

import com.peopleflow.accesscontrol.core.ports.output.KeycloakUsuarioPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioService - Testes Unitários")
class UsuarioServiceTest {

    @Mock
    private KeycloakUsuarioPort keycloakPort;

    private UsuarioService service;

    @BeforeEach
    void setUp() {
        service = new UsuarioService(keycloakPort);
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // Arrange
        String expectedUserId = "user-uuid-123";
        when(keycloakPort.createUser(any(), any(), any(), any(), any()))
                .thenReturn(expectedUserId);

        // Act
        String userId = service.criar(
                "maria.silva",
                "maria@example.com",
                "Maria",
                "Silva",
                null, // sem senha
                Map.of("colaboradorId", List.of("100"))
        );

        // Assert
        assertThat(userId).isEqualTo(expectedUserId);
        verify(keycloakPort).createUser(
                eq("maria.silva"),
                eq("maria@example.com"),
                eq("Maria"),
                eq("Silva"),
                eq(Map.of("colaboradorId", List.of("100")))
        );
    }

    @Test
    @DisplayName("Deve buscar usuário por username")
    void deveBuscarUsuarioPorUsername() {
        // Arrange
        Map<String, Object> expectedUser = new HashMap<>();
        expectedUser.put("id", "user-123");
        expectedUser.put("username", "maria");
        when(keycloakPort.findByUsername("maria")).thenReturn(expectedUser);

        // Act
        Map<String, Object> result = service.buscarPorUsername("maria");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.get("username")).isEqualTo("maria");
        verify(keycloakPort).findByUsername("maria");
    }

    @Test
    @DisplayName("Deve atualizar atributo do usuário")
    void deveAtualizarAtributoUsuario() {
        // Arrange
        String userId = "user-123";
        String attributeName = "colaboradorId";
        String attributeValue = "200";

        // Act
        service.atualizarAtributo(userId, attributeName, attributeValue);

        // Assert
        verify(keycloakPort).updateUserAttribute(userId, attributeName, attributeValue);
    }

    @Test
    @DisplayName("Deve atribuir roles ao usuário")
    void deveAtribuirRoles() {
        // Arrange
        String userId = "user-123";
        List<String> roles = List.of("colaborador:criar", "colaborador:ler");

        // Act
        service.atribuirRoles(userId, roles);

        // Assert
        verify(keycloakPort).assignClientRoles(userId, roles);
    }

    @Test
    @DisplayName("Deve enviar email para definir senha")
    void deveEnviarEmailDefinirSenha() {
        // Arrange
        String userId = "user-123";

        // Act
        service.enviarEmailDefinirSenha(userId);

        // Assert
        verify(keycloakPort).sendPasswordSetupEmail(userId);
    }

    @Test
    @DisplayName("Deve ativar usuário")
    void deveAtivarUsuario() {
        // Arrange
        String userId = "user-123";

        // Act
        service.ativar(userId);

        // Assert
        verify(keycloakPort).enableUser(userId);
    }

    @Test
    @DisplayName("Deve desativar usuário")
    void deveDesativarUsuario() {
        // Arrange
        String userId = "user-123";

        // Act
        service.desativar(userId);

        // Assert
        verify(keycloakPort).disableUser(userId);
    }

    @Test
    @DisplayName("Deve adicionar usuário a grupo")
    void deveAdicionarUsuarioAoGrupo() {
        // Arrange
        String userId = "user-123";
        String groupId = "group-456";

        // Act
        service.adicionarAoGrupo(userId, groupId);

        // Assert
        verify(keycloakPort).addToGroup(userId, groupId);
    }

    @Test
    @DisplayName("Deve buscar usuários por atributo")
    void deveBuscarUsuariosPorAtributo() {
        // Arrange
        List<Map<String, Object>> expectedUsers = List.of(
                Map.of("id", "user-1", "username", "user1"),
                Map.of("id", "user-2", "username", "user2")
        );
        when(keycloakPort.findByAttribute("colaboradorId", "100"))
                .thenReturn(expectedUsers);

        // Act
        List<Map<String, Object>> result = service.buscarPorAtributo("colaboradorId", "100");

        // Assert
        assertThat(result).hasSize(2);
        verify(keycloakPort).findByAttribute("colaboradorId", "100");
    }
}

