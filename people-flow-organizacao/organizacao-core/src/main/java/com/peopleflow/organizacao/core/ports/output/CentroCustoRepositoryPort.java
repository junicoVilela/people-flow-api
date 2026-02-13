package com.peopleflow.organizacao.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.CentroCusto;
import com.peopleflow.organizacao.core.query.CentroCustoFilter;

import java.util.Optional;

public interface CentroCustoRepositoryPort {

    CentroCusto salvar(CentroCusto centroCusto);
    Optional<CentroCusto> buscarPorId(Long id);
    PagedResult<CentroCusto> buscarPorFiltros(CentroCustoFilter filter, Pagination pagination);

    boolean existePorCodigo(String codigo);
    boolean existePorCodigoExcluindoId(String codigo, Long id);
}
