package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.FaixaSalarial;
import com.peopleflow.pessoascontratos.core.query.FaixaSalarialFilter;

import java.util.Optional;

public interface FaixaSalarialRepositoryPort {

    FaixaSalarial salvar(FaixaSalarial faixa);

    Optional<FaixaSalarial> buscarAtivoPorId(Long id);

    PagedResult<FaixaSalarial> buscarPorFiltros(Long cargoId, FaixaSalarialFilter filtros, Pagination pagination);

    void excluir(Long id);
}
