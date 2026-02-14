package com.peopleflow.organizacao.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.common.validation.AccessValidatorPort;
import com.peopleflow.organizacao.core.application.EmpresaService;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.ports.input.EmpresaUseCase;
import com.peopleflow.organizacao.core.ports.output.CentroCustoRepositoryPort;
import com.peopleflow.organizacao.core.ports.output.DepartamentoRepositoryPort;
import com.peopleflow.organizacao.core.ports.output.EmpresaRepositoryPort;
import com.peopleflow.organizacao.core.ports.output.ExisteColaboradorPorEmpresaPort;
import com.peopleflow.organizacao.core.ports.output.UnidadeRepositoryPort;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableTransactionManagement
public class EmpresaConfig {

    @Bean
    public EmpresaUseCase empresaUseCase(
            EmpresaRepositoryPort repository,
            DepartamentoRepositoryPort departamentoRepository,
            UnidadeRepositoryPort unidadeRepository,
            CentroCustoRepositoryPort centroCustoRepository,
            ExisteColaboradorPorEmpresaPort existeColaboradorPorEmpresaPort,
            AccessValidatorPort accessValidator) {
        EmpresaService service = new EmpresaService(repository, departamentoRepository, unidadeRepository, centroCustoRepository, existeColaboradorPorEmpresaPort, accessValidator);
        return new TransactionalEmpresaUseCase(service);
    }

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
