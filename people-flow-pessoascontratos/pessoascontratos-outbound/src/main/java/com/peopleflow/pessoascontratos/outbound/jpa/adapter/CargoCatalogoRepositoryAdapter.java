package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.pessoascontratos.core.ports.output.CargoCatalogoRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.CargoJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CargoCatalogoRepositoryAdapter implements CargoCatalogoRepositoryPort {

    private final CargoJpaRepository repository;

    public CargoCatalogoRepositoryAdapter(CargoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existeAtivoPorId(Long cargoId) {
        return repository.existsByIdAndExcluidoEmIsNull(cargoId);
    }
}
