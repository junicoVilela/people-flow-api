package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.DuplicateResourceException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.domain.events.*;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.SecurityContext;
import com.peopleflow.pessoascontratos.core.ports.output.ColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.DomainEventPublisher;
import com.peopleflow.common.query.PagedResult;
import com.peopleflow.common.query.Pagination;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Serviço de casos de uso para Colaborador
 * 
 * Implementa as regras de negócio do domínio Colaborador.
 * Este serviço é puro e não depende de frameworks específicos.
 */
public class ColaboradorService implements ColaboradorUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(ColaboradorService.class);

    private final ColaboradorRepositoryPort colaboradorRepository;
    private final DomainEventPublisher eventPublisher;
    private final SecurityContext securityContext;

    public ColaboradorService(
            ColaboradorRepositoryPort colaboradorRepository,
            DomainEventPublisher eventPublisher,
            SecurityContext securityContext) {
        this.colaboradorRepository = colaboradorRepository;
        this.eventPublisher = eventPublisher;
        this.securityContext = securityContext;
    }

    @Override
    public Colaborador criar(Colaborador colaborador) {
        log.info("Iniciando criação de colaborador: nome={}, clienteId={}, empresaId={}", 
                 colaborador.getNome(), colaborador.getClienteId(), colaborador.getEmpresaId());
        
        try {
            validarPermissaoDeAcesso(colaborador.getClienteId(), colaborador.getEmpresaId());
            
            validarUnicidadeParaCriacao(colaborador);
            
            Colaborador colaboradorCriado = colaboradorRepository.salvar(colaborador);

            eventPublisher.publish(
                new ColaboradorCriado(
                    colaboradorCriado.getId(),
                    colaboradorCriado.getNome(),
                    colaboradorCriado.getCpf().getValor(),
                    colaboradorCriado.getEmail().getValor()
                )
            );
            
            log.info("Colaborador criado com sucesso: id={}, nome={}, cpf={}", 
                     colaboradorCriado.getId(), 
                     colaboradorCriado.getNome(), 
                     colaboradorCriado.getCpf().getValor());
            
            return colaboradorCriado;
        } catch (BusinessException e) {
            log.warn("Erro ao criar colaborador: {} - {}", e.getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar colaborador: nome={}", colaborador.getNome(), e);
            throw e;
        }
    }

    @Override
    public Colaborador buscarPorId(Long id) {
        log.debug("Buscando colaborador por ID: {}", id);
        Colaborador colaborador = colaboradorRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Colaborador não encontrado: id={}", id);
                    return new ResourceNotFoundException("Colaborador", id);
                });

        validarPermissaoDeAcesso(colaborador.getClienteId(), colaborador.getEmpresaId());
        
        return colaborador;
    }

    @Override
    public PagedResult<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pagination pagination) {
        log.debug("Buscando colaboradores com filtros: page={}, size={}", 
                  pagination.page(), pagination.size());

        ColaboradorFilter filtroComSeguranca = aplicarFiltrosDeSeguranca(filter);
        
        PagedResult<Colaborador> result = colaboradorRepository.buscarPorFiltros(filtroComSeguranca, pagination);
        
        log.debug("Encontrados {} colaboradores", result.totalElements());
        return result;
    }

    @Override
    public Colaborador atualizar(Long id, Colaborador colaborador) {
        log.info("Iniciando atualização de colaborador: id={}", id);

        try {
            if (colaborador.getId() != null && !colaborador.getId().equals(id)) {
                throw new BusinessException("ID_MISMATCH",
                        String.format("ID do path (%d) diferente do ID do objeto (%d)",
                                id, colaborador.getId()));
            }

            validarPermissaoDeAcesso(colaborador.getClienteId(), colaborador.getEmpresaId());

            Colaborador original = buscarPorId(id);

            Colaborador colaboradorParaAtualizar = colaborador.toBuilder()
                    .id(id)
                    .build();

            validarUnicidadeParaAtualizacao(colaboradorParaAtualizar, id);

            Colaborador colaboradorAtualizado = colaboradorRepository.salvar(colaboradorParaAtualizar);

            List<String> camposAlterados = detectarCamposAlterados(original, colaboradorAtualizado);

            if (!camposAlterados.equals(List.of("nenhum"))) {
                eventPublisher.publish(
                        new ColaboradorAtualizado(
                                colaboradorAtualizado.getId(),
                                colaboradorAtualizado.getNome(),
                                String.join(", ", camposAlterados)
                        )
                );
            }

            log.info("Colaborador atualizado com sucesso: id={}, nome={}, camposAlterados={}",
                    id, colaborador.getNome(), camposAlterados);

            return colaboradorAtualizado;
        } catch (BusinessException e) {
            log.warn("Erro ao atualizar colaborador: id={}, erro={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar colaborador: id={}", id, e);
            throw e;
        }
    }

    private List<String> detectarCamposAlterados(Colaborador original, Colaborador atualizado) {
        List<String> camposAlterados = new ArrayList<>();

        compararEAdicionar(camposAlterados, "clienteId", original.getClienteId(), atualizado.getClienteId());
        compararEAdicionar(camposAlterados, "empresaId", original.getEmpresaId(), atualizado.getEmpresaId());
        compararEAdicionar(camposAlterados, "departamentoId", original.getDepartamentoId(), atualizado.getDepartamentoId());
        compararEAdicionar(camposAlterados, "centroCustoId", original.getCentroCustoId(), atualizado.getCentroCustoId());
        compararEAdicionar(camposAlterados, "nome", original.getNome(), atualizado.getNome());
        compararEAdicionar(camposAlterados, "cpf", original.getCpf(), atualizado.getCpf());
        compararEAdicionar(camposAlterados, "matricula", original.getMatricula(), atualizado.getMatricula());
        compararEAdicionar(camposAlterados, "email", original.getEmail(), atualizado.getEmail());
        compararEAdicionar(camposAlterados, "dataAdmissao", original.getDataAdmissao(), atualizado.getDataAdmissao());
        compararEAdicionar(camposAlterados, "dataDemissao", original.getDataDemissao(), atualizado.getDataDemissao());
        compararEAdicionar(camposAlterados, "status", original.getStatus(), atualizado.getStatus());
        
        return camposAlterados.isEmpty() ? List.of("nenhum") : camposAlterados;
    }

    private void compararEAdicionar(List<String> lista, String nomeCampo, Object valorOriginal, Object valorAtualizado) {
        if (!Objects.equals(valorOriginal, valorAtualizado)) {
            lista.add(nomeCampo);
        }
    }

    @Override
    public void deletar(Long id) {
        log.warn("Deletando colaborador (hard delete): id={}", id);
        
        buscarPorId(id);
        
        colaboradorRepository.deletar(id);
        
        log.info("Colaborador deletado: id={}", id);
    }

    @Override
    public Colaborador demitir(Long id, LocalDate dataDemissao) {
        log.info("Iniciando demissão de colaborador: id={}, dataDemissao={}", id, dataDemissao);
        
        Colaborador colaborador = buscarPorId(id);
        Colaborador colaboradorDemitido = colaborador.demitir(dataDemissao);
        Colaborador resultado = colaboradorRepository.salvar(colaboradorDemitido);
        
        eventPublisher.publish(
            new ColaboradorDemitido(
                resultado.getId(),
                resultado.getNome(),
                dataDemissao
            )
        );
        
        log.info("Colaborador demitido com sucesso: id={}, nome={}", id, resultado.getNome());
        
        return resultado;
    }

    @Override
    public Colaborador ativar(Long id) {
        log.info("Ativando colaborador: id={}", id);
        
        Colaborador colaborador = buscarPorId(id);
        Colaborador colaboradorAtivado = colaborador.ativar();
        Colaborador resultado = colaboradorRepository.salvar(colaboradorAtivado);
        
        // Publica evento de domínio
        eventPublisher.publish(
            new ColaboradorAtivado(
                resultado.getId(),
                resultado.getNome()
            )
        );
        
        log.info("Colaborador ativado com sucesso: id={}, nome={}", id, resultado.getNome());
        
        return resultado;
    }

    @Override
    public Colaborador inativar(Long id) {
        log.info("Inativando colaborador: id={}", id);
        
        Colaborador colaborador = buscarPorId(id);
        Colaborador colaboradorInativado = colaborador.inativar();
        Colaborador resultado = colaboradorRepository.salvar(colaboradorInativado);

        eventPublisher.publish(
            new ColaboradorInativado(
                resultado.getId(),
                resultado.getNome()
            )
        );
        
        log.info("Colaborador inativado com sucesso: id={}, nome={}", id, resultado.getNome());
        
        return resultado;
    }

    @Override
    public Colaborador excluir(Long id) {
        log.info("Excluindo colaborador (soft delete): id={}", id);
        
        Colaborador colaborador = buscarPorId(id);
        Colaborador colaboradorExcluido = colaborador.excluir();
        Colaborador resultado = colaboradorRepository.salvar(colaboradorExcluido);
        
        eventPublisher.publish(
            new ColaboradorExcluido(
                resultado.getId(),
                resultado.getNome()
            )
        );
        
        log.info("Colaborador excluído com sucesso: id={}, nome={}", id, resultado.getNome());
        
        return resultado;
    }

    private void validarUnicidadeParaCriacao(Colaborador colaborador) {
        validarUnicidadeCampo(
            "CPF",
            colaborador.getCpf().getValorNumerico(),
            colaboradorRepository::existePorCpf
        );
        
        validarUnicidadeCampo(
            "Email",
            colaborador.getEmail().getValor(),
            colaboradorRepository::existePorEmail
        );
        
        if (colaborador.getMatricula() != null && !colaborador.getMatricula().trim().isEmpty()) {
            validarUnicidadeCampo(
                "Matrícula",
                colaborador.getMatricula(),
                colaboradorRepository::existePorMatricula
            );
        }
    }

    private void validarUnicidadeParaAtualizacao(Colaborador colaborador, Long id) {
        validarUnicidadeCampoComExclusao(
            "CPF",
            colaborador.getCpf().getValorNumerico(),
            id,
            colaboradorRepository::existePorCpfExcluindoId
        );
        
        validarUnicidadeCampoComExclusao(
            "Email",
            colaborador.getEmail().getValor(),
            id,
            colaboradorRepository::existePorEmailExcluindoId
        );
        
        if (colaborador.getMatricula() != null && !colaborador.getMatricula().trim().isEmpty()) {
            validarUnicidadeCampoComExclusao(
                "Matrícula",
                colaborador.getMatricula(),
                id,
                colaboradorRepository::existePorMatriculaExcluindoId
            );
        }
    }

    private void validarUnicidadeCampo(
            String nomeCampo, 
            String valor, 
            Predicate<String> validador) {
        
        if (validador.test(valor)) {
            throw new DuplicateResourceException(nomeCampo, valor);
        }
    }

    private void validarUnicidadeCampoComExclusao(
            String nomeCampo, 
            String valor, 
            Long idExcluir,
            BiPredicate<String, Long> validador) {
        
        if (validador.test(valor, idExcluir)) {
            throw new DuplicateResourceException(nomeCampo, valor);
        }
    }

    private void validarPermissaoDeAcesso(Long clienteId, Long empresaId) {
        if (securityContext.isGlobalAdmin()) {
            return;
        }
        
        if (clienteId != null && !securityContext.canAccessCliente(clienteId)) {
            log.warn("Acesso negado: usuário {} tentou acessar clienteId={}", 
                     securityContext.getCurrentUsername(), clienteId);
            throw new BusinessException(
                "ACESSO_NEGADO",
                String.format("Você não tem permissão para acessar dados do cliente %d", clienteId)
            );
        }
        
        if (empresaId != null && !securityContext.canAccessEmpresa(empresaId)) {
            log.warn("Acesso negado: usuário {} tentou acessar empresaId={}", 
                     securityContext.getCurrentUsername(), empresaId);
            throw new BusinessException(
                "ACESSO_NEGADO",
                String.format("Você não tem permissão para acessar dados da empresa %d", empresaId)
            );
        }
    }

    private ColaboradorFilter aplicarFiltrosDeSeguranca(ColaboradorFilter filter) {
        if (securityContext.isGlobalAdmin()) {
            return filter;
        }
        
        Set<Long> allowedClienteIds = securityContext.getAllowedClienteIds();
        Set<Long> allowedEmpresaIds = securityContext.getAllowedEmpresaIds();
        
        if (allowedClienteIds.isEmpty() && !securityContext.isGlobalAdmin()) {
            log.warn("Usuário {} não tem clienteIds permitidos no token", 
                     securityContext.getCurrentUsername());
            return ColaboradorFilter.builder()
                .clienteId(-1L) // ID inexistente
                .build();
        }
        
        if (allowedEmpresaIds.isEmpty() && !securityContext.isGlobalAdmin()) {
            log.warn("Usuário {} não tem empresaIds permitidos no token", 
                     securityContext.getCurrentUsername());
            return ColaboradorFilter.builder()
                .empresaId(-1L) // ID inexistente
                .build();
        }
        
        ColaboradorFilter.ColaboradorFilterBuilder builder = filter != null
            ? filter.toBuilder() 
            : ColaboradorFilter.builder();
        
        if (filter != null && filter.getClienteId() != null) {
            if (!allowedClienteIds.contains(filter.getClienteId())) {
                throw new BusinessException(
                    "ACESSO_NEGADO",
                    String.format("Você não tem permissão para filtrar por clienteId=%d", 
                                  filter.getClienteId())
                );
            }
        } else if (!allowedClienteIds.isEmpty()) {
            builder.clienteId(allowedClienteIds.iterator().next());
        }
        
        if (filter != null && filter.getEmpresaId() != null) {
            if (!allowedEmpresaIds.contains(filter.getEmpresaId())) {
                throw new BusinessException(
                    "ACESSO_NEGADO",
                    String.format("Você não tem permissão para filtrar por empresaId=%d", 
                                  filter.getEmpresaId())
                );
            }
        } else if (!allowedEmpresaIds.isEmpty()) {
            builder.empresaId(allowedEmpresaIds.iterator().next());
        }
        
        return builder.build();
    }
}

