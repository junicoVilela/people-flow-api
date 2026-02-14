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

    boolean existePorCodigoEEmpresa(String codigo, Long empresaId);
    boolean existePorCodigoEEmpresaExcluindoId(String codigo, Long empresaId, Long id);

    void excluirTodosPorEmpresaId(Long empresaId);
}
