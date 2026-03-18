package com.peopleflow.organizacao.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.DuplicateResourceException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.common.util.ServiceUtils;
import com.peopleflow.organizacao.core.domain.Area;
import com.peopleflow.organizacao.core.ports.input.AreaUseCase;
import com.peopleflow.organizacao.core.ports.output.AreaRepositoryPort;
import com.peopleflow.organizacao.core.query.AreaFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AreaService implements AreaUseCase {

    private static final Logger log = LoggerFactory.getLogger(AreaService.class);

    private final AreaRepositoryPort areaRepository;

    @Override
    public Area criar(Area area) {
        log.info("Criando área: codigo={}, nome={}", area.getCodigo(), area.getNome());

        validarUnicidadeCriacao(area);

        Area areaSalvar = Area.nova(area.getCodigo(), area.getNome(), area.getDescricao());
        Area areaCriada = areaRepository.salvar(areaSalvar);

        log.info("Área criada com sucesso: id={}, codigo={}", areaCriada.getId(), areaCriada.getCodigo());
        return areaCriada;
    }

    @Override
    public Area atualizar(Long id, Area area) {
        log.info("Atualizando área: id={}", id);

        try {
            if (area.getId() != null && !area.getId().equals(id)) {
                throw new BusinessException("ID_MISMATCH",
                        String.format("ID do path (%d) diferente do ID do objeto (%d)", id, area.getId()));
            }

            Area original = buscarPorId(id);
            validarUnicidadeParaAtualizacao(area, id);

            Area areaAtualizar = original.atualizar(area.getCodigo(), area.getNome(), area.getDescricao())
                    .toBuilder().id(id).build();

            Area areaAtualizada = areaRepository.salvar(areaAtualizar);

            List<String> camposAlterados = detectarCamposAlterados(original, areaAtualizada);
            log.info("Área atualizada com sucesso: id={}, camposAlterados={}", id, camposAlterados);

            return areaAtualizada;
        } catch (BusinessException e) {
            log.warn("Erro ao atualizar área: id={}, erro={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar área: id={}", id, e);
            throw e;
        }
    }

    @Override
    public Area buscarPorId(Long id) {
        log.debug("Buscando área por ID: {}", id);
        return areaRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Área não encontrada: id={}", id);
                    return new ResourceNotFoundException("Area", id);
                });
    }

    @Override
    public PagedResult<Area> buscarPorFiltros(AreaFilter filter, Pagination pagination) {
        log.debug("Buscando áreas com filtros: page={}, size={}", pagination.page(), pagination.size());
        PagedResult<Area> result = areaRepository.buscarPorFiltros(filter, pagination);
        log.debug("Encontradas {} áreas", result.totalElements());
        return result;
    }

    @Override
    public Area ativar(Long id) {
        log.info("Ativando área: id={}", id);
        Area area = buscarPorId(id);
        Area resultado = areaRepository.salvar(area.ativar());
        log.info("Área ativada com sucesso: id={}", id);
        return resultado;
    }

    @Override
    public Area inativar(Long id) {
        log.info("Inativando área: id={}", id);
        Area area = buscarPorId(id);
        Area resultado = areaRepository.salvar(area.inativar());
        log.info("Área inativada com sucesso: id={}", id);
        return resultado;
    }

    @Override
    public Area excluir(Long id) {
        log.info("Excluindo área (soft delete): id={}", id);
        Area area = buscarPorId(id);
        Area resultado = areaRepository.salvar(area.excluir());
        log.info("Área excluída com sucesso: id={}", id);
        return resultado;
    }

    private void validarUnicidadeCriacao(Area area) {
        if (areaRepository.existePorCodigo(area.getCodigo())) {
            throw new DuplicateResourceException("Código", area.getCodigo());
        }
    }

    private void validarUnicidadeParaAtualizacao(Area area, Long id) {
        if (areaRepository.existePorCodigoExcluindoId(area.getCodigo(), id)) {
            throw new DuplicateResourceException("Código", area.getCodigo());
        }
    }

    private List<String> detectarCamposAlterados(Area original, Area atualizada) {
        List<String> camposAlterados = new ArrayList<>();
        ServiceUtils.compararEAdicionar(camposAlterados, "codigo", original.getCodigo(), atualizada.getCodigo());
        ServiceUtils.compararEAdicionar(camposAlterados, "nome", original.getNome(), atualizada.getNome());
        ServiceUtils.compararEAdicionar(camposAlterados, "descricao", original.getDescricao(), atualizada.getDescricao());
        return camposAlterados.isEmpty() ? List.of("nenhum") : camposAlterados;
    }
}
