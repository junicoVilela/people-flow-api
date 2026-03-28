package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.ContaBancaria;

import java.util.Optional;

public interface ContaBancariaRepositoryPort {

    ContaBancaria salvar(ContaBancaria conta);

    Optional<ContaBancaria> buscarAtivoPorId(Long id);

    PagedResult<ContaBancaria> listarPorColaboradorId(Long colaboradorId, Pagination pagination);

    void excluir(Long id);
}
