package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoContrato;

public interface DocumentoContratoUseCase {

    DocumentoContrato adicionar(Long contratoId, DocumentoContrato documento);

    DocumentoContrato buscarPorId(Long contratoId, Long id);

    PagedResult<DocumentoContrato> listarPorContrato(Long contratoId, Pagination pagination);

    void excluir(Long contratoId, Long id);
}
