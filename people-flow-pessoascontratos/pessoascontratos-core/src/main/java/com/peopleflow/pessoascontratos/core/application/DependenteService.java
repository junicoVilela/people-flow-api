package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Dependente;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.DependenteUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.DependenteRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class DependenteService implements DependenteUseCase {

    private static final Logger log = LoggerFactory.getLogger(DependenteService.class);

    private final DependenteRepositoryPort repository;
    private final ColaboradorUseCase colaboradorUseCase;

    @Override
    public Dependente adicionar(Long colaboradorId, Dependente dados) {
        colaboradorUseCase.buscarPorId(colaboradorId);
        Dependente novo = Dependente.novo(
                colaboradorId,
                dados.getNome(),
                dados.getParentesco(),
                dados.getDataNascimento(),
                dados.getCpf());
        Dependente salvo = repository.salvar(novo);
        log.info("Dependente criado: id={}, colaboradorId={}", salvo.getId(), colaboradorId);
        return salvo;
    }

    @Override
    public Dependente atualizar(Long colaboradorId, Long dependenteId, Dependente dados) {
        Dependente existente = buscarDoColaborador(colaboradorId, dependenteId);
        Dependente atualizado = existente.atualizar(
                dados.getNome(),
                dados.getParentesco(),
                dados.getDataNascimento(),
                dados.getCpf());
        return repository.salvar(atualizado);
    }

    @Override
    public Dependente buscarPorId(Long colaboradorId, Long dependenteId) {
        return buscarDoColaborador(colaboradorId, dependenteId);
    }

    @Override
    public PagedResult<Dependente> listarPorColaborador(Long colaboradorId, Pagination pagination) {
        colaboradorUseCase.buscarPorId(colaboradorId);
        return repository.listarPorColaboradorId(colaboradorId, pagination);
    }

    @Override
    public void excluir(Long colaboradorId, Long dependenteId) {
        Dependente d = buscarDoColaborador(colaboradorId, dependenteId);
        repository.excluir(d.getId());
        log.info("Dependente excluído: id={}", dependenteId);
    }

    private Dependente buscarDoColaborador(Long colaboradorId, Long dependenteId) {
        colaboradorUseCase.buscarPorId(colaboradorId);
        Dependente d = repository.buscarAtivoPorId(dependenteId)
                .orElseThrow(() -> new ResourceNotFoundException("Dependente", dependenteId));
        if (!d.getColaboradorId().equals(colaboradorId)) {
            throw new ResourceNotFoundException("Dependente", dependenteId);
        }
        return d;
    }
}
