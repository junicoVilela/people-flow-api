package com.peopleflow.organizacao.outbound.jpa.adapter;

import com.peopleflow.common.query.PagedResult;
import com.peopleflow.common.query.Pagination;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.ports.output.EmpresaRepositoryPort;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import com.peopleflow.organizacao.outbound.jpa.repository.EmpresaJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmpresaRepositoryAdapter implements EmpresaRepositoryPort {

    private final EmpresaJpaRepository empresaJpaRepository;

    public EmpresaRepositoryAdapter(EmpresaJpaRepository empresaJpaRepository) {
        this.empresaJpaRepository = empresaJpaRepository;
    }

    @Override
    public Empresa salvar(Empresa colaborador) {
        return null;
    }

    @Override
    public Optional<Empresa> buscarPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public PagedResult<Empresa> buscarPorFiltros(EmpresaFilter filter, Pagination pagination) {
        return null;
    }

    @Override
    public void deletar(Long id) {

    }
}
