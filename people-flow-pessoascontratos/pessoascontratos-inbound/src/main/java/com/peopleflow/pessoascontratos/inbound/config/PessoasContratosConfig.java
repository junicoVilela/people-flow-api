package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.SecurityContext;
import com.peopleflow.pessoascontratos.core.ports.output.ColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.DomainEventPublisher;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import com.peopleflow.pessoascontratos.core.query.PagedResult;
import com.peopleflow.pessoascontratos.core.query.Pagination;
import com.peopleflow.pessoascontratos.core.application.ColaboradorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * Configuração do módulo Pessoas e Contratos
 * 
 * Registra os beans do core e configura transações.
 * Esta configuração permite que o core seja puro, sem anotações Spring.
 * As transações são gerenciadas através de um wrapper transacional.
 */
@Configuration
@EnableTransactionManagement
public class PessoasContratosConfig {
    
    /**
     * Registra o ColaboradorService como bean do Spring com suporte a transações
     * 
     * O wrapper aplica @Transactional aos métodos, permitindo que o core
     * permaneça puro sem anotações Spring.
     */
    @Bean
    public ColaboradorUseCase colaboradorUseCase(
            ColaboradorRepositoryPort repository,
            DomainEventPublisher eventPublisher,
            SecurityContext securityContext) {
        ColaboradorService service = new ColaboradorService(repository, eventPublisher, securityContext);
        return new TransactionalColaboradorUseCase(service);
    }
    
    /**
     * Wrapper transacional para ColaboradorUseCase
     * 
     * Aplica transações aos métodos do service sem poluir o core com anotações Spring.
     */
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
        public void deletar(Long id) {
            delegate.deletar(id);
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

