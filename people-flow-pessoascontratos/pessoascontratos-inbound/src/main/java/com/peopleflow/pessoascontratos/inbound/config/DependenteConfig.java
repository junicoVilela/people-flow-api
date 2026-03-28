package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.application.DependenteService;
import com.peopleflow.pessoascontratos.core.domain.Dependente;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.DependenteUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.DependenteRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DependenteConfig {

    @Bean
    public DependenteUseCase dependenteUseCase(
            DependenteRepositoryPort repository,
            ColaboradorUseCase colaboradorUseCase) {
        DependenteService service = new DependenteService(repository, colaboradorUseCase);
        return new TransactionalDependenteUseCase(service);
    }

    private static class TransactionalDependenteUseCase implements DependenteUseCase {

        private final DependenteService delegate;

        TransactionalDependenteUseCase(DependenteService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Dependente adicionar(Long colaboradorId, Dependente dados) {
            return delegate.adicionar(colaboradorId, dados);
        }

        @Override
        @Transactional
        public Dependente atualizar(Long colaboradorId, Long dependenteId, Dependente dados) {
            return delegate.atualizar(colaboradorId, dependenteId, dados);
        }

        @Override
        @Transactional(readOnly = true)
        public Dependente buscarPorId(Long colaboradorId, Long dependenteId) {
            return delegate.buscarPorId(colaboradorId, dependenteId);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<Dependente> listarPorColaborador(Long colaboradorId, Pagination pagination) {
            return delegate.listarPorColaborador(colaboradorId, pagination);
        }

        @Override
        @Transactional
        public void excluir(Long colaboradorId, Long dependenteId) {
            delegate.excluir(colaboradorId, dependenteId);
        }
    }
}
