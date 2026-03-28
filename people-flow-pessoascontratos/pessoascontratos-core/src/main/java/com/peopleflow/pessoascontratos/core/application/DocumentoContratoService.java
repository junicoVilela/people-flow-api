package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Contrato;
import com.peopleflow.pessoascontratos.core.domain.DocumentoContrato;
import com.peopleflow.pessoascontratos.core.query.DocumentoContratoFilter;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.DocumentoContratoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.ContratoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.DocumentoContratoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class DocumentoContratoService implements DocumentoContratoUseCase {

    private static final Logger log = LoggerFactory.getLogger(DocumentoContratoService.class);

    private final DocumentoContratoRepositoryPort documentoRepository;
    private final ContratoRepositoryPort contratoRepository;
    private final ColaboradorUseCase colaboradorUseCase;

    @Override
    public DocumentoContrato adicionar(Long contratoId, DocumentoContrato documento) {
        log.info("Adicionando documento ao contrato: contratoId={}, tipo={}, nomeArquivo={}",
                contratoId, documento.getTipo(), documento.getNomeArquivo());

        Contrato contrato = resolverContratoAtivo(contratoId);
        colaboradorUseCase.buscarPorId(contrato.getColaboradorId());

        DocumentoContrato novo = DocumentoContrato.novoDocumento(
                contratoId,
                documento.getTipo(),
                documento.getNomeArquivo(),
                documento.getMimeType(),
                documento.getTamanhoBytes(),
                documento.getStorageKey()
        );

        DocumentoContrato salvo = documentoRepository.salvar(novo);
        log.info("Documento de contrato adicionado: id={}, contratoId={}, tipo={}",
                salvo.getId(), contratoId, salvo.getTipo());
        return salvo;
    }

    @Override
    public DocumentoContrato buscarPorId(Long contratoId, Long id) {
        log.debug("Buscando documento de contrato: contratoId={}, id={}", contratoId, id);

        Contrato contrato = resolverContratoAtivo(contratoId);
        colaboradorUseCase.buscarPorId(contrato.getColaboradorId());

        DocumentoContrato documento = documentoRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentoContrato", id));

        if (!documento.getContratoId().equals(contratoId)) {
            throw new ResourceNotFoundException("DocumentoContrato", id);
        }

        return documento;
    }

    @Override
    public PagedResult<DocumentoContrato> buscarPorFiltros(
            Long contratoId, DocumentoContratoFilter filtros, Pagination pagination) {
        log.debug("Buscando documentos do contrato: contratoId={}", contratoId);

        Contrato contrato = resolverContratoAtivo(contratoId);
        colaboradorUseCase.buscarPorId(contrato.getColaboradorId());

        return documentoRepository.buscarPorFiltros(contratoId, filtros, pagination);
    }

    @Override
    public void excluir(Long contratoId, Long id) {
        log.info("Excluindo documento de contrato (soft delete): contratoId={}, id={}", contratoId, id);

        DocumentoContrato documento = buscarPorId(contratoId, id);
        documentoRepository.excluir(documento.getId());
        log.info("Documento de contrato excluído: id={}", id);
    }

    private Contrato resolverContratoAtivo(Long contratoId) {
        return contratoRepository.buscarAtivoPorId(contratoId)
                .orElseThrow(() -> {
                    log.warn("Contrato não encontrado ou excluído: id={}", contratoId);
                    return new ResourceNotFoundException("Contrato", contratoId);
                });
    }
}
