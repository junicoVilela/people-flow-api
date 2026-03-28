package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Dependente;

public interface DependenteUseCase {

    Dependente adicionar(Long colaboradorId, Dependente dados);

    Dependente atualizar(Long colaboradorId, Long dependenteId, Dependente dados);

    Dependente buscarPorId(Long colaboradorId, Long dependenteId);

    PagedResult<Dependente> listarPorColaborador(Long colaboradorId, Pagination pagination);

    void excluir(Long colaboradorId, Long dependenteId);
}
