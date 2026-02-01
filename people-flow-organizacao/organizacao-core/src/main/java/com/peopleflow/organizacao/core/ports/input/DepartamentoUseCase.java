package com.peopleflow.organizacao.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Departamento;
import com.peopleflow.organizacao.core.query.DepartamentoFilter;

public interface DepartamentoUseCase {

    Departamento criar(Departamento departamento);
    Departamento atualizar(Long id, Departamento departamento);
    Departamento buscarPorId(Long id);
    PagedResult<Departamento> buscarPorFiltros(DepartamentoFilter filter, Pagination pagination);
    Departamento excluir(Long id);
    Departamento ativar(Long id);
    Departamento inativar(Long id);
}
