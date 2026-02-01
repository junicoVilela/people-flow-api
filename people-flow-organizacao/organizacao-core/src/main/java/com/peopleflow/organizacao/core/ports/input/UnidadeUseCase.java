package com.peopleflow.organizacao.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.query.UnidadeFilter;

public interface UnidadeUseCase {

    Unidade criar(Unidade unidade);
    Unidade atualizar(Long id, Unidade unidade);
    Unidade buscarPorId(Long id);
    PagedResult<Unidade> buscarPorFiltros(UnidadeFilter filter, Pagination pagination);
    Unidade excluir(Long id);
    Unidade ativar(Long id);
    Unidade inativar(Long id);
}
