package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Contrato;

import java.util.Optional;

public interface ContratoRepositoryPort {

    Contrato salvar(Contrato contrato);

    Optional<Contrato> buscarAtivoPorId(Long id);

    PagedResult<Contrato> listarPorColaboradorId(Long colaboradorId, Pagination pagination);

    void excluir(Long id);
}
