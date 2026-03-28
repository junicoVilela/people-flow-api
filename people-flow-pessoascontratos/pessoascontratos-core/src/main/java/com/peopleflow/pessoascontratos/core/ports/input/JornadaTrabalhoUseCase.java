package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.pessoascontratos.core.domain.JornadaTrabalho;

import java.util.List;

public interface JornadaTrabalhoUseCase {

    JornadaTrabalho criar(JornadaTrabalho dados);

    JornadaTrabalho atualizar(Long id, JornadaTrabalho dados);

    JornadaTrabalho buscarPorId(Long id);

    List<JornadaTrabalho> listarTodos();

    void excluir(Long id);
}
