package com.peopleflow.accesscontrol.outbound.database.adapter;

import com.peopleflow.accesscontrol.core.ports.output.DepartamentoGrupoMappingPort;
import com.peopleflow.accesscontrol.outbound.database.repository.DepartamentoGrupoJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter para mapeamento Departamento → Grupo Keycloak
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DepartamentoGrupoMappingAdapter implements DepartamentoGrupoMappingPort {

    private final DepartamentoGrupoJdbcRepository repository;

    @Override
    public Optional<String> buscarGrupoPorDepartamento(Long departamentoId) {
        log.debug("Buscando grupo para departamentoId={}", departamentoId);
        return repository.findGroupIdByDepartamentoId(departamentoId);
    }

    @Override
    public void salvarMapeamento(Long departamentoId, String keycloakGroupId, String keycloakGroupName) {
        log.info("Salvando mapeamento: departamentoId={}, groupId={}, groupName={}", 
                departamentoId, keycloakGroupId, keycloakGroupName);
        repository.upsert(departamentoId, keycloakGroupId, keycloakGroupName, "SYSTEM");
    }

    @Override
    public void removerMapeamento(Long departamentoId) {
        log.info("Removendo mapeamento do departamentoId={}", departamentoId);
        repository.delete(departamentoId);
    }
}

