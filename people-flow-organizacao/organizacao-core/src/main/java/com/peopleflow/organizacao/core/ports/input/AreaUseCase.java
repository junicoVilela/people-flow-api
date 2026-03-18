package com.peopleflow.organizacao.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Area;
import com.peopleflow.organizacao.core.query.AreaFilter;

public interface AreaUseCase {

    Area criar(Area area);
    Area atualizar(Long id, Area area);
    Area buscarPorId(Long id);
    PagedResult<Area> buscarPorFiltros(AreaFilter filter, Pagination pagination);
    Area ativar(Long id);
    Area inativar(Long id);
    Area excluir(Long id);
}
