package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Contrato;
import com.peopleflow.pessoascontratos.core.query.ContratoFilter;

public interface ContratoUseCase {

    Contrato criar(Long colaboradorId, Contrato dados);

    Contrato atualizar(Long colaboradorId, Long contratoId, Contrato dados);

    Contrato buscarPorId(Long colaboradorId, Long contratoId);

    PagedResult<Contrato> buscarPorFiltros(Long colaboradorId, ContratoFilter filtros, Pagination pagination);

    void excluir(Long colaboradorId, Long contratoId);
}
