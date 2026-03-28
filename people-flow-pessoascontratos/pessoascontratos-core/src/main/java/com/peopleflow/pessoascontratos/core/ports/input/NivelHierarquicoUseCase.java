package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.pessoascontratos.core.domain.NivelHierarquico;

import java.util.List;

public interface NivelHierarquicoUseCase {

    NivelHierarquico adicionar(NivelHierarquico nivelHierarquico);
    NivelHierarquico atualizar(Long id, NivelHierarquico nivelHierarquico);
    NivelHierarquico buscarPorId(Long id);
    List<NivelHierarquico> listarTodos();
    void excluir(Long id);
}
