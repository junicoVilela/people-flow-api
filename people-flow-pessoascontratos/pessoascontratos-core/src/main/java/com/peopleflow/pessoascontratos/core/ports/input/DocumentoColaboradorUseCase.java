package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoColaborador;

public interface DocumentoColaboradorUseCase {

    DocumentoColaborador adicionar(Long colaboradorId, DocumentoColaborador documento);

    DocumentoColaborador buscarPorId(Long colaboradorId, Long id);

    PagedResult<DocumentoColaborador> listarPorColaborador(Long colaboradorId, Pagination pagination);

    void excluir(Long colaboradorId, Long id);
}
