package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoColaborador;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.DocumentoColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.DocumentoColaboradorRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class DocumentoColaboradorService implements DocumentoColaboradorUseCase {

    private static final Logger log = LoggerFactory.getLogger(DocumentoColaboradorService.class);

    private final DocumentoColaboradorRepositoryPort documentoRepository;
    private final ColaboradorUseCase colaboradorUseCase;

    @Override
    public DocumentoColaborador adicionar(Long colaboradorId, DocumentoColaborador documento) {
        log.info("Adicionando documento ao colaborador: colaboradorId={}, tipo={}, nomeArquivo={}",
                colaboradorId, documento.getTipo(), documento.getNomeArquivo());

        colaboradorUseCase.buscarPorId(colaboradorId);

        DocumentoColaborador novoDocumento = DocumentoColaborador.novoDocumento(
                colaboradorId,
                documento.getTipo(),
                documento.getNomeArquivo(),
                documento.getMimeType(),
                documento.getTamanhoBytes(),
                documento.getStorageKey()
        );

        DocumentoColaborador salvo = documentoRepository.salvar(novoDocumento);

        log.info("Documento adicionado com sucesso: id={}, colaboradorId={}, tipo={}",
                salvo.getId(), colaboradorId, salvo.getTipo());

        return salvo;
    }

    @Override
    public DocumentoColaborador buscarPorId(Long colaboradorId, Long id) {
        log.debug("Buscando documento: colaboradorId={}, id={}", colaboradorId, id);

        DocumentoColaborador documento = documentoRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentoColaborador", id));

        if (!documento.getColaboradorId().equals(colaboradorId)) {
            throw new ResourceNotFoundException("DocumentoColaborador", id);
        }

        return documento;
    }

    @Override
    public PagedResult<DocumentoColaborador> listarPorColaborador(Long colaboradorId, Pagination pagination) {
        log.debug("Listando documentos do colaborador: colaboradorId={}", colaboradorId);

        colaboradorUseCase.buscarPorId(colaboradorId);

        return documentoRepository.buscarPorColaboradorId(colaboradorId, pagination);
    }

    @Override
    public void excluir(Long colaboradorId, Long id) {
        log.info("Excluindo documento (soft delete): colaboradorId={}, id={}", colaboradorId, id);

        DocumentoColaborador documento = buscarPorId(colaboradorId, id);
        documentoRepository.excluir(documento.getId());

        log.info("Documento excluído com sucesso: id={}", id);
    }
}
