package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.pessoascontratos.core.application.FamiliaCargoService;
import com.peopleflow.pessoascontratos.core.domain.FamiliaCargo;
import com.peopleflow.pessoascontratos.core.ports.input.FamiliaCargoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.FamiliaCargoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class FamiliaCargoConfig {

    @Bean
    public FamiliaCargoUseCase familiaCargoUseCase(FamiliaCargoRepositoryPort repository) {
        FamiliaCargoService service = new FamiliaCargoService(repository);
        return new TransactionalFamiliaCargoUseCase(service);
    }

    private static class TransactionalFamiliaCargoUseCase implements FamiliaCargoUseCase {

        private final FamiliaCargoService delegate;

        TransactionalFamiliaCargoUseCase(FamiliaCargoService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public FamiliaCargo adicionar(FamiliaCargo familiaCargo) {
            return delegate.adicionar(familiaCargo);
        }

        @Override
        @Transactional
        public FamiliaCargo atualizar(Long id, FamiliaCargo familiaCargo) {
            return delegate.atualizar(id, familiaCargo);
        }

        @Override
        @Transactional(readOnly = true)
        public FamiliaCargo buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }

        @Override
        @Transactional(readOnly = true)
        public List<FamiliaCargo> listarTodos() {
            return delegate.listarTodos();
        }

        @Override
        @Transactional
        public void excluir(Long id) {
            delegate.excluir(id);
        }
    }
}
