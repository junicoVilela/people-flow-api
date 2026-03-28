package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.pessoascontratos.core.domain.JornadaTrabalho;

import java.util.List;
import java.util.Optional;

public interface JornadaTrabalhoRepositoryPort {

    boolean existeAtivaPorId(Long jornadaId);

    JornadaTrabalho salvar(JornadaTrabalho jornada);

    Optional<JornadaTrabalho> buscarAtivoPorId(Long id);

    List<JornadaTrabalho> listarAtivos();

    long contarContratosAtivosVinculados(Long jornadaId);

    void excluir(Long id);
}
