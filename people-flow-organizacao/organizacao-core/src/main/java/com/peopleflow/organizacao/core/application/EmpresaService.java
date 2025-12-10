package com.peopleflow.organizacao.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.util.ServiceUtils;
import com.peopleflow.common.validation.AccessValidatorPort;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.ports.input.EmpresaUseCase;
import com.peopleflow.organizacao.core.ports.output.EmpresaRepositoryPort;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class EmpresaService implements EmpresaUseCase {

    private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);

    private final EmpresaRepositoryPort empresaRepository;
    private final AccessValidatorPort accessValidator;

    @Override
    public Empresa criar(Empresa empresa) {
        log.info("Iniciando criação de Empresa: nome={}, cnpj={}, inscricaoEstadual={}, status={}",
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getInscricaoEstadual(),
                empresa.getStatus());

        validarUnicidadeCriacao(empresa);

        Empresa empresaCriar = empresaRepository.salvar(empresa);

        log.info("Empresa criada com sucesso: nome={}, cnpj={}, id={}",
                empresa.getNome(),
                empresa.getCnpj(),
                empresaCriar.getId());

        return empresaCriar;
    }

    @Override
    public Empresa atualizar(Long id, Empresa empresa) {
        log.info("Iniciando atualização de empresa: id={}", id);

        try {
            if (empresa.getId() != null && !empresa.getId().equals(id)) {
                throw new BusinessException("ID_MISMATCH",
                        String.format("ID do path (%d) diferente do ID do objeto (%d)",
                                id, empresa.getId()));
            }

            Empresa original = buscarPorId(id);
            
            // Valida acesso à empresa antes de atualizar
            if (!accessValidator.isAdmin()) {
                accessValidator.validarAcessoEmpresa(original.getId());
            }

            Empresa empresaAtualizar = original.atualizar(
                    empresa.getNome(),
                    empresa.getCnpj(),
                    empresa.getInscricaoEstadual()
                    ).toBuilder()
                    .id(id)
                    .build();

            validarUnicidadeParaAtualizacao(empresaAtualizar, id);

            Empresa empresaAtualizado = empresaRepository.salvar(empresaAtualizar);

            List<String> camposAlterados = detectarCamposAlterados(original, empresaAtualizado);

            log.info("Empresa atualizado com sucesso: id={}, nome={}, cnpj={}, camposAlterados={}",
                    id, empresa.getNome(), empresa.getCnpj(), camposAlterados);

            return empresaAtualizado;
        } catch (BusinessException e) {
            log.warn("Erro ao atualizar empresa: id={}, erro={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar empresa: id={}", id, e);
            throw e;
        }

    }

    @Override
    public Empresa buscarPorId(Long id) {
        log.debug("Buscando empresa por ID: {}", id);
        Empresa empresa = empresaRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Empresa não encontrado: id={}", id);
                    return new ResourceNotFoundException("Empresa", id);
                });

        // Valida acesso à empresa
        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(empresa.getId());
        }

        return empresa;
    }

    @Override
    public PagedResult<Empresa> buscarPorFiltros(EmpresaFilter filter, Pagination pagination) {
        log.debug("Buscando empresas com filtros: page={}, size={}",
                pagination.page(), pagination.size());

        PagedResult<Empresa> result = empresaRepository.buscarPorFiltros(filter, pagination);

        log.debug("Encontrados {} empresas", result.totalElements());
        return result;
    }

    @Override
    public Empresa ativar(Long id) {
        log.info("Ativando empresa: id={}", id);

        Empresa empresa = buscarPorId(id);
        
        // Valida acesso à empresa antes de ativar
        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(empresa.getId());
        }
        Empresa empresaAtivado = empresa.ativar();
        Empresa resultado = empresaRepository.salvar(empresaAtivado);

        log.info("Empresa ativado com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    @Override
    public Empresa inativar(Long id) {
        log.info("Inativando empresa: id={}", id);

        Empresa empresa = buscarPorId(id);
        
        // Valida acesso à empresa antes de inativar
        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(empresa.getId());
        }
        Empresa empresaInativado = empresa.inativar();
        Empresa resultado = empresaRepository.salvar(empresaInativado);

        log.info("Empresa inativado com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    @Override
    public Empresa excluir(Long id) {
        log.info("Excluindo empresa (soft delete): id={}", id);

        Empresa empresa = buscarPorId(id);
        
        // Valida acesso à empresa antes de excluir
        if (!accessValidator.isAdmin()) {
            accessValidator.validarAcessoEmpresa(empresa.getId());
        }
        Empresa empresaExcluido = empresa.excluir();
        Empresa resultado = empresaRepository.salvar(empresaExcluido);

        log.info("Empresa excluído com sucesso: id={}, nome={}", id, resultado.getNome());

        return resultado;
    }

    private void validarUnicidadeCriacao(Empresa empresa) {
        ServiceUtils.validarUnicidadeCampo(
                "CNPJ",
                empresa.getCnpj().getValor(),
                empresaRepository::existePorCnpj
        );
    }

    private void validarUnicidadeParaAtualizacao(Empresa empresa, Long id) {
        ServiceUtils.validarUnicidadeCampoComExclusao(
                "CNPJ",
                empresa.getCnpj().getValor(),
                id,
                empresaRepository::existePorCnpjExcluindoId
        );
    }

    private List<String> detectarCamposAlterados(Empresa original, Empresa atualizado) {
        List<String> camposAlterados = new ArrayList<>();

        ServiceUtils.compararEAdicionar(camposAlterados, "nome", original.getNome(), atualizado.getNome());
        ServiceUtils.compararEAdicionar(camposAlterados, "cnpj", original.getCnpj(), atualizado.getCnpj());
        ServiceUtils.compararEAdicionar(camposAlterados, "inscricaoEstadual", original.getInscricaoEstadual(), atualizado.getInscricaoEstadual());

        return camposAlterados.isEmpty() ? List.of("nenhum") : camposAlterados;
    }
}
