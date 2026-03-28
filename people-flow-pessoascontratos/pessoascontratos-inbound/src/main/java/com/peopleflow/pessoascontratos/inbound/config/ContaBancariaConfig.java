package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.application.ContaBancariaService;
import com.peopleflow.pessoascontratos.core.domain.ContaBancaria;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.ContaBancariaUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.ContaBancariaRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class ContaBancariaConfig {

    @Bean
    public ContaBancariaUseCase contaBancariaUseCase(
            ContaBancariaRepositoryPort repository,
            ColaboradorUseCase colaboradorUseCase) {
        ContaBancariaService service = new ContaBancariaService(repository, colaboradorUseCase);
        return new TransactionalContaBancariaUseCase(service);
    }

    private static class TransactionalContaBancariaUseCase implements ContaBancariaUseCase {

        private final ContaBancariaService delegate;

        TransactionalContaBancariaUseCase(ContaBancariaService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public ContaBancaria adicionar(Long colaboradorId, ContaBancaria dados) {
            return delegate.adicionar(colaboradorId, dados);
        }

        @Override
        @Transactional
        public ContaBancaria atualizar(Long colaboradorId, Long contaId, ContaBancaria dados) {
            return delegate.atualizar(colaboradorId, contaId, dados);
        }

        @Override
        @Transactional(readOnly = true)
        public ContaBancaria buscarPorId(Long colaboradorId, Long contaId) {
            return delegate.buscarPorId(colaboradorId, contaId);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<ContaBancaria> listarPorColaborador(Long colaboradorId, Pagination pagination) {
            return delegate.listarPorColaborador(colaboradorId, pagination);
        }

        @Override
        @Transactional
        public void excluir(Long colaboradorId, Long contaId) {
            delegate.excluir(colaboradorId, contaId);
        }
    }
}
