package com.peopleflow.organizacao.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.CentroCusto;
import com.peopleflow.organizacao.core.query.CentroCustoFilter;

public interface CentroCustoUseCase {

    CentroCusto criar(CentroCusto centroCusto);
    CentroCusto atualizar(Long id, CentroCusto centroCusto);
    CentroCusto buscarPorId(Long id);
    PagedResult<CentroCusto> buscarPorFiltros(CentroCustoFilter filter, Pagination pagination);
    CentroCusto excluir(Long id);
    CentroCusto ativar(Long id);
    CentroCusto inativar(Long id);
}
