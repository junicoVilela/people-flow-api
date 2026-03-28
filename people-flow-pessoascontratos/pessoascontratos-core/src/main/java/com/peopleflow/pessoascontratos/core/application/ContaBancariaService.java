package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.ContaBancaria;
import com.peopleflow.pessoascontratos.core.query.ContaBancariaFilter;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.ContaBancariaUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.ContaBancariaRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class ContaBancariaService implements ContaBancariaUseCase {

    private static final Logger log = LoggerFactory.getLogger(ContaBancariaService.class);

    private final ContaBancariaRepositoryPort repository;
    private final ColaboradorUseCase colaboradorUseCase;

    @Override
    public ContaBancaria adicionar(Long colaboradorId, ContaBancaria dados) {
        colaboradorUseCase.buscarPorId(colaboradorId);
        ContaBancaria nova = ContaBancaria.novo(
                colaboradorId,
                dados.getBanco(),
                dados.getAgencia(),
                dados.getConta(),
                dados.getTipo(),
                dados.getPix());
        ContaBancaria salva = repository.salvar(nova);
        log.info("Conta bancária criada: id={}, colaboradorId={}", salva.getId(), colaboradorId);
        return salva;
    }

    @Override
    public ContaBancaria atualizar(Long colaboradorId, Long contaId, ContaBancaria dados) {
        ContaBancaria existente = buscarDoColaborador(colaboradorId, contaId);
        ContaBancaria atualizada = existente.atualizar(
                dados.getBanco(),
                dados.getAgencia(),
                dados.getConta(),
                dados.getTipo(),
                dados.getPix());
        return repository.salvar(atualizada);
    }

    @Override
    public ContaBancaria buscarPorId(Long colaboradorId, Long contaId) {
        return buscarDoColaborador(colaboradorId, contaId);
    }

    @Override
    public PagedResult<ContaBancaria> buscarPorFiltros(Long colaboradorId, ContaBancariaFilter filtros, Pagination pagination) {
        colaboradorUseCase.buscarPorId(colaboradorId);
        return repository.buscarPorFiltros(colaboradorId, filtros, pagination);
    }

    @Override
    public void excluir(Long colaboradorId, Long contaId) {
        ContaBancaria c = buscarDoColaborador(colaboradorId, contaId);
        repository.excluir(c.getId());
        log.info("Conta bancária excluída: id={}", contaId);
    }

    private ContaBancaria buscarDoColaborador(Long colaboradorId, Long contaId) {
        colaboradorUseCase.buscarPorId(colaboradorId);
        ContaBancaria c = repository.buscarAtivoPorId(contaId)
                .orElseThrow(() -> new ResourceNotFoundException("ContaBancaria", contaId));
        if (!c.getColaboradorId().equals(colaboradorId)) {
            throw new ResourceNotFoundException("ContaBancaria", contaId);
        }
        return c;
    }
}
