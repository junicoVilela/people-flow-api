package com.peopleflow.organizacao.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.common.validation.AccessValidatorPort;
import com.peopleflow.organizacao.core.application.UnidadeService;
import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.ports.input.UnidadeUseCase;
import com.peopleflow.organizacao.core.ports.output.UnidadeRepositoryPort;
import com.peopleflow.organizacao.core.query.UnidadeFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableTransactionManagement
public class UnidadeConfig {

    @Bean
    public UnidadeUseCase unidadeUseCase(
            UnidadeRepositoryPort repository,
            AccessValidatorPort accessValidator) {
        UnidadeService service = new UnidadeService(repository, accessValidator);
        return new TransactionalUnidadeUseCase(service);
    }

    @Transactional
    private static class TransactionalUnidadeUseCase implements UnidadeUseCase {
        private final UnidadeService delegate;

        public TransactionalUnidadeUseCase(UnidadeService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Unidade criar(Unidade unidade) {
            return delegate.criar(unidade);
        }

        @Override
        @Transactional(readOnly = true)
        public Unidade buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<Unidade> buscarPorFiltros(UnidadeFilter filter, Pagination pagination) {
            return delegate.buscarPorFiltros(filter, pagination);
        }

        @Override
        @Transactional
        public Unidade atualizar(Long id, Unidade unidade) {
            return delegate.atualizar(id, unidade);
        }

        @Override
        @Transactional
        public Unidade ativar(Long id) {
            return delegate.ativar(id);
        }

        @Override
        @Transactional
        public Unidade inativar(Long id) {
            return delegate.inativar(id);
        }

        @Override
        @Transactional
        public Unidade excluir(Long id) {
            return delegate.excluir(id);
        }
    }
}
