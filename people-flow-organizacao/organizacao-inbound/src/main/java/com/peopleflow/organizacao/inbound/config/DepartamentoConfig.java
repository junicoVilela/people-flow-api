package com.peopleflow.organizacao.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.common.validation.AccessValidatorPort;
import com.peopleflow.organizacao.core.application.DepartamentoService;
import com.peopleflow.organizacao.core.domain.Departamento;
import com.peopleflow.organizacao.core.ports.input.DepartamentoUseCase;
import com.peopleflow.organizacao.core.ports.output.DepartamentoRepositoryPort;
import com.peopleflow.organizacao.core.query.DepartamentoFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableTransactionManagement
public class DepartamentoConfig {

    @Bean
    public DepartamentoUseCase departamentoUseCase(
            DepartamentoRepositoryPort repository,
            AccessValidatorPort accessValidator) {
        DepartamentoService service = new DepartamentoService(repository, accessValidator);
        return new TransactionalDepartamentoUseCase(service);
    }

    @Transactional
    private static class TransactionalDepartamentoUseCase implements DepartamentoUseCase {
        private final DepartamentoService delegate;

        public TransactionalDepartamentoUseCase(DepartamentoService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Departamento criar(Departamento departamento) {
            return delegate.criar(departamento);
        }

        @Override
        @Transactional(readOnly = true)
        public Departamento buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<Departamento> buscarPorFiltros(DepartamentoFilter filter, Pagination pagination) {
            return delegate.buscarPorFiltros(filter, pagination);
        }

        @Override
        @Transactional
        public Departamento atualizar(Long id, Departamento departamento) {
            return delegate.atualizar(id, departamento);
        }

        @Override
        @Transactional
        public Departamento ativar(Long id) {
            return delegate.ativar(id);
        }

        @Override
        @Transactional
        public Departamento inativar(Long id) {
            return delegate.inativar(id);
        }

        @Override
        @Transactional
        public Departamento excluir(Long id) {
            return delegate.excluir(id);
        }
    }
}
