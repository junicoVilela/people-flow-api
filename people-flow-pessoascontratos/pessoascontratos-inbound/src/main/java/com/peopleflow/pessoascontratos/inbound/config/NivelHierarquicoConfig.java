package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.pessoascontratos.core.application.NivelHierarquicoService;
import com.peopleflow.pessoascontratos.core.domain.NivelHierarquico;
import com.peopleflow.pessoascontratos.core.ports.input.NivelHierarquicoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.NivelHierarquicoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class NivelHierarquicoConfig {

    @Bean
    public NivelHierarquicoUseCase nivelHierarquicoUseCase(NivelHierarquicoRepositoryPort repository) {
        NivelHierarquicoService service = new NivelHierarquicoService(repository);
        return new TransactionalNivelHierarquicoUseCase(service);
    }

    private static class TransactionalNivelHierarquicoUseCase implements NivelHierarquicoUseCase {

        private final NivelHierarquicoService delegate;

        public TransactionalNivelHierarquicoUseCase(NivelHierarquicoService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public NivelHierarquico adicionar(NivelHierarquico nivelHierarquico) {
            return delegate.adicionar(nivelHierarquico);
        }

        @Override
        @Transactional
        public NivelHierarquico atualizar(Long id, NivelHierarquico nivelHierarquico) {
            return delegate.atualizar(id, nivelHierarquico);
        }

        @Override
        @Transactional(readOnly = true)
        public NivelHierarquico buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }

        @Override
        @Transactional(readOnly = true)
        public List<NivelHierarquico> listarTodos() {
            return delegate.listarTodos();
        }

        @Override
        @Transactional
        public void excluir(Long id) {
            delegate.excluir(id);
        }
    }
}
