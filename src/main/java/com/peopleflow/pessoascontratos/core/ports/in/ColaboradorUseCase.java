package com.peopleflow.pessoascontratos.core.ports.in;

import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

/**
 * Port de entrada (driving port) para casos de uso de Colaborador
 * 
 * Define as operações que a aplicação oferece para gerenciar colaboradores.
 * Este é o contrato que a camada de aplicação expõe para as camadas externas.
 */
public interface ColaboradorUseCase {
    Colaborador criar(Colaborador colaborador);
    Colaborador buscarPorId(Long id);
    Page<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pageable pageable);
    Colaborador atualizar(Long id, Colaborador colaborador);
    void deletar(Long id);
    Colaborador demitir(Long id, LocalDate dataDemissao);
    Colaborador ativar(Long id);
    Colaborador inativar(Long id);
    Colaborador excluir(Long id);
}
