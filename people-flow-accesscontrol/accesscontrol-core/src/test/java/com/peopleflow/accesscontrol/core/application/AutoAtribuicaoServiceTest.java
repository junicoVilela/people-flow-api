package com.peopleflow.accesscontrol.core.application;

import com.peopleflow.accesscontrol.core.ports.output.CargoRoleMappingPort;
import com.peopleflow.accesscontrol.core.ports.output.DepartamentoGrupoMappingPort;
import com.peopleflow.accesscontrol.core.ports.output.KeycloakUsuarioPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AutoAtribuicaoService - Testes Unitários")
class AutoAtribuicaoServiceTest {

    @Mock
    private KeycloakUsuarioPort usuarioPort;

    @Mock
    private CargoRoleMappingPort cargoMappingPort;

    @Mock
    private DepartamentoGrupoMappingPort departamentoMappingPort;

    private AutoAtribuicaoService service;

    @BeforeEach
    void setUp() {
        service = new AutoAtribuicaoService(usuarioPort, cargoMappingPort, departamentoMappingPort);
    }

    @Test
    @DisplayName("Deve atribuir roles automaticamente por cargo")
    void deveAtribuirRolesPorCargo() {
        // Arrange
        String userId = "user-123";
        Long cargoId = 1L;
        List<String> roles = List.of("colaborador:criar", "colaborador:ler");
        
        when(cargoMappingPort.buscarRolesPorCargo(cargoId)).thenReturn(roles);

        // Act
        service.atribuirRolesPorCargo(userId, cargoId);

        // Assert
        verify(cargoMappingPort).buscarRolesPorCargo(cargoId);
        verify(usuarioPort).assignClientRoles(userId, roles);
    }

    @Test
    @DisplayName("Não deve atribuir roles se cargo não tiver mapeamento")
    void naoDeveAtribuirRolesSeCadgoSemMapeamento() {
        // Arrange
        String userId = "user-123";
        Long cargoId = 99L;
        
        when(cargoMappingPort.buscarRolesPorCargo(cargoId)).thenReturn(List.of());

        // Act
        service.atribuirRolesPorCargo(userId, cargoId);

        // Assert
        verify(cargoMappingPort).buscarRolesPorCargo(cargoId);
        verify(usuarioPort, never()).assignClientRoles(any(), any());
    }

    @Test
    @DisplayName("Deve atribuir grupo automaticamente por departamento")
    void deveAtribuirGrupoPorDepartamento() {
        // Arrange
        String userId = "user-123";
        Long departamentoId = 5L;
        String groupId = "group-uuid-456";
        
        when(departamentoMappingPort.buscarGrupoPorDepartamento(departamentoId))
                .thenReturn(Optional.of(groupId));

        // Act
        service.atribuirGrupoPorDepartamento(userId, departamentoId);

        // Assert
        verify(departamentoMappingPort).buscarGrupoPorDepartamento(departamentoId);
        verify(usuarioPort).addToGroup(userId, groupId);
    }

    @Test
    @DisplayName("Não deve atribuir grupo se departamento não tiver mapeamento")
    void naoDeveAtribuirGrupoSeDepartamentoSemMapeamento() {
        // Arrange
        String userId = "user-123";
        Long departamentoId = 99L;
        
        when(departamentoMappingPort.buscarGrupoPorDepartamento(departamentoId))
                .thenReturn(Optional.empty());

        // Act
        service.atribuirGrupoPorDepartamento(userId, departamentoId);

        // Assert
        verify(departamentoMappingPort).buscarGrupoPorDepartamento(departamentoId);
        verify(usuarioPort, never()).addToGroup(any(), any());
    }

    @Test
    @DisplayName("Deve atribuir permissões completas (roles + grupo)")
    void deveAtribuirPermissoesCompletas() {
        // Arrange
        String userId = "user-123";
        Long cargoId = 1L;
        Long departamentoId = 5L;
        List<String> roles = List.of("colaborador:ler");
        String groupId = "group-456";
        
        when(cargoMappingPort.buscarRolesPorCargo(cargoId)).thenReturn(roles);
        when(departamentoMappingPort.buscarGrupoPorDepartamento(departamentoId))
                .thenReturn(Optional.of(groupId));

        // Act
        service.atribuirPermissoesCompletas(userId, cargoId, departamentoId);

        // Assert
        verify(usuarioPort).assignClientRoles(userId, roles);
        verify(usuarioPort).addToGroup(userId, groupId);
    }

    @Test
    @DisplayName("Deve propagar exceção ao falhar atribuição de roles")
    void devePropagaExcecaoAoFalharAtribuicaoRoles() {
        // Arrange
        String userId = "user-123";
        Long cargoId = 1L;
        List<String> roles = List.of("colaborador:criar");
        
        when(cargoMappingPort.buscarRolesPorCargo(cargoId)).thenReturn(roles);
        doThrow(new RuntimeException("Keycloak error"))
                .when(usuarioPort).assignClientRoles(userId, roles);

        // Act & Assert
        assertThatThrownBy(() -> service.atribuirRolesPorCargo(userId, cargoId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Falha na auto-atribuição de roles");
    }
}

