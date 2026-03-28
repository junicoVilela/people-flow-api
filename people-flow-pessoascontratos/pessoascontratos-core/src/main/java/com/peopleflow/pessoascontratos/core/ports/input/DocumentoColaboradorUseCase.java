package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoColaborador;
import com.peopleflow.pessoascontratos.core.query.DocumentoColaboradorFilter;

public interface DocumentoColaboradorUseCase {

    DocumentoColaborador adicionar(Long colaboradorId, DocumentoColaborador documento);

    DocumentoColaborador buscarPorId(Long colaboradorId, Long id);

    PagedResult<DocumentoColaborador> buscarPorFiltros(Long colaboradorId, DocumentoColaboradorFilter filtros, Pagination pagination);

    void excluir(Long colaboradorId, Long id);
}
