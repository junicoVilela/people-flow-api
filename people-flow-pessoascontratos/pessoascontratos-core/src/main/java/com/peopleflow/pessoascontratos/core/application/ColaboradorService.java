package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.DuplicateResourceException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.util.ServiceUtils;
import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.domain.events.*;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.ColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.DomainEventPublisher;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ColaboradorService implements ColaboradorUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(ColaboradorService.class);

    private final ColaboradorRepositoryPort colaboradorRepository;
    private final DomainEventPublisher eventPublisher;

    @Override
    public Colaborador criar(Colaborador colaborador) {
        log.info("Iniciando criação de colaborador: nome={}, clienteId={}, empresaId={}", 
                 colaborador.getNome(), colaborador.getClienteId(), colaborador.getEmpresaId());
        
        try {
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
        
        return colaborador;
    }

    @Override
    public PagedResult<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pagination pagination) {
        log.debug("Buscando colaboradores com filtros: page={}, size={}", 
                  pagination.page(), pagination.size());
        
        PagedResult<Colaborador> result = colaboradorRepository.buscarPorFiltros(filter, pagination);
        
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

            Colaborador original = buscarPorId(id);

            Colaborador colaboradorParaAtualizar = original.atualizar(
                    colaborador.getNome(),
                    colaborador.getCpf(),
                    colaborador.getEmail(),
                    colaborador.getMatricula(),
                    colaborador.getDataAdmissao(),
                    colaborador.getClienteId(),
                    colaborador.getEmpresaId(),
                    colaborador.getDepartamentoId(),
                    colaborador.getCentroCustoId()
            ).toBuilder()
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

        ServiceUtils.compararEAdicionar(camposAlterados, "nome", original.getNome(), atualizado.getNome());
        ServiceUtils.compararEAdicionar(camposAlterados, "cpf", original.getCpf(), atualizado.getCpf());
        ServiceUtils.compararEAdicionar(camposAlterados, "matricula", original.getMatricula(), atualizado.getMatricula());
        ServiceUtils.compararEAdicionar(camposAlterados, "email", original.getEmail(), atualizado.getEmail());
        ServiceUtils.compararEAdicionar(camposAlterados, "dataAdmissao", original.getDataAdmissao(), atualizado.getDataAdmissao());
        ServiceUtils.compararEAdicionar(camposAlterados, "clienteId", original.getClienteId(), atualizado.getClienteId());
        ServiceUtils.compararEAdicionar(camposAlterados, "empresaId", original.getEmpresaId(), atualizado.getEmpresaId());
        ServiceUtils.compararEAdicionar(camposAlterados, "departamentoId", original.getDepartamentoId(), atualizado.getDepartamentoId());
        ServiceUtils.compararEAdicionar(camposAlterados, "centroCustoId", original.getCentroCustoId(), atualizado.getCentroCustoId());
        
        return camposAlterados.isEmpty() ? List.of("nenhum") : camposAlterados;
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
        ServiceUtils.validarUnicidadeCampo(
            "CPF",
            colaborador.getCpf().getValorNumerico(),
            colaboradorRepository::existePorCpf
        );
        
        ServiceUtils.validarUnicidadeCampo(
            "Email",
            colaborador.getEmail().getValor(),
            colaboradorRepository::existePorEmail
        );
        
        if (colaborador.getMatricula() != null && !colaborador.getMatricula().trim().isEmpty()) {
            ServiceUtils.validarUnicidadeCampo(
                "Matrícula",
                colaborador.getMatricula(),
                colaboradorRepository::existePorMatricula
            );
        }
    }

    private void validarUnicidadeParaAtualizacao(Colaborador colaborador, Long id) {
        ServiceUtils.validarUnicidadeCampoComExclusao(
            "CPF",
            colaborador.getCpf().getValorNumerico(),
            id,
            colaboradorRepository::existePorCpfExcluindoId
        );
        
        ServiceUtils.validarUnicidadeCampoComExclusao(
            "Email",
            colaborador.getEmail().getValor(),
            id,
            colaboradorRepository::existePorEmailExcluindoId
        );
        
        if (colaborador.getMatricula() != null && !colaborador.getMatricula().trim().isEmpty()) {
            ServiceUtils.validarUnicidadeCampoComExclusao(
                "Matrícula",
                colaborador.getMatricula(),
                id,
                colaboradorRepository::existePorMatriculaExcluindoId
            );
        }
    }
}

