package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.application.ContratoService;
import com.peopleflow.pessoascontratos.core.domain.Contrato;
import com.peopleflow.pessoascontratos.core.query.ContratoFilter;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.ContratoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.CargoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.ContratoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.DocumentoContratoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.JornadaTrabalhoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class ContratoConfig {

    @Bean
    public ContratoUseCase contratoUseCase(
            ContratoRepositoryPort contratoRepository,
            ColaboradorUseCase colaboradorUseCase,
            JornadaTrabalhoRepositoryPort jornadaTrabalhoRepository,
            CargoRepositoryPort cargoRepository,
            DocumentoContratoRepositoryPort documentoContratoRepository) {
        ContratoService service = new ContratoService(
                contratoRepository,
                colaboradorUseCase,
                jornadaTrabalhoRepository,
                cargoRepository,
                documentoContratoRepository);
        return new TransactionalContratoUseCase(service);
    }

    private static class TransactionalContratoUseCase implements ContratoUseCase {

        private final ContratoService delegate;

        TransactionalContratoUseCase(ContratoService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Contrato criar(Long colaboradorId, Contrato dados) {
            return delegate.criar(colaboradorId, dados);
        }

        @Override
        @Transactional
        public Contrato atualizar(Long colaboradorId, Long contratoId, Contrato dados) {
            return delegate.atualizar(colaboradorId, contratoId, dados);
        }

        @Override
        @Transactional(readOnly = true)
        public Contrato buscarPorId(Long colaboradorId, Long contratoId) {
            return delegate.buscarPorId(colaboradorId, contratoId);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<Contrato> buscarPorFiltros(Long colaboradorId, ContratoFilter filtros, Pagination pagination) {
            return delegate.buscarPorFiltros(colaboradorId, filtros, pagination);
        }

        @Override
        @Transactional
        public void excluir(Long colaboradorId, Long contratoId) {
            delegate.excluir(colaboradorId, contratoId);
        }
    }
}
