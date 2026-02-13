package com.peopleflow.organizacao.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.common.util.ServiceUtils;
import com.peopleflow.common.validation.AccessValidatorPort;
import com.peopleflow.organizacao.core.domain.CentroCusto;
import com.peopleflow.organizacao.core.ports.input.CentroCustoUseCase;
import com.peopleflow.organizacao.core.ports.output.CentroCustoRepositoryPort;
import com.peopleflow.organizacao.core.query.CentroCustoFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CentroCustoService implements CentroCustoUseCase {

    private static final Logger log = LoggerFactory.getLogger(CentroCustoService.class);

    private final CentroCustoRepositoryPort centroCustoRepository;
    private final AccessValidatorPort accessValidator;

    @Override
    public CentroCusto criar(CentroCusto centroCusto) {
        log.info("Iniciando criação de Centro Custo: nome={}, codigo={}, empresaId={}, status={}",
                centroCusto.getNome(),
                centroCusto.getCodigo(),
                centroCusto.getEmpresaId(),
                centroCusto.getStatus());

        validarUnicidadeCriacao(centroCusto);

        CentroCusto centroCustoSalvar = CentroCusto.nova(
                centroCusto.getNome(),
                centroCusto.getCodigo(),
                centroCusto.getEmpresaId(),
                centroCusto.getStatus());

        CentroCusto centroCustoCriar = centroCustoRepository.salvar(centroCustoSalvar);

        log.info("Centro Custo criado com sucesso: nome={}, codigo={}, empresaId={}, status={}, id={}",
                centroCustoCriar.getNome(),
                centroCustoCriar.getCodigo(),
                centroCustoCriar.getEmpresaId(),
                centroCustoCriar.getStatus(),
                centroCustoCriar.getId());

        return centroCustoCriar;
    }

    @Override
    public CentroCusto atualizar(Long id, CentroCusto centroCusto) {
        log.info("Iniciando atualização de Centro Custo: id={}", id);

        try {
            if (centroCusto.getId() != null && !centroCusto.getId().equals(id)) {
                throw new BusinessException("ID_MISMATCH",
                        String.format("ID do path (%d) diferente do ID do objeto (%d)",
                                id, centroCusto.getId()));
            }

            CentroCusto original = buscarPorId(id);
            
            if (!accessValidator.isAdmin()) {
                accessValidator.validarAcessoEmpresa(original.getEmpresaId());
            }

            CentroCusto centroCustoAtualizar = original.atualizar(
                    centroCusto.getNome(),
                    centroCusto.getCodigo(),
                    centroCusto.getEmpresaId(),
                    centroCusto.getStatus()
                    ).toBuilder()
                    .id(id)
                    .build();

            validarUnicidadeParaAtualizacao(centroCustoAtualizar, id);

            CentroCusto centroCustoAtualizado = centroCustoRepository.salvar(centroCustoAtualizar);

            List<String> camposAlterados = detectarCamposAlterados(original, centroCustoAtualizado);

            log.info("Centro Custo atualizado com sucesso: id={}, codigo={},nome={}, empresaId={}, status={} camposAlterados={}",
                    id, centroCusto.getCodigo(), centroCusto.getNome(), centroCusto.getStatus(), camposAlterados);

            return centroCustoAtualizado;
        } catch (BusinessException e) {
            log.warn("Erro ao atualizar Centro Custo: id={}, erro={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar Centro Custo: id={}", id, e);
            throw e;
        }

    }

    @Override
    public CentroCusto buscarPorId(Long id) {
        log.debug("Buscando Centro Custo por ID: {}", id);
        CentroCusto centroCusto = centroCustoRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Centro Custo não encontrado: id={}", id);
                    return new ResourceNotFoundException("Centro Custo", id);
                });

        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(centroCusto.getEmpresaId());
        }

        return centroCusto;
    }

    @Override
    public PagedResult<CentroCusto> buscarPorFiltros(CentroCustoFilter filter, Pagination pagination) {
        log.debug("Buscando Centro Custo com filtros: page={}, size={}",
                pagination.page(), pagination.size());

        PagedResult<CentroCusto> result = centroCustoRepository.buscarPorFiltros(filter, pagination);

        log.debug("Encontrados {} Centro Custo", result.totalElements());
        return result;
    }

    @Override
    public CentroCusto ativar(Long id) {
        log.info("Ativando Centro Custo: id={}", id);

        CentroCusto centroCusto = buscarPorId(id);

        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(centroCusto.getEmpresaId());
        }
        CentroCusto centroCustoAtivado = centroCusto.ativar();
        CentroCusto resultado = centroCustoRepository.salvar(centroCustoAtivado);

        log.info("Centro Custo ativado com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    @Override
    public CentroCusto inativar(Long id) {
        log.info("Inativando Centro Custo: id={}", id);

        CentroCusto centroCusto = buscarPorId(id);

        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(centroCusto.getEmpresaId());
        }
        CentroCusto centroCustoInativado = centroCusto.inativar();
        CentroCusto resultado = centroCustoRepository.salvar(centroCustoInativado);

        log.info("Centro Custo inativada com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    @Override
    public CentroCusto excluir(Long id) {
        log.info("Excluindo Centro Custo (soft delete): id={}", id);

        CentroCusto centroCusto = buscarPorId(id);
        
        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(centroCusto.getEmpresaId());
        }
        CentroCusto centroCustoExcluido = centroCusto.excluir();
        CentroCusto resultado = centroCustoRepository.salvar(centroCustoExcluido);

        log.info("Centro Custo excluído com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    private void validarUnicidadeCriacao(CentroCusto centroCusto) {
        ServiceUtils.validarUnicidadeCampo(
                "Código",
                centroCusto.getCodigo(),
                centroCustoRepository::existePorCodigo
        );
    }

    private void validarUnicidadeParaAtualizacao(CentroCusto centroCusto, Long id) {
        ServiceUtils.validarUnicidadeCampoComExclusao(
                "Código",
                centroCusto.getCodigo(),
                id,
                centroCustoRepository::existePorCodigoExcluindoId
        );
    }

    private List<String> detectarCamposAlterados(CentroCusto original, CentroCusto atualizado) {
        List<String> camposAlterados = new ArrayList<>();

        ServiceUtils.compararEAdicionar(camposAlterados, "codigo", original.getCodigo(), atualizado.getCodigo());
        ServiceUtils.compararEAdicionar(camposAlterados, "nome", original.getNome(), atualizado.getNome());
        ServiceUtils.compararEAdicionar(camposAlterados, "empresaId", original.getEmpresaId(), atualizado.getEmpresaId());
        ServiceUtils.compararEAdicionar(camposAlterados, "status", original.getStatus(), atualizado.getStatus());

        return camposAlterados.isEmpty() ? List.of("nenhum") : camposAlterados;
    }
}
