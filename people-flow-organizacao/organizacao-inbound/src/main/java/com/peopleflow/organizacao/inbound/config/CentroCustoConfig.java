package com.peopleflow.organizacao.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.common.validation.AccessValidatorPort;
import com.peopleflow.organizacao.core.application.CentroCustoService;
import com.peopleflow.organizacao.core.domain.CentroCusto;
import com.peopleflow.organizacao.core.ports.input.CentroCustoUseCase;
import com.peopleflow.organizacao.core.ports.output.CentroCustoRepositoryPort;
import com.peopleflow.organizacao.core.query.CentroCustoFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableTransactionManagement
public class CentroCustoConfig {

    @Bean
    public CentroCustoUseCase centroCustoUseCase(
            CentroCustoRepositoryPort repository,
            AccessValidatorPort accessValidator) {
        CentroCustoService service = new CentroCustoService(repository, accessValidator);
        return new TransactionalCentroCustoUseCase(service);
    }

    @Transactional
    private static class TransactionalCentroCustoUseCase implements CentroCustoUseCase {
        private final CentroCustoService delegate;

        public TransactionalCentroCustoUseCase(CentroCustoService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public CentroCusto criar(CentroCusto centroCusto) {
            return delegate.criar(centroCusto);
        }

        @Override
        @Transactional(readOnly = true)
        public CentroCusto buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<CentroCusto> buscarPorFiltros(CentroCustoFilter filter, Pagination pagination) {
            return delegate.buscarPorFiltros(filter, pagination);
        }

        @Override
        @Transactional
        public CentroCusto atualizar(Long id, CentroCusto centroCusto) {
            return delegate.atualizar(id, centroCusto);
        }

        @Override
        @Transactional
        public CentroCusto ativar(Long id) {
            return delegate.ativar(id);
        }

        @Override
        @Transactional
        public CentroCusto inativar(Long id) {
            return delegate.inativar(id);
        }

        @Override
        @Transactional
        public CentroCusto excluir(Long id) {
            return delegate.excluir(id);
        }
    }
}
