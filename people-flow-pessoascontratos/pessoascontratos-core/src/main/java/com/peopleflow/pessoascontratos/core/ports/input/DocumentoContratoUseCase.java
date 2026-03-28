package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoContrato;
import com.peopleflow.pessoascontratos.core.query.DocumentoContratoFilter;

public interface DocumentoContratoUseCase {

    DocumentoContrato adicionar(Long contratoId, DocumentoContrato documento);

    DocumentoContrato buscarPorId(Long contratoId, Long id);

    PagedResult<DocumentoContrato> buscarPorFiltros(Long contratoId, DocumentoContratoFilter filtros, Pagination pagination);

    void excluir(Long contratoId, Long id);
}
