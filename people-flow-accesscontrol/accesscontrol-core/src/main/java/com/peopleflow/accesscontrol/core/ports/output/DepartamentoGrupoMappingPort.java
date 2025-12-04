package com.peopleflow.accesscontrol.core.ports.output;

import java.util.Optional;

/**
 * Port para operações de mapeamento Departamento → Grupo Keycloak
 */
public interface DepartamentoGrupoMappingPort {

    /**
     * Busca o grupo Keycloak associado a um departamento
     * 
     * @param departamentoId ID do departamento
     * @return ID do grupo no Keycloak (UUID) ou Optional.empty()
     */
    Optional<String> buscarGrupoPorDepartamento(Long departamentoId);

    /**
     * Cria ou atualiza o mapeamento departamento → grupo
     * 
     * @param departamentoId ID do departamento
     * @param keycloakGroupId UUID do grupo no Keycloak
     * @param keycloakGroupName Nome do grupo no Keycloak
     */
    void salvarMapeamento(Long departamentoId, String keycloakGroupId, String keycloakGroupName);

    /**
     * Remove o mapeamento de um departamento
     * 
     * @param departamentoId ID do departamento
     */
    void removerMapeamento(Long departamentoId);
}

