package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoColaborador;

import java.util.Optional;

public interface DocumentoColaboradorRepositoryPort {

    DocumentoColaborador salvar(DocumentoColaborador documento);

    Optional<DocumentoColaborador> buscarPorId(Long id);

    PagedResult<DocumentoColaborador> buscarPorColaboradorId(Long colaboradorId, Pagination pagination);

    void excluir(Long id);
}
