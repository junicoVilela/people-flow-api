package com.peopleflow.pessoascontratos.core.ports.out;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ColaboradorRepositoryPort {
    Colaborador salvar(Colaborador colaborador);
    Optional<Colaborador> buscarPorId(Long id);
    Page<Colaborador> listarTodos(Pageable pageable);
    Page<Colaborador> buscarPorFiltros(ColaboradorFiltros filtros, Pageable pageable);
    void deletar(Long id);
}
