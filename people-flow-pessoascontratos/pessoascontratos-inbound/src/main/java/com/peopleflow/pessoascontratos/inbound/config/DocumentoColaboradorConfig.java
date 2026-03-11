package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.application.DocumentoColaboradorService;
import com.peopleflow.pessoascontratos.core.domain.DocumentoColaborador;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.DocumentoColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.DocumentoColaboradorRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DocumentoColaboradorConfig {

    @Bean
    public DocumentoColaboradorUseCase documentoColaboradorUseCase(
            DocumentoColaboradorRepositoryPort repository,
            ColaboradorUseCase colaboradorUseCase) {
        DocumentoColaboradorService service = new DocumentoColaboradorService(repository, colaboradorUseCase);
        return new TransactionalDocumentoColaboradorUseCase(service);
    }

    @Transactional
    private static class TransactionalDocumentoColaboradorUseCase implements DocumentoColaboradorUseCase {

        private final DocumentoColaboradorService delegate;

        public TransactionalDocumentoColaboradorUseCase(DocumentoColaboradorService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public DocumentoColaborador adicionar(Long colaboradorId, DocumentoColaborador documento) {
            return delegate.adicionar(colaboradorId, documento);
        }

        @Override
        @Transactional(readOnly = true)
        public DocumentoColaborador buscarPorId(Long colaboradorId, Long id) {
            return delegate.buscarPorId(colaboradorId, id);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<DocumentoColaborador> listarPorColaborador(Long colaboradorId, Pagination pagination) {
            return delegate.listarPorColaborador(colaboradorId, pagination);
        }

        @Override
        @Transactional
        public void excluir(Long colaboradorId, Long id) {
            delegate.excluir(colaboradorId, id);
        }
    }
}
