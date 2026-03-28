package com.peopleflow.pessoascontratos.inbound.config;

import com.peopleflow.pessoascontratos.core.application.JornadaTrabalhoService;
import com.peopleflow.pessoascontratos.core.domain.JornadaTrabalho;
import com.peopleflow.pessoascontratos.core.ports.input.JornadaTrabalhoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.JornadaTrabalhoRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
public class JornadaTrabalhoConfig {

    @Bean
    public JornadaTrabalhoUseCase jornadaTrabalhoUseCase(JornadaTrabalhoRepositoryPort repository) {
        JornadaTrabalhoService service = new JornadaTrabalhoService(repository);
        return new TransactionalJornadaTrabalhoUseCase(service);
    }

    private static class TransactionalJornadaTrabalhoUseCase implements JornadaTrabalhoUseCase {

        private final JornadaTrabalhoService delegate;

        TransactionalJornadaTrabalhoUseCase(JornadaTrabalhoService delegate) {
            this.delegate = delegate;
        }

        @Override
        @Transactional
        public JornadaTrabalho criar(JornadaTrabalho dados) {
            return delegate.criar(dados);
        }

        @Override
        @Transactional
        public JornadaTrabalho atualizar(Long id, JornadaTrabalho dados) {
            return delegate.atualizar(id, dados);
        }

        @Override
        @Transactional(readOnly = true)
        public JornadaTrabalho buscarPorId(Long id) {
            return delegate.buscarPorId(id);
        }

        @Override
        @Transactional(readOnly = true)
        public List<JornadaTrabalho> listarTodos() {
            return delegate.listarTodos();
        }

        @Override
        @Transactional
        public void excluir(Long id) {
            delegate.excluir(id);
        }
    }
}
