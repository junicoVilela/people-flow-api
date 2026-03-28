package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoContrato;

import java.util.Optional;

public interface DocumentoContratoRepositoryPort {

    DocumentoContrato salvar(DocumentoContrato documento);

    Optional<DocumentoContrato> buscarPorId(Long id);

    PagedResult<DocumentoContrato> buscarPorContratoId(Long contratoId, Pagination pagination);

    void excluir(Long id);

    long contarAtivosPorContratoId(Long contratoId);
}
