package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.application.CargoService;
import com.peopleflow.pessoascontratos.core.domain.Cargo;
import com.peopleflow.pessoascontratos.core.ports.input.CargoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.CargoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.FamiliaCargoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.NivelHierarquicoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class CargoConfig {

    @Bean
    public CargoUseCase cargoUseCase(
            CargoRepositoryPort cargoRepository,
            NivelHierarquicoRepositoryPort nivelHierarquicoRepository,
            FamiliaCargoRepositoryPort familiaCargoRepository) {
        CargoService service = new CargoService(cargoRepository, nivelHierarquicoRepository, familiaCargoRepository);
        return new TransactionalCargoUseCase(service);
    }

    private static class TransactionalCargoUseCase implements CargoUseCase {

        private final CargoService delegate;

        TransactionalCargoUseCase(CargoService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public Cargo criar(Cargo dados) {
            return delegate.criar(dados);
        }

        @Override
        @Transactional
        public Cargo atualizar(Long id, Cargo dados) {
            return delegate.atualizar(id, dados);
        }

        @Override
        @Transactional(readOnly = true)
        public Cargo buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }

        @Override
        @Transactional(readOnly = true)
        public PagedResult<Cargo> listar(Pagination pagination) {
            return delegate.listar(pagination);
        }

        @Override
        @Transactional
        public void excluir(Long id) {
            delegate.excluir(id);
        }
    }
}
