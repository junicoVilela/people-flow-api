package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoColaborador;
import com.peopleflow.pessoascontratos.core.query.DocumentoColaboradorFilter;

import java.util.Optional;

public interface DocumentoColaboradorRepositoryPort {

    DocumentoColaborador salvar(DocumentoColaborador documento);

    Optional<DocumentoColaborador> buscarPorId(Long id);

    PagedResult<DocumentoColaborador> buscarPorFiltros(Long colaboradorId, DocumentoColaboradorFilter filtros, Pagination pagination);

    void excluir(Long id);
}
