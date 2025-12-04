package com.peopleflow.accesscontrol.core.ports.output;

import java.util.List;

/**
 * Port para operações de mapeamento Cargo → Roles
 */
public interface CargoRoleMappingPort {

    /**
     * Busca todas as roles associadas a um cargo
     * 
     * @param cargoId ID do cargo
     * @return Lista de nomes de roles
     */
    List<String> buscarRolesPorCargo(Long cargoId);

    /**
     * Adiciona um mapeamento cargo → role
     * 
     * @param cargoId ID do cargo
     * @param roleName Nome da role
     * @param descricao Descrição do mapeamento
     */
    void adicionarMapeamento(Long cargoId, String roleName, String descricao);

    /**
     * Remove um mapeamento cargo → role
     * 
     * @param cargoId ID do cargo
     * @param roleName Nome da role
     */
    void removerMapeamento(Long cargoId, String roleName);

    /**
     * Remove todos os mapeamentos de um cargo
     * 
     * @param cargoId ID do cargo
     */
    void removerTodosMapeamentos(Long cargoId);
}

