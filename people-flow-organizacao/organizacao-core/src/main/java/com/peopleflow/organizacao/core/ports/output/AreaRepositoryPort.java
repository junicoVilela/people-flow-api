package com.peopleflow.organizacao.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Area;
import com.peopleflow.organizacao.core.query.AreaFilter;

import java.util.Optional;

public interface AreaRepositoryPort {

    Area salvar(Area area);
    Optional<Area> buscarPorId(Long id);
    PagedResult<Area> buscarPorFiltros(AreaFilter filter, Pagination pagination);

    boolean existePorCodigo(String codigo);
    boolean existePorCodigoExcluindoId(String codigo, Long id);
}
