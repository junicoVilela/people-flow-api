package com.peopleflow.organizacao.core.ports.output;

import com.peopleflow.common.query.PagedResult;
import com.peopleflow.common.query.Pagination;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.query.EmpresaFilter;

import java.util.Optional;

public interface EmpresaRepositoryPort {

    Empresa salvar(Empresa colaborador);
    Optional<Empresa> buscarPorId(Long id);
    PagedResult<Empresa> buscarPorFiltros(EmpresaFilter filter, Pagination pagination);
    void deletar(Long id);
}
