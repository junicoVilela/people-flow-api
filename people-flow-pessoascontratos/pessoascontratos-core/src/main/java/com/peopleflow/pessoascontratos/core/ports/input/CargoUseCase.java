package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Cargo;

public interface CargoUseCase {

    Cargo criar(Cargo dados);

    Cargo atualizar(Long id, Cargo dados);

    Cargo buscarPorId(Long id);

    PagedResult<Cargo> listar(Pagination pagination);

    void excluir(Long id);
}
