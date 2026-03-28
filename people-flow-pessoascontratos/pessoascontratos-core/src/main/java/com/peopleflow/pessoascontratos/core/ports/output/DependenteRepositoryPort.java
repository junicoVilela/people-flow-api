package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Dependente;
import com.peopleflow.pessoascontratos.core.query.DependenteFilter;

import java.util.Optional;

public interface DependenteRepositoryPort {

    Dependente salvar(Dependente dependente);

    Optional<Dependente> buscarAtivoPorId(Long id);

    PagedResult<Dependente> buscarPorFiltros(Long colaboradorId, DependenteFilter filtros, Pagination pagination);

    void excluir(Long id);
}
