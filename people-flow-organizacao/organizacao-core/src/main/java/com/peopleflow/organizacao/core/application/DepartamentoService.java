package com.peopleflow.organizacao.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.DuplicateResourceException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.common.util.ServiceUtils;
import com.peopleflow.common.validation.AccessValidatorPort;
import com.peopleflow.organizacao.core.domain.Departamento;
import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.ports.input.DepartamentoUseCase;
import com.peopleflow.organizacao.core.ports.output.DepartamentoRepositoryPort;
import com.peopleflow.organizacao.core.ports.output.EmpresaRepositoryPort;
import com.peopleflow.organizacao.core.ports.output.UnidadeRepositoryPort;
import com.peopleflow.organizacao.core.query.DepartamentoFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class DepartamentoService implements DepartamentoUseCase {

    private static final Logger log = LoggerFactory.getLogger(DepartamentoService.class);

    private final DepartamentoRepositoryPort departamentoRepository;
    private final EmpresaRepositoryPort empresaRepository;
    private final UnidadeRepositoryPort unidadeRepository;
    private final AccessValidatorPort accessValidator;

    @Override
    public Departamento criar(Departamento departamento) {
        log.info("Iniciando criação de Departamento: nome={}, codigo={}, empresaId={}, unidadeId={}, status={}",
                departamento.getNome(),
                departamento.getCodigo(),
                departamento.getEmpresaId(),
                departamento.getUnidadeId(),
                departamento.getStatus());

        validarEmpresaExiste(departamento.getEmpresaId());
        validarUnidadePertenceAEmpresa(departamento.getUnidadeId(), departamento.getEmpresaId());
        validarUnicidadeCriacao(departamento);

        Departamento departamentoSalvar = Departamento.nova(
                departamento.getNome(),
                departamento.getCodigo(),
                departamento.getEmpresaId(),
                departamento.getUnidadeId(),
                departamento.getStatus());

        Departamento departamentoCriar = departamentoRepository.salvar(departamentoSalvar);

        log.info("Departamento criado com sucesso: nome={}, codigo={}, empresaId={}, unidadeId={}, status={}, id={}",
                departamentoCriar.getNome(),
                departamentoCriar.getCodigo(),
                departamentoCriar.getEmpresaId(),
                departamento.getUnidadeId(),
                departamento.getStatus(),
                departamentoCriar.getId());

        return departamentoCriar;
    }

    @Override
    public Departamento atualizar(Long id, Departamento departamento) {
        log.info("Iniciando atualização de departamento: id={}", id);

        try {
            if (departamento.getId() != null && !departamento.getId().equals(id)) {
                throw new BusinessException("ID_MISMATCH",
                        String.format("ID do path (%d) diferente do ID do objeto (%d)",
                                id, departamento.getId()));
            }

            Departamento original = buscarPorId(id);
            
            if (!accessValidator.isAdmin()) {
                accessValidator.validarAcessoEmpresa(original.getEmpresaId());
            }

            validarEmpresaExiste(departamento.getEmpresaId());
            validarUnidadePertenceAEmpresa(departamento.getUnidadeId(), departamento.getEmpresaId());

            Departamento departamentoAtualizar = original.atualizar(
                    departamento.getNome(),
                    departamento.getCodigo(),
                    departamento.getEmpresaId(),
                    departamento.getUnidadeId(),
                    departamento.getStatus()
                    ).toBuilder()
                    .id(id)
                    .build();

            validarUnicidadeParaAtualizacao(departamentoAtualizar, id);

            Departamento departamentoAtualizado = departamentoRepository.salvar(departamentoAtualizar);

            List<String> camposAlterados = detectarCamposAlterados(original, departamentoAtualizado);

            log.info("Departamento atualizado com sucesso: id={}, codigo={},nome={},empresaId={},unidadeId={},camposAlterados={}",
                    id, departamento.getCodigo(), departamento.getNome(), departamento.getEmpresaId(), departamento.getUnidadeId(), camposAlterados);

            return departamentoAtualizado;
        } catch (BusinessException e) {
            log.warn("Erro ao atualizar departamento: id={}, erro={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar departamento: id={}", id, e);
            throw e;
        }

    }

    @Override
    public Departamento buscarPorId(Long id) {
        log.debug("Buscando departamento por ID: {}", id);
        Departamento departamento = departamentoRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Departamento não encontrado: id={}", id);
                    return new ResourceNotFoundException("Departamento", id);
                });

        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(departamento.getEmpresaId());
        }

        return departamento;
    }

    @Override
    public PagedResult<Departamento> buscarPorFiltros(DepartamentoFilter filter, Pagination pagination) {
        log.debug("Buscando departamentos com filtros: page={}, size={}",
                pagination.page(), pagination.size());

        PagedResult<Departamento> result = departamentoRepository.buscarPorFiltros(filter, pagination);

        log.debug("Encontrados {} departamentos", result.totalElements());
        return result;
    }

    @Override
    public Departamento ativar(Long id) {
        log.info("Ativando departamento: id={}", id);

        Departamento departamento = buscarPorId(id);

        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(departamento.getEmpresaId());
        }
        Departamento departamentoAtivado = departamento.ativar();
        Departamento resultado = departamentoRepository.salvar(departamentoAtivado);

        log.info("Departamento ativado com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    @Override
    public Departamento inativar(Long id) {
        log.info("Inativando departamento: id={}", id);

        Departamento departamento = buscarPorId(id);

        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(departamento.getEmpresaId());
        }
        Departamento departamentoInativado = departamento.inativar();
        Departamento resultado = departamentoRepository.salvar(departamentoInativado);

        log.info("Departamento inativada com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    @Override
    public Departamento excluir(Long id) {
        log.info("Excluindo departamento (soft delete): id={}", id);

        Departamento departamento = buscarPorId(id);
        
        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(departamento.getEmpresaId());
        }
        Departamento departamentoExcluido = departamento.excluir();
        Departamento resultado = departamentoRepository.salvar(departamentoExcluido);

        log.info("Departamento excluído com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    private void validarUnicidadeCriacao(Departamento departamento) {
        if (departamentoRepository.existePorCodigoEEmpresa(departamento.getCodigo(), departamento.getEmpresaId())) {
            throw new DuplicateResourceException("Código", departamento.getCodigo());
        }
    }

    private void validarUnicidadeParaAtualizacao(Departamento departamento, Long id) {
        if (departamentoRepository.existePorCodigoEEmpresaExcluindoId(departamento.getCodigo(), departamento.getEmpresaId(), id)) {
            throw new DuplicateResourceException("Código", departamento.getCodigo());
        }
    }

    private List<String> detectarCamposAlterados(Departamento original, Departamento atualizado) {
        List<String> camposAlterados = new ArrayList<>();

        ServiceUtils.compararEAdicionar(camposAlterados, "codigo", original.getCodigo(), atualizado.getCodigo());
        ServiceUtils.compararEAdicionar(camposAlterados, "nome", original.getNome(), atualizado.getNome());
        ServiceUtils.compararEAdicionar(camposAlterados, "empresaId", original.getEmpresaId(), atualizado.getEmpresaId());
        ServiceUtils.compararEAdicionar(camposAlterados, "unidadeId", original.getUnidadeId(), atualizado.getUnidadeId());
        ServiceUtils.compararEAdicionar(camposAlterados, "status", original.getStatus(), atualizado.getStatus());

        return camposAlterados.isEmpty() ? List.of("nenhum") : camposAlterados;
    }

    private void validarEmpresaExiste(Long empresaId) {
        empresaRepository.buscarPorId(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa", empresaId));
    }

    private void validarUnidadePertenceAEmpresa(Long unidadeId, Long empresaId) {
        Unidade unidade = unidadeRepository.buscarPorId(unidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Unidade", unidadeId));

        if (!unidade.getEmpresaId().equals(empresaId)) {
            throw new BusinessException("UNIDADE_NAO_PERTENCE_EMPRESA",
                    String.format("A unidade %d não pertence à empresa %d", unidadeId, empresaId));
        }
    }
}
