package com.peopleflow.organizacao.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.application.AreaService;
import com.peopleflow.organizacao.core.domain.Area;
import com.peopleflow.organizacao.core.ports.input.AreaUseCase;
import com.peopleflow.organizacao.core.ports.output.AreaRepositoryPort;
import com.peopleflow.organizacao.core.query.AreaFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableTransactionManagement
public class AreaConfig {

    @Bean
    public AreaUseCase areaUseCase(AreaRepositoryPort repository) {
        AreaService service = new AreaService(repository);
        return new TransactionalAreaUseCase(service);
    }

    @Transactional
    private static class TransactionalAreaUseCase implements AreaUseCase {
        private final AreaService delegate;

        public TransactionalAreaUseCase(AreaService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Area criar(Area area) {
            return delegate.criar(area);
        }

        @Override
        @Transactional
        public Area atualizar(Long id, Area area) {
            return delegate.atualizar(id, area);
        }

        @Override
        @Transactional(readOnly = true)
        public Area buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<Area> buscarPorFiltros(AreaFilter filter, Pagination pagination) {
            return delegate.buscarPorFiltros(filter, pagination);
        }

        @Override
        @Transactional
        public Area ativar(Long id) {
            return delegate.ativar(id);
        }

        @Override
        @Transactional
        public Area inativar(Long id) {
            return delegate.inativar(id);
        }

        @Override
        @Transactional
        public Area excluir(Long id) {
            return delegate.excluir(id);
        }
    }
}
