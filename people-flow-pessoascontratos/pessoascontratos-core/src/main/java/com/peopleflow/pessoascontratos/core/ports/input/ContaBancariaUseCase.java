package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.ContaBancaria;
import com.peopleflow.pessoascontratos.core.query.ContaBancariaFilter;

public interface ContaBancariaUseCase {

    ContaBancaria adicionar(Long colaboradorId, ContaBancaria dados);

    ContaBancaria atualizar(Long colaboradorId, Long contaId, ContaBancaria dados);

    ContaBancaria buscarPorId(Long colaboradorId, Long contaId);

    PagedResult<ContaBancaria> buscarPorFiltros(Long colaboradorId, ContaBancariaFilter filtros, Pagination pagination);

    void excluir(Long colaboradorId, Long contaId);
}
