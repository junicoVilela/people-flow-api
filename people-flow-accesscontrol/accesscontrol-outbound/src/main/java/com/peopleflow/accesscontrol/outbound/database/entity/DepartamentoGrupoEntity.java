package com.peopleflow.accesscontrol.outbound.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidade JPA para mapeamento Departamento → Grupo Keycloak
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoGrupoEntity {

    private Long departamentoId;
    private String keycloakGroupId;
    private String keycloakGroupName;
    private LocalDateTime criadoEm;
    private String criadoPor;
    private LocalDateTime atualizadoEm;
    private String atualizadoPor;
}

