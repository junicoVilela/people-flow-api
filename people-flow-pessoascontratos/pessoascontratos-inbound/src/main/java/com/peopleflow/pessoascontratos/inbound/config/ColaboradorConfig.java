package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.application.ColaboradorService;
import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.ColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.DomainEventPublisher;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
@EnableTransactionManagement
public class ColaboradorConfig {
    
    @Bean
    public ColaboradorUseCase colaboradorUseCase(
            ColaboradorRepositoryPort repository,
            DomainEventPublisher eventPublisher) {
        ColaboradorService service = new ColaboradorService(repository, eventPublisher);
        return new TransactionalColaboradorUseCase(service);
    }

    @Transactional
    private static class TransactionalColaboradorUseCase implements ColaboradorUseCase {
        private final ColaboradorService delegate;
        
        public TransactionalColaboradorUseCase(ColaboradorService delegate) {
            this.delegate = delegate;
        }
        
        @Override
        @Transactional
        public Colaborador criar(Colaborador colaborador) {
            return delegate.criar(colaborador);
        }
        
        @Override
        @Transactional(readOnly = true)
        public Colaborador buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }
        
        @Override
        @Transactional(readOnly = true)
        public PagedResult<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pagination pagination) {
            return delegate.buscarPorFiltros(filter, pagination);
        }
        
        @Override
        @Transactional
        public Colaborador atualizar(Long id, Colaborador colaborador) {
            return delegate.atualizar(id, colaborador);
        }
        
        @Override
        @Transactional
        public Colaborador demitir(Long id, LocalDate dataDemissao) {
            return delegate.demitir(id, dataDemissao);
        }
        
        @Override
        @Transactional
        public Colaborador ativar(Long id) {
            return delegate.ativar(id);
        }
        
        @Override
        @Transactional
        public Colaborador inativar(Long id) {
            return delegate.inativar(id);
        }
        
        @Override
        @Transactional
        public Colaborador excluir(Long id) {
            return delegate.excluir(id);
        }
    }
}
