package com.peopleflow.accesscontrol.core.application;

import com.peopleflow.accesscontrol.core.ports.output.CargoRoleMappingPort;
import com.peopleflow.accesscontrol.core.ports.output.DepartamentoGrupoMappingPort;
import com.peopleflow.accesscontrol.core.ports.output.KeycloakUsuarioPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Serviço para auto-atribuição de roles e grupos baseado em cargo e departamento
 */
@Slf4j
@RequiredArgsConstructor
public class AutoAtribuicaoService {

    private final KeycloakUsuarioPort usuarioPort;
    private final CargoRoleMappingPort cargoMappingPort;
    private final DepartamentoGrupoMappingPort departamentoMappingPort;

    /**
     * Atribui automaticamente roles ao usuário baseado no cargo
     * 
     * @param userId ID do usuário no Keycloak
     * @param cargoId ID do cargo
     */
    public void atribuirRolesPorCargo(String userId, Long cargoId) {
        log.info("🎯 Iniciando auto-atribuição de roles para userId={}, cargoId={}", userId, cargoId);
        
        try {
            List<String> roles = cargoMappingPort.buscarRolesPorCargo(cargoId);
            
            if (roles.isEmpty()) {
                log.warn("⚠️ Nenhuma role mapeada para o cargoId={}", cargoId);
                return;
            }

            log.info("📋 Roles encontradas para cargoId={}: {}", cargoId, roles);
            usuarioPort.assignClientRoles(userId, roles);
            
            log.info("✅ Roles atribuídas com sucesso: {} → userId={}", roles, userId);
            
        } catch (Exception e) {
            log.error("❌ Erro ao atribuir roles por cargo: userId={}, cargoId={}", 
                    userId, cargoId, e);
            throw new RuntimeException("Falha na auto-atribuição de roles", e);
        }
    }

    /**
     * Adiciona automaticamente o usuário ao grupo baseado no departamento
     * 
     * @param userId ID do usuário no Keycloak
     * @param departamentoId ID do departamento
     */
    public void atribuirGrupoPorDepartamento(String userId, Long departamentoId) {
        log.info("🎯 Iniciando auto-atribuição de grupo para userId={}, departamentoId={}", 
                userId, departamentoId);
        
        try {
            Optional<String> groupIdOpt = departamentoMappingPort.buscarGrupoPorDepartamento(departamentoId);
            
            if (groupIdOpt.isEmpty()) {
                log.warn("⚠️ Nenhum grupo mapeado para o departamentoId={}", departamentoId);
                return;
            }

            String groupId = groupIdOpt.get();
            log.info("📋 Grupo encontrado para departamentoId={}: {}", departamentoId, groupId);
            
            usuarioPort.addToGroup(userId, groupId);
            
            log.info("✅ Usuário adicionado ao grupo com sucesso: userId={} → groupId={}", 
                    userId, groupId);
            
        } catch (Exception e) {
            log.error("❌ Erro ao atribuir grupo por departamento: userId={}, departamentoId={}", 
                    userId, departamentoId, e);
            throw new RuntimeException("Falha na auto-atribuição de grupo", e);
        }
    }

    /**
     * Atribui automaticamente roles e grupo de uma só vez
     * 
     * @param userId ID do usuário no Keycloak
     * @param cargoId ID do cargo (opcional)
     * @param departamentoId ID do departamento (opcional)
     */
    public void atribuirPermissoesCompletas(String userId, Long cargoId, Long departamentoId) {
        log.info("🎯 Iniciando auto-atribuição completa: userId={}, cargoId={}, departamentoId={}", 
                userId, cargoId, departamentoId);
        
        if (cargoId != null) {
            atribuirRolesPorCargo(userId, cargoId);
        }
        
        if (departamentoId != null) {
            atribuirGrupoPorDepartamento(userId, departamentoId);
        }
        
        log.info("✅ Auto-atribuição completa finalizada para userId={}", userId);
    }
}

