package com.peopleflow.accesscontrol.outbound.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade JPA para mapeamento Cargo → Role
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CargoRoleEntity {

    private Long cargoId;
    private String roleName;
    private String descricao;
    private LocalDateTime criadoEm;
    private String criadoPor;
}

