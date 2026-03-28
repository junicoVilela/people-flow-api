package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.pessoascontratos.core.domain.NivelHierarquico;
import com.peopleflow.pessoascontratos.core.ports.input.NivelHierarquicoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.NivelHierarquicoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class NivelHierarquicoService implements NivelHierarquicoUseCase {

    private static final Logger log = LoggerFactory.getLogger(NivelHierarquicoService.class);

    private final NivelHierarquicoRepositoryPort repository;

    @Override
    public NivelHierarquico adicionar(NivelHierarquico nivelHierarquico) {
        String nome = normalizarNome(nivelHierarquico.getNome());
        String descricao = normalizarDescricao(nivelHierarquico.getDescricao());
        validarNomeUnico(nome, null);

        NivelHierarquico novo = NivelHierarquico.nova(nome, descricao, nivelHierarquico.getOrdem());
        NivelHierarquico salvo = repository.salvar(novo);
        log.info("Nível hierárquico criado: id={}, nome={}", salvo.getId(), salvo.getNome());
        return salvo;
    }

    @Override
    public NivelHierarquico atualizar(Long id, NivelHierarquico nivelHierarquico) {
        NivelHierarquico existente = buscarAtivo(id);
        String nome = normalizarNome(nivelHierarquico.getNome());
        String descricao = normalizarDescricao(nivelHierarquico.getDescricao());
        validarNomeUnico(nome, id);

        NivelHierarquico atualizado = existente.atualizar(nome, descricao, nivelHierarquico.getOrdem());

        NivelHierarquico salvo = repository.salvar(atualizado);
        log.info("Nível hierárquico atualizado: id={}, nome={}", salvo.getId(), salvo.getNome());
        return salvo;
    }

    @Override
    public NivelHierarquico buscarPorId(Long id) {
        return buscarAtivo(id);
    }

    @Override
    public List<NivelHierarquico> listarTodos() {
        return repository.listarAtivosOrdemCrescente();
    }

    @Override
    public void excluir(Long id) {
        buscarAtivo(id);
        if (repository.contarCargosAtivosVinculados(id) > 0) {
            throw new BusinessException(
                    "NIVEL_HIERARQUICO_EM_USO",
                    "Não é possível excluir o nível hierárquico enquanto existirem cargos ativos vinculados a ele.");
        }
        repository.excluir(id);
        log.info("Nível hierárquico excluído (soft delete): id={}", id);
    }

    private NivelHierarquico buscarAtivo(Long id) {
        return repository.buscarAtivoPorId(id)
                .orElseThrow(() -> {
                    log.warn("Nível hierárquico não encontrado ou excluído: id={}", id);
                    return new ResourceNotFoundException("NivelHierarquico", id);
                });
    }

    private void validarNomeUnico(String nome, Long excluirId) {
        if (repository.existeNomeIgnorandoMaiusculas(nome, excluirId)) {
            throw new BusinessException(
                    "NIVEL_HIERARQUICO_NOME_DUPLICADO",
                    "Já existe um nível hierárquico ativo com este nome.");
        }
    }

    private static String normalizarNome(String nome) {
        if (nome == null) {
            return null;
        }
        return nome.trim();
    }

    private static String normalizarDescricao(String descricao) {
        if (descricao == null) {
            return null;
        }
        String t = descricao.trim();
        return t.isEmpty() ? null : t;
    }
}
