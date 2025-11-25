package com.peopleflow.organizacao.inbound.config;

import com.peopleflow.organizacao.core.application.EmpresaService;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.ports.input.EmpresaUseCase;
import com.peopleflow.organizacao.core.ports.output.EmpresaRepositoryPort;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

/**
 * Configuração para o agregado de Empresas.
 *
 * Registra os beans do core e configura transações.
 * Esta configuração permite que o core seja puro, sem anotações Spring.
 * As transações são gerenciadas através de um wrapper transacional.
 */
@Configuration
@EnableTransactionManagement
public class EmpresaConfig {
    
    /**
     * Registra o EmpresaService como bean do Spring com suporte a transações
     * 
     * O wrapper aplica @Transactional aos métodos, permitindo que o core
     * permaneça puro sem anotações Spring.
     */
    @Bean
    public EmpresaUseCase empresaUseCase(EmpresaRepositoryPort repository) {
        EmpresaService service = new EmpresaService(repository);
        return new TransactionalEmpresaUseCase(service);
    }
    
    /**
     * Wrapper transacional para EmpresaUseCase
     * 
     * Aplica transações aos métodos do service sem poluir o core com anotações Spring.
     */
    @Transactional
    private static class TransactionalEmpresaUseCase implements EmpresaUseCase {
        private final EmpresaService delegate;
        
        public TransactionalEmpresaUseCase(EmpresaService delegate) {
            this.delegate = delegate;
        }
        
        @Override
        @Transactional
        public Empresa criar(Empresa empresa) {
            return delegate.criar(empresa);
        }
        
        @Override
        @Transactional(readOnly = true)
        public Empresa buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }
        
        @Override
        @Transactional(readOnly = true)
        public PagedResult<Empresa> buscarPorFiltros(EmpresaFilter filter, Pagination pagination) {
            return delegate.buscarPorFiltros(filter, pagination);
        }
        
        @Override
        @Transactional
        public Empresa atualizar(Long id, Empresa empresa) {
            return delegate.atualizar(id, empresa);
        }
        
        @Override
        @Transactional
        public Empresa ativar(Long id) {
            return delegate.ativar(id);
        }
        
        @Override
        @Transactional
        public Empresa inativar(Long id) {
            return delegate.inativar(id);
        }
        
        @Override
        @Transactional
        public Empresa excluir(Long id) {
            return delegate.excluir(id);
        }
    }
}
