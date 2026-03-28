package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.FaixaSalarial;

public interface FaixaSalarialUseCase {

    FaixaSalarial adicionar(Long cargoId, FaixaSalarial dados);

    FaixaSalarial atualizar(Long cargoId, Long faixaId, FaixaSalarial dados);

    FaixaSalarial buscarPorId(Long cargoId, Long faixaId);

    PagedResult<FaixaSalarial> listarPorCargo(Long cargoId, Pagination pagination);

    void excluir(Long cargoId, Long faixaId);
}
