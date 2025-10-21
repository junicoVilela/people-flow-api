package com.peopleflow.pessoascontratos.core.ports.in;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorFiltros;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ColaboradorUseCase {
    Colaborador criar(Colaborador colaborador);
    Colaborador buscarPorId(Long id);
    Page<Colaborador> listarTodos(Pageable pageable);
    Page<Colaborador> buscarPorFiltros(ColaboradorFiltros filtros, Pageable pageable);
    Colaborador atualizar(Long id, Colaborador colaborador);
    void deletar(Long id);
}
