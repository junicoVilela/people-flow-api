package com.peopleflow.organizacao.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.DuplicateResourceException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.common.util.ServiceUtils;
import com.peopleflow.common.validation.AccessValidatorPort;
import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.ports.input.UnidadeUseCase;
import com.peopleflow.organizacao.core.ports.output.EmpresaRepositoryPort;
import com.peopleflow.organizacao.core.ports.output.UnidadeRepositoryPort;
import com.peopleflow.organizacao.core.query.UnidadeFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class UnidadeService implements UnidadeUseCase {

    private static final Logger log = LoggerFactory.getLogger(UnidadeService.class);

    private final UnidadeRepositoryPort unidadeRepository;
    private final EmpresaRepositoryPort empresaRepository;
    private final AccessValidatorPort accessValidator;

    @Override
    public Unidade criar(Unidade unidade) {
        log.info("Iniciando criação de Unidade: nome={}, codigo={}, empresaId={}",
                unidade.getNome(),
                unidade.getCodigo(),
                unidade.getEmpresaId());

        validarEmpresaExiste(unidade.getEmpresaId());
        validarUnicidadeCriacao(unidade);

        Unidade unidadeParaSalvar = Unidade.nova(
                unidade.getNome(),
                unidade.getCodigo(),
                unidade.getEmpresaId(),
                unidade.getStatus());
        Unidade unidadeCriar = unidadeRepository.salvar(unidadeParaSalvar);

        log.info("Unidade criada com sucesso: nome={}, codigo={}, empresaId={}, id={}",
                unidadeCriar.getNome(),
                unidadeCriar.getCodigo(),
                unidadeCriar.getEmpresaId(),
                unidadeCriar.getId());

        return unidadeCriar;
    }

    @Override
    public Unidade atualizar(Long id, Unidade unidade) {
        log.info("Iniciando atualização de unidade: id={}", id);

        try {
            if (unidade.getId() != null && !unidade.getId().equals(id)) {
                throw new BusinessException("ID_MISMATCH",
                        String.format("ID do path (%d) diferente do ID do objeto (%d)",
                                id, unidade.getId()));
            }

            Unidade original = buscarPorId(id);
            
            if (!accessValidator.isAdmin()) {
                accessValidator.validarAcessoEmpresa(original.getEmpresaId());
            }

            validarEmpresaExiste(unidade.getEmpresaId());

            Unidade unidadeAtualizar = original.atualizar(
                    unidade.getNome(),
                    unidade.getCodigo(),
                    unidade.getEmpresaId()
                    ).toBuilder()
                    .id(id)
                    .build();

            validarUnicidadeParaAtualizacao(unidadeAtualizar, id);

            Unidade unidadeAtualizado = unidadeRepository.salvar(unidadeAtualizar);

            List<String> camposAlterados = detectarCamposAlterados(original, unidadeAtualizado);

            log.info("Unidade atualizada com sucesso: id={}, codigo={},nome={},empresaId={},camposAlterados={}",
                    id, unidade.getCodigo(), unidade.getNome(), unidade.getEmpresaId(), camposAlterados);

            return unidadeAtualizado;
        } catch (BusinessException e) {
            log.warn("Erro ao atualizar unidade: id={}, erro={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar unidade: id={}", id, e);
            throw e;
        }

    }

    @Override
    public Unidade buscarPorId(Long id) {
        log.debug("Buscando unidade por ID: {}", id);
        Unidade unidade = unidadeRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Unidade não encontrada: id={}", id);
                    return new ResourceNotFoundException("Unidade", id);
                });

        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(unidade.getEmpresaId());
        }

        return unidade;
    }

    @Override
    public PagedResult<Unidade> buscarPorFiltros(UnidadeFilter filter, Pagination pagination) {
        log.debug("Buscando unidades com filtros: page={}, size={}",
                pagination.page(), pagination.size());

        PagedResult<Unidade> result = unidadeRepository.buscarPorFiltros(filter, pagination);

        log.debug("Encontrados {} unidades", result.totalElements());
        return result;
    }

    @Override
    public Unidade ativar(Long id) {
        log.info("Ativando unidade: id={}", id);

        Unidade unidade = buscarPorId(id);

        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(unidade.getEmpresaId());
        }
        Unidade unidadeAtivado = unidade.ativar();
        Unidade resultado = unidadeRepository.salvar(unidadeAtivado);

        log.info("Unidade ativada com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    @Override
    public Unidade inativar(Long id) {
        log.info("Inativando unidade: id={}", id);

        Unidade unidade = buscarPorId(id);

        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(unidade.getEmpresaId());
        }
        Unidade unidadeInativado = unidade.inativar();
        Unidade resultado = unidadeRepository.salvar(unidadeInativado);

        log.info("Unidade inativada com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    @Override
    public Unidade excluir(Long id) {
        log.info("Excluindo unidade (soft delete): id={}", id);

        Unidade unidade = buscarPorId(id);
        
        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(unidade.getEmpresaId());
        }
        Unidade unidadeExcluido = unidade.excluir();
        Unidade resultado = unidadeRepository.salvar(unidadeExcluido);

        log.info("Unidade excluída com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    private void validarUnicidadeCriacao(Unidade unidade) {
        if (unidadeRepository.existePorCodigoEEmpresa(unidade.getCodigo(), unidade.getEmpresaId())) {
            throw new DuplicateResourceException("Código", unidade.getCodigo());
        }
    }

    private void validarUnicidadeParaAtualizacao(Unidade unidade, Long id) {
        if (unidadeRepository.existePorCodigoEEmpresaExcluindoId(unidade.getCodigo(), unidade.getEmpresaId(), id)) {
            throw new DuplicateResourceException("Código", unidade.getCodigo());
        }
    }

    private List<String> detectarCamposAlterados(Unidade original, Unidade atualizado) {
        List<String> camposAlterados = new ArrayList<>();

        ServiceUtils.compararEAdicionar(camposAlterados, "codigo", original.getCodigo(), atualizado.getCodigo());
        ServiceUtils.compararEAdicionar(camposAlterados, "nome", original.getNome(), atualizado.getNome());
        ServiceUtils.compararEAdicionar(camposAlterados, "empresaId", original.getEmpresaId(), atualizado.getEmpresaId());

        return camposAlterados.isEmpty() ? List.of("nenhum") : camposAlterados;
    }

    private void validarEmpresaExiste(Long empresaId) {
        empresaRepository.buscarPorId(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", empresaId));
    }
}
