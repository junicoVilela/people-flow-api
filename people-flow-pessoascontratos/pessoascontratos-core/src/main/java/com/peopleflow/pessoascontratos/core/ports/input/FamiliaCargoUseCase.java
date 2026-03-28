package com.peopleflow.pessoascontratos.core.ports.input;

import com.peopleflow.pessoascontratos.core.domain.FamiliaCargo;

import java.util.List;

public interface FamiliaCargoUseCase {

    FamiliaCargo adicionar(FamiliaCargo familiaCargo);

    FamiliaCargo atualizar(Long id, FamiliaCargo familiaCargo);

    FamiliaCargo buscarPorId(Long id);

    List<FamiliaCargo> listarTodos();

    void excluir(Long id);
}
