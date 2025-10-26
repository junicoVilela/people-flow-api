package com.peopleflow.pessoascontratos.core.ports.out;

import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ColaboradorRepositoryPort {
    Colaborador salvar(Colaborador colaborador);
    Optional<Colaborador> buscarPorId(Long id);
    List<Colaborador> listarTodos();
    Page<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pageable pageable);
    void deletar(Long id);
    
    boolean existePorCpf(String cpf);
    boolean existePorEmail(String email);
    boolean existePorMatricula(String matricula);
    
    boolean existePorCpfExcluindoId(String cpf, Long id);
    boolean existePorEmailExcluindoId(String email, Long id);
    boolean existePorMatriculaExcluindoId(String matricula, Long id);
}
