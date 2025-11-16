package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;

import java.time.LocalDate;

public interface ColaboradorUseCase {
    Colaborador criar(Colaborador colaborador);
    Colaborador buscarPorId(Long id);
    PagedResult<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pagination pagination);
    Colaborador atualizar(Long id, Colaborador colaborador);
    Colaborador demitir(Long id, LocalDate dataDemissao);
    Colaborador ativar(Long id);
    Colaborador inativar(Long id);
    Colaborador excluir(Long id);
}

