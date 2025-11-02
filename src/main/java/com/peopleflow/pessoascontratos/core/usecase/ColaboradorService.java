package com.peopleflow.pessoascontratos.core.usecase;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.DuplicateResourceException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.security.SecurityContextService;
import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.domain.events.*;
import com.peopleflow.pessoascontratos.core.ports.in.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Serviço de aplicação para Colaborador
 * 
 * Responsabilidades:
 * - Orquestrar casos de uso
 * - Validar unicidade (dependência do repositório)
 * - Gerenciar transações
 * - Logging de operações
 * 
 * NÃO é responsabilidade deste serviço:
 * - Validar invariantes do modelo (isso é do Colaborador)
 * - Lógicas de negócio complexas (isso é do DomainService)
 */
@Service
@Transactional
public class ColaboradorService implements ColaboradorUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(ColaboradorService.class);

    private final ColaboradorRepositoryPort colaboradorRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final SecurityContextService securityContextService;

    public ColaboradorService(
            ColaboradorRepositoryPort colaboradorRepository,
            ApplicationEventPublisher eventPublisher,
            SecurityContextService securityContextService) {
        this.colaboradorRepository = colaboradorRepository;
        this.eventPublisher = eventPublisher;
        this.securityContextService = securityContextService;
    }

    @Override
    public Colaborador criar(Colaborador colaborador) {
        log.info("Iniciando criação de colaborador: nome={}, clienteId={}, empresaId={}", 
                 colaborador.getNome(), colaborador.getClienteId(), colaborador.getEmpresaId());
        
        try {
            // Valida permissões de acesso
            validarPermissaoDeAcesso(colaborador.getClienteId(), colaborador.getEmpresaId());
            
            validarUnicidadeParaCriacao(colaborador);
            
            Colaborador colaboradorCriado = colaboradorRepository.salvar(colaborador);

            eventPublisher.publishEvent(
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
    @Transactional(readOnly = true)
    public Colaborador buscarPorId(Long id) {
        log.debug("Buscando colaborador por ID: {}", id);
        Colaborador colaborador = colaboradorRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Colaborador não encontrado: id={}", id);
                    return new ResourceNotFoundException("Colaborador", id);
                });
        
        // Valida permissões de acesso
        validarPermissaoDeAcesso(colaborador.getClienteId(), colaborador.getEmpresaId());
        
        return colaborador;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pageable pageable) {
        log.debug("Buscando colaboradores com filtros: page={}, size={}", 
                  pageable.getPageNumber(), pageable.getPageSize());
        
        // Aplica filtros de segurança automaticamente
        ColaboradorFilter filtroComSeguranca = aplicarFiltrosDeSeguranca(filter);
        
        Page<Colaborador> result = colaboradorRepository.buscarPorFiltros(filtroComSeguranca, pageable);
        
        log.debug("Encontrados {} colaboradores", result.getTotalElements());
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
            
            // Valida permissões ANTES de buscar (para evitar leak de informações)
            validarPermissaoDeAcesso(colaborador.getClienteId(), colaborador.getEmpresaId());

            Colaborador original = buscarPorId(id);

            Colaborador colaboradorParaAtualizar = colaborador.toBuilder()
                    .id(id)  // Garante que o ID é o correto
                    .build();

            validarUnicidadeParaAtualizacao(colaboradorParaAtualizar, id);

            Colaborador colaboradorAtualizado = colaboradorRepository.salvar(colaboradorParaAtualizar);

            List<String> camposAlterados = detectarCamposAlterados(original, colaboradorAtualizado);

            if (!camposAlterados.equals(List.of("nenhum"))) {
                eventPublisher.publishEvent(
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

    /**
     * Detecta quais campos foram alterados entre duas instâncias de Colaborador
     * 
     * @param original Colaborador antes da atualização
     * @param atualizado Colaborador após a atualização
     * @return Lista com nomes dos campos alterados, ou ["nenhum"] se nada mudou
     */
    private List<String> detectarCamposAlterados(Colaborador original, Colaborador atualizado) {
        List<String> camposAlterados = new ArrayList<>();
        
        // Usar método auxiliar para reduzir duplicação de código
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
    
    /**
     * Método auxiliar para comparar valores e adicionar à lista se forem diferentes
     * 
     * @param lista Lista onde adicionar o nome do campo se houver mudança
     * @param nomeCampo Nome do campo para adicionar à lista
     * @param valorOriginal Valor original do campo
     * @param valorAtualizado Valor atualizado do campo
     */
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
        
        eventPublisher.publishEvent(
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
        eventPublisher.publishEvent(
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

        eventPublisher.publishEvent(
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
        
        eventPublisher.publishEvent(
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

    /**
     * Valida unicidade de campos para atualização de colaborador existente
     */
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

    /**
     * Método genérico para validar unicidade de um campo
     * 
     * @param nomeCampo Nome do campo para mensagem de erro
     * @param valor Valor do campo a ser validado
     * @param validador Função que verifica se o valor já existe
     */
    private void validarUnicidadeCampo(
            String nomeCampo, 
            String valor, 
            Predicate<String> validador) {
        
        if (validador.test(valor)) {
            throw new DuplicateResourceException(nomeCampo, valor);
        }
    }

    /**
     * Método genérico para validar unicidade de um campo excluindo um ID
     * (usado em atualizações para permitir manter o mesmo valor)
     * 
     * @param nomeCampo Nome do campo para mensagem de erro
     * @param valor Valor do campo a ser validado
     * @param idExcluir ID do colaborador a ser excluído da verificação
     * @param validador Função que verifica se o valor já existe (excluindo o ID)
     */
    private void validarUnicidadeCampoComExclusao(
            String nomeCampo, 
            String valor, 
            Long idExcluir,
            BiPredicate<String, Long> validador) {
        
        if (validador.test(valor, idExcluir)) {
            throw new DuplicateResourceException(nomeCampo, valor);
        }
    }

    /**
     * Valida se o usuário tem permissão para acessar dados de um cliente/empresa
     * 
     * @param clienteId ID do cliente
     * @param empresaId ID da empresa
     * @throws AccessDeniedException se não tiver permissão
     */
    private void validarPermissaoDeAcesso(Long clienteId, Long empresaId) {
        // Admin global pode acessar tudo
        if (securityContextService.isGlobalAdmin()) {
            return;
        }
        
        // Valida cliente
        if (clienteId != null && !securityContextService.canAccessCliente(clienteId)) {
            log.warn("Acesso negado: usuário {} tentou acessar clienteId={}", 
                     securityContextService.getCurrentUsername(), clienteId);
            throw new AccessDeniedException(
                String.format("Você não tem permissão para acessar dados do cliente %d", clienteId)
            );
        }
        
        // Valida empresa
        if (empresaId != null && !securityContextService.canAccessEmpresa(empresaId)) {
            log.warn("Acesso negado: usuário {} tentou acessar empresaId={}", 
                     securityContextService.getCurrentUsername(), empresaId);
            throw new AccessDeniedException(
                String.format("Você não tem permissão para acessar dados da empresa %d", empresaId)
            );
        }
    }

    /**
     * Aplica filtros de segurança ao filtro de busca
     * Garante que o usuário só veja colaboradores das empresas/clientes que tem acesso
     * 
     * @param filter Filtro original
     * @return Filtro com restrições de segurança aplicadas
     */
    private ColaboradorFilter aplicarFiltrosDeSeguranca(ColaboradorFilter filter) {
        // Admin global não tem restrições
        if (securityContextService.isGlobalAdmin()) {
            return filter;
        }
        
        Set<Long> allowedClienteIds = securityContextService.getAllowedClienteIds();
        Set<Long> allowedEmpresaIds = securityContextService.getAllowedEmpresaIds();
        
        // Se o usuário não tem acesso a nenhum cliente/empresa, retorna filtro vazio
        if (allowedClienteIds.isEmpty() && !securityContextService.isGlobalAdmin()) {
            log.warn("Usuário {} não tem clienteIds permitidos no token", 
                     securityContextService.getCurrentUsername());
            // Cria um filtro com ID impossível para retornar vazio
            return ColaboradorFilter.builder()
                .clienteId(-1L) // ID inexistente
                .build();
        }
        
        if (allowedEmpresaIds.isEmpty() && !securityContextService.isGlobalAdmin()) {
            log.warn("Usuário {} não tem empresaIds permitidos no token", 
                     securityContextService.getCurrentUsername());
            return ColaboradorFilter.builder()
                .empresaId(-1L) // ID inexistente
                .build();
        }
        
        // Aplica restrições ao filtro existente
        ColaboradorFilter.ColaboradorFilterBuilder builder = filter != null 
            ? filter.toBuilder() 
            : ColaboradorFilter.builder();
        
        // Se o filtro já tem clienteId, valida se está permitido
        if (filter != null && filter.getClienteId() != null) {
            if (!allowedClienteIds.contains(filter.getClienteId())) {
                throw new AccessDeniedException(
                    String.format("Você não tem permissão para filtrar por clienteId=%d", 
                                  filter.getClienteId())
                );
            }
        } else if (!allowedClienteIds.isEmpty()) {
            // Se não especificou cliente, usa o primeiro permitido
            // (em filtros mais complexos, deveria buscar de todos os permitidos)
            builder.clienteId(allowedClienteIds.iterator().next());
        }
        
        // Mesma lógica para empresaId
        if (filter != null && filter.getEmpresaId() != null) {
            if (!allowedEmpresaIds.contains(filter.getEmpresaId())) {
                throw new AccessDeniedException(
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
