package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import com.peopleflow.pessoascontratos.core.query.PagedResult;
import com.peopleflow.pessoascontratos.core.query.Pagination;

import java.util.Optional;

public interface ColaboradorRepositoryPort {
    Colaborador salvar(Colaborador colaborador);
    Optional<Colaborador> buscarPorId(Long id);
    PagedResult<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pagination pagination);
    void deletar(Long id);
    
    boolean existePorCpf(String cpf);
    boolean existePorEmail(String email);
    boolean existePorMatricula(String matricula);
    
    boolean existePorCpfExcluindoId(String cpf, Long id);
    boolean existePorEmailExcluindoId(String email, Long id);
    boolean existePorMatriculaExcluindoId(String matricula, Long id);
}

