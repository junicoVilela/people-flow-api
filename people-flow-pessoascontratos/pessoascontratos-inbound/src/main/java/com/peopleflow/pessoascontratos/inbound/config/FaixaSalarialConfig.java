package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.application.FaixaSalarialService;
import com.peopleflow.pessoascontratos.core.domain.FaixaSalarial;
import com.peopleflow.pessoascontratos.core.ports.input.FaixaSalarialUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.CargoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.FaixaSalarialRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class FaixaSalarialConfig {

    @Bean
    public FaixaSalarialUseCase faixaSalarialUseCase(
            FaixaSalarialRepositoryPort faixaRepository,
            CargoRepositoryPort cargoRepository) {
        FaixaSalarialService service = new FaixaSalarialService(faixaRepository, cargoRepository);
        return new TransactionalFaixaSalarialUseCase(service);
    }

    private static class TransactionalFaixaSalarialUseCase implements FaixaSalarialUseCase {

        private final FaixaSalarialService delegate;

        TransactionalFaixaSalarialUseCase(FaixaSalarialService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public FaixaSalarial adicionar(Long cargoId, FaixaSalarial dados) {
            return delegate.adicionar(cargoId, dados);
        }

        @Override
        @Transactional
        public FaixaSalarial atualizar(Long cargoId, Long faixaId, FaixaSalarial dados) {
            return delegate.atualizar(cargoId, faixaId, dados);
        }

        @Override
        @Transactional(readOnly = true)
        public FaixaSalarial buscarPorId(Long cargoId, Long faixaId) {
            return delegate.buscarPorId(cargoId, faixaId);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<FaixaSalarial> listarPorCargo(Long cargoId, Pagination pagination) {
            return delegate.listarPorCargo(cargoId, pagination);
        }

        @Override
        @Transactional
        public void excluir(Long cargoId, Long faixaId) {
            delegate.excluir(cargoId, faixaId);
        }
    }
}
