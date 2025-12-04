package com.peopleflow.accesscontrol.outbound.database.adapter;

import com.peopleflow.accesscontrol.core.ports.output.CargoRoleMappingPort;
import com.peopleflow.accesscontrol.outbound.database.repository.CargoRoleJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adapter para mapeamento Cargo → Role
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CargoRoleMappingAdapter implements CargoRoleMappingPort {

    private final CargoRoleJdbcRepository repository;

    @Override
    @Cacheable(value = "cargo-roles", key = "#cargoId", unless = "#result.isEmpty()")
    public List<String> buscarRolesPorCargo(Long cargoId) {
        log.debug("Buscando roles para cargoId={} (cache miss)", cargoId);
        return repository.findRolesByCargoId(cargoId);
    }

    @Override
    @CacheEvict(value = "cargo-roles", key = "#cargoId")
    public void adicionarMapeamento(Long cargoId, String roleName, String descricao) {
        log.info("Adicionando mapeamento: cargoId={}, role={}", cargoId, roleName);
        repository.insert(cargoId, roleName, descricao, "SYSTEM");
    }

    @Override
    @CacheEvict(value = "cargo-roles", key = "#cargoId")
    public void removerMapeamento(Long cargoId, String roleName) {
        log.info("Removendo mapeamento: cargoId={}, role={}", cargoId, roleName);
        repository.delete(cargoId, roleName);
    }

    @Override
    @CacheEvict(value = "cargo-roles", key = "#cargoId")
    public void removerTodosMapeamentos(Long cargoId) {
        log.info("Removendo todos os mapeamentos do cargoId={}", cargoId);
        repository.deleteAllByCargoId(cargoId);
    }
}

