package com.peopleflow.organizacao.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.query.UnidadeFilter;

import java.util.Optional;

public interface UnidadeRepositoryPort {

    Unidade salvar(Unidade unidade);
    Optional<Unidade> buscarPorId(Long id);
    PagedResult<Unidade> buscarPorFiltros(UnidadeFilter filter, Pagination pagination);

    boolean existePorCodigo(String codigo);
    boolean existePorCodigoExcluindoId(String codigo, Long id);
}
