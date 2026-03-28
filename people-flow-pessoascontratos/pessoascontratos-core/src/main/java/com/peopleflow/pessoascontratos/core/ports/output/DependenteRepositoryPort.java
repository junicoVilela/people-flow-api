package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Dependente;

import java.util.Optional;

public interface DependenteRepositoryPort {

    Dependente salvar(Dependente dependente);

    Optional<Dependente> buscarAtivoPorId(Long id);

    PagedResult<Dependente> listarPorColaboradorId(Long colaboradorId, Pagination pagination);

    void excluir(Long id);
}
