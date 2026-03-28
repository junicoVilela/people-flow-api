package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.application.DocumentoContratoService;
import com.peopleflow.pessoascontratos.core.domain.DocumentoContrato;
import com.peopleflow.pessoascontratos.core.query.DocumentoContratoFilter;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.DocumentoContratoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.ContratoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.DocumentoContratoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DocumentoContratoConfig {

    @Bean
    public DocumentoContratoUseCase documentoContratoUseCase(
            DocumentoContratoRepositoryPort documentoRepository,
            ContratoRepositoryPort contratoRepository,
            ColaboradorUseCase colaboradorUseCase) {
        DocumentoContratoService service =
                new DocumentoContratoService(documentoRepository, contratoRepository, colaboradorUseCase);
        return new TransactionalDocumentoContratoUseCase(service);
    }

    private static class TransactionalDocumentoContratoUseCase implements DocumentoContratoUseCase {

        private final DocumentoContratoService delegate;

        public TransactionalDocumentoContratoUseCase(DocumentoContratoService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public DocumentoContrato adicionar(Long contratoId, DocumentoContrato documento) {
            return delegate.adicionar(contratoId, documento);
        }

        @Override
        @Transactional(readOnly = true)
        public DocumentoContrato buscarPorId(Long contratoId, Long id) {
            return delegate.buscarPorId(contratoId, id);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<DocumentoContrato> buscarPorFiltros(
                Long contratoId, DocumentoContratoFilter filtros, Pagination pagination) {
            return delegate.buscarPorFiltros(contratoId, filtros, pagination);
        }

        @Override
        @Transactional
        public void excluir(Long contratoId, Long id) {
            delegate.excluir(contratoId, id);
        }
    }
}
