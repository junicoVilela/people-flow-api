package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.pessoascontratos.core.domain.FamiliaCargo;

import java.util.List;
import java.util.Optional;

public interface FamiliaCargoRepositoryPort {

    FamiliaCargo salvar(FamiliaCargo familiaCargo);

    Optional<FamiliaCargo> buscarAtivoPorId(Long id);

    List<FamiliaCargo> listarAtivosNome();

    boolean existeNomeIgnorandoMaiusculas(String nome, Long excluirId);

    boolean existeAtivoPorId(Long id);

    long contarCargosAtivosVinculados(Long familiaCargoId);

    void excluir(Long id);
}
