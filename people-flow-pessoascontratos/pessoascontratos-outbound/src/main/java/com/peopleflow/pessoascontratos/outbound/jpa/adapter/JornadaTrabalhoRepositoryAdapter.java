package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.pessoascontratos.core.ports.output.JornadaTrabalhoRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.JornadaTrabalhoJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class JornadaTrabalhoRepositoryAdapter implements JornadaTrabalhoRepositoryPort {

    private final JornadaTrabalhoJpaRepository repository;

    public JornadaTrabalhoRepositoryAdapter(JornadaTrabalhoJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existeAtivaPorId(Long jornadaId) {
        return repository.existsByIdAndExcluidoEmIsNull(jornadaId);
    }
}
