package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.pessoascontratos.core.domain.NivelHierarquico;

import java.util.List;
import java.util.Optional;

public interface NivelHierarquicoRepositoryPort {

    NivelHierarquico salvar(NivelHierarquico nivelHierarquico);

    Optional<NivelHierarquico> buscarAtivoPorId(Long id);

    List<NivelHierarquico> listarAtivosOrdemCrescente();

    boolean existeNomeIgnorandoMaiusculas(String nome, Long excluirId);

    long contarCargosAtivosVinculados(Long nivelHierarquicoId);

    void excluir(Long id);
}
