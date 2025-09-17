package com.peopleflow.pessoascontratos.core.ports.out;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ColaboradorRepositoryPort {
    Colaborador salvar(Colaborador colaborador);
    Optional<Colaborador> buscarPorId(UUID id);
    List<Colaborador> listarTodos();
    void deletar(UUID id);
} 