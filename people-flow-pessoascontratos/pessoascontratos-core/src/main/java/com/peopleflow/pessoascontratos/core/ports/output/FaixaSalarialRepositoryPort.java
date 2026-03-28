package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.FaixaSalarial;

import java.util.Optional;

public interface FaixaSalarialRepositoryPort {

    FaixaSalarial salvar(FaixaSalarial faixa);

    Optional<FaixaSalarial> buscarAtivoPorId(Long id);

    PagedResult<FaixaSalarial> listarPorCargoId(Long cargoId, Pagination pagination);

    void excluir(Long id);
}
