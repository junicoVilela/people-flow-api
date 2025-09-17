package com.peopleflow.pessoascontratos.core.ports.in;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import java.util.List;
import java.util.UUID;

public interface ColaboradorUseCase {
    Colaborador criar(Colaborador colaborador);
    Colaborador buscarPorId(UUID id);
    List<Colaborador> listarTodos();
    Colaborador atualizar(UUID id, Colaborador colaborador);
    void deletar(UUID id);
} 