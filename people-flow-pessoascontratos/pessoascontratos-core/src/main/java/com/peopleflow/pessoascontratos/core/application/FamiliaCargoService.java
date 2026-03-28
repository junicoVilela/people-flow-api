package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.pessoascontratos.core.domain.FamiliaCargo;
import com.peopleflow.pessoascontratos.core.ports.input.FamiliaCargoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.FamiliaCargoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class FamiliaCargoService implements FamiliaCargoUseCase {

    private static final Logger log = LoggerFactory.getLogger(FamiliaCargoService.class);

    private final FamiliaCargoRepositoryPort repository;

    @Override
    public FamiliaCargo adicionar(FamiliaCargo familiaCargo) {
        validarNomeUnico(familiaCargo.getNome(), null);
        FamiliaCargo novo = FamiliaCargo.nova(familiaCargo.getNome(), familiaCargo.getDescricao());
        FamiliaCargo salvo = repository.salvar(novo);
        log.info("Família de cargo criada: id={}, nome={}", salvo.getId(), salvo.getNome());
        return salvo;
    }

    @Override
    public FamiliaCargo atualizar(Long id, FamiliaCargo dados) {
        FamiliaCargo existente = buscarAtivo(id);
        validarNomeUnico(dados.getNome(), id);
        FamiliaCargo atualizado = existente.atualizar(dados.getNome(), dados.getDescricao());
        FamiliaCargo salvo = repository.salvar(atualizado);
        log.info("Família de cargo atualizada: id={}", salvo.getId());
        return salvo;
    }

    @Override
    public FamiliaCargo buscarPorId(Long id) {
        return buscarAtivo(id);
    }

    @Override
    public List<FamiliaCargo> listarTodos() {
        return repository.listarAtivosNome();
    }

    @Override
    public void excluir(Long id) {
        buscarAtivo(id);
        if (repository.contarCargosAtivosVinculados(id) > 0) {
            throw new BusinessException(
                    "FAMILIA_CARGO_EM_USO",
                    "Não é possível excluir: existem cargos ativos nesta família.");
        }
        repository.excluir(id);
        log.info("Família de cargo excluída: id={}", id);
    }

    private FamiliaCargo buscarAtivo(Long id) {
        return repository.buscarAtivoPorId(id)
                .orElseThrow(() -> {
                    log.warn("Família de cargo não encontrada: id={}", id);
                    return new ResourceNotFoundException("FamiliaCargo", id);
                });
    }

    private void validarNomeUnico(String nome, Long excluirId) {
        if (repository.existeNomeIgnorandoMaiusculas(nome, excluirId)) {
            throw new BusinessException(
                    "FAMILIA_CARGO_NOME_DUPLICADO",
                    "Já existe uma família de cargo ativa com este nome.");
        }
    }
}
