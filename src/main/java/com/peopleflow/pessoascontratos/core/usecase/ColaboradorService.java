package com.peopleflow.pessoascontratos.core.usecase;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.DuplicateResourceException;
import com.peopleflow.common.exception.ResourceNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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

    public ColaboradorService(
            ColaboradorRepositoryPort colaboradorRepository,
            ApplicationEventPublisher eventPublisher) {
        this.colaboradorRepository = colaboradorRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Colaborador criar(Colaborador colaborador) {
        log.info("Iniciando criação de colaborador: nome={}", colaborador.getNome());
        
        try {
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
        return colaboradorRepository.buscarPorId(id)
                .orElseThrow(() -> {
                    log.warn("Colaborador não encontrado: id={}", id);
                    return new ResourceNotFoundException("Colaborador", id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Colaborador> listarTodos() {
        log.debug("Listando todos os colaboradores");
        return colaboradorRepository.listarTodos();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pageable pageable) {
        log.debug("Buscando colaboradores com filtros: page={}, size={}", 
                  pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Colaborador> result = colaboradorRepository.buscarPorFiltros(filter, pageable);
        
        log.debug("Encontrados {} colaboradores", result.getTotalElements());
        return result;
    }

    @Override
    public Colaborador atualizar(Long id, Colaborador colaborador) {
        log.info("Iniciando atualização de colaborador: id={}", id);
        
        try {

            validarUnicidadeParaAtualizacao(colaborador, id);
            Colaborador original = buscarPorId(id);
            Colaborador colaboradorAtualizado = colaboradorRepository.salvar(colaborador);

            // TODO: verificar se está cobrindo todos os campos que precisam ser atualizados
            List<String> camposAlterados = detectarCamposAlterados(original, colaboradorAtualizado);

            eventPublisher.publishEvent(
                new ColaboradorAtualizado(
                    colaboradorAtualizado.getId(),
                    colaboradorAtualizado.getNome(),
                    String.join(",", camposAlterados)
                )
            );
            
            log.info("Colaborador atualizado com sucesso: id={}, nome={}", id, colaborador.getNome());
            
            return colaboradorAtualizado;
        } catch (BusinessException e) {
            log.warn("Erro ao atualizar colaborador: id={}, erro={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar colaborador: id={}", id, e);
            throw e;
        }
    }

    public static List<String> detectarCamposAlterados(Colaborador original, Colaborador atualizado) {
        List<String> camposAlterados = null;

        if (!Objects.equals(original.getNome(), atualizado.getNome())) {
            camposAlterados = List.of("nome");
        }

        if (!Objects.equals(original.getCpf(), atualizado.getCpf())) {
            camposAlterados = List.of("cpf");
        }

        if (!Objects.equals(original.getEmail(), atualizado.getEmail())) {
            camposAlterados = List.of("email");
        }

        return camposAlterados;
    }

    @Override
    public void deletar(Long id) {
        log.warn("Deletando colaborador (hard delete): id={}", id);
        
        // Verifica se existe
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
        
        // Publica evento de domínio
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
        
        // Publica evento de domínio
        eventPublisher.publishEvent(
            new ColaboradorExcluido(
                resultado.getId(),
                resultado.getNome()
            )
        );
        
        log.info("Colaborador excluído com sucesso: id={}, nome={}", id, resultado.getNome());
        
        return resultado;
    }

    // ========== MÉTODOS PRIVADOS DE VALIDAÇÃO (Bem Encapsulados) ==========

    /**
     * Valida unicidade de campos para criação de novo colaborador
     */
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
}
