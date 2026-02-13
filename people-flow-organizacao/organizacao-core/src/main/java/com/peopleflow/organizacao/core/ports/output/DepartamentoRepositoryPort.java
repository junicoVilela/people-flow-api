package com.peopleflow.organizacao.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Departamento;
import com.peopleflow.organizacao.core.query.DepartamentoFilter;

import java.util.Optional;

public interface DepartamentoRepositoryPort {

    Departamento salvar(Departamento departamento);
    Optional<Departamento> buscarPorId(Long id);
    PagedResult<Departamento> buscarPorFiltros(DepartamentoFilter filter, Pagination pagination);

    boolean existePorCodigo(String codigo);
    boolean existePorCodigoExcluindoId(String codigo, Long id);
}
