package com.peopleflow.pessoascontratos.core.ports.in;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.model.ColaboradorFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ColaboradorUseCase {
    Colaborador criar(Colaborador colaborador);
    Colaborador buscarPorId(Long id);
    List<Colaborador> listarTodos();
    Page<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pageable pageable);
    Colaborador atualizar(Long id, Colaborador colaborador);
    void deletar(Long id);
    Colaborador demitir(Long id, LocalDate dataDemissao);
    Colaborador ativar(Long id);
    Colaborador inativar(Long id);
    Colaborador excluir(Long id);
}
