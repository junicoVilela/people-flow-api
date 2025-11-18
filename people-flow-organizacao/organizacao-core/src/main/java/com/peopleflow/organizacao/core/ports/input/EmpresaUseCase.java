package com.peopleflow.organizacao.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.query.EmpresaFilter;

import java.time.LocalDate;

public interface EmpresaUseCase {

    Empresa criar(Empresa colaborador);
    Empresa atualizar(Long id, Empresa colaborador);
    Empresa buscarPorId(Long id);
    PagedResult<Empresa> buscarPorFiltros(EmpresaFilter filter, Pagination pagination);
    Empresa excluir(Long id);
    Empresa ativar(Long id);
    Empresa inativar(Long id);
}
