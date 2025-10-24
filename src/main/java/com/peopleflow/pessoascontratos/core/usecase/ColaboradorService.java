package com.peopleflow.pessoascontratos.core.usecase;

import com.peopleflow.common.exception.DuplicateResourceException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.pessoascontratos.core.domain.ColaboradorDomainService;
import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.in.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.model.ColaboradorFilter;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.core.valueobject.Cpf;
import com.peopleflow.pessoascontratos.core.valueobject.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ColaboradorService implements ColaboradorUseCase {
    
    private static final Logger log = LoggerFactory.getLogger(ColaboradorService.class);

    private final ColaboradorRepositoryPort colaboradorRepository;
    private final ColaboradorDomainService domainService;

    public ColaboradorService(ColaboradorRepositoryPort colaboradorRepository, 
                             ColaboradorDomainService domainService) {
        this.colaboradorRepository = colaboradorRepository;
        this.domainService = domainService;
    }

    @Override
    public Colaborador criar(Colaborador colaborador) {
        log.info("Iniciando criação de colaborador: nome={}", colaborador.getNome());
        
        try {
            domainService.validarDadosObrigatorios(colaborador);

            validarCpfUnico(colaborador.getCpf(), null);
            validarUnicidadeEmail(colaborador.getEmail());
            validarUnicidadeMatricula(colaborador.getMatricula());
            
            // TODO: Adicionar validações de clienteId, empresaId, departamentoId quando houver esses módulos
            
            Colaborador colaboradorCriado = colaboradorRepository.salvar(colaborador);
            
            log.info("Colaborador criado com sucesso: id={}, nome={}, cpf={}", 
                     colaboradorCriado.getId(), colaboradorCriado.getNome(), 
                     colaboradorCriado.getCpf() != null ? colaboradorCriado.getCpf().getValor() : null);
            
            return colaboradorCriado;
        } catch (com.peopleflow.common.exception.BusinessException e) {
            log.warn("Erro ao criar colaborador: {} - {}", e.getCode(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao criar colaborador: nome={}", colaborador.getNome(), e);
            throw e;
        }
    }
    
    private void validarUnicidadeEmail(Email email) {
        if (email != null) {
            String emailValor = email.getValor();
            if (colaboradorRepository.existePorEmail(emailValor)) {
                throw new DuplicateResourceException("Email", emailValor);
            }
        }
    }
    
    private void validarUnicidadeMatricula(String matricula) {
        if (matricula != null && !matricula.trim().isEmpty()) {
            if (colaboradorRepository.existePorMatricula(matricula)) {
                throw new DuplicateResourceException("Matrícula", matricula);
            }
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
        return colaboradorRepository.listarTodos();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pageable pageable) {
        log.debug("Buscando colaboradores com filtros: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Colaborador> result = colaboradorRepository.buscarPorFiltros(filter, pageable);
        log.debug("Encontrados {} colaboradores", result.getTotalElements());
        return result;
    }

    @Override
    public Colaborador atualizar(Long id, Colaborador colaborador) {
        log.info("Iniciando atualização de colaborador: id={}", id);
        
        try {
            buscarPorId(id);
            
            domainService.validarDadosObrigatorios(colaborador);

            validarCpfUnico(colaborador.getCpf(), id);
            validarUnicidadeEmailParaAtualizacao(colaborador.getEmail(), id);
            validarUnicidadeMatriculaParaAtualizacao(colaborador.getMatricula(), id);

            Colaborador colaboradorAtualizado = colaboradorRepository.salvar(colaborador);
            
            log.info("Colaborador atualizado com sucesso: id={}, nome={}", id, colaborador.getNome());
            
            return colaboradorAtualizado;
        } catch (com.peopleflow.common.exception.BusinessException e) {
            log.warn("Erro ao atualizar colaborador: id={}, erro={}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Erro inesperado ao atualizar colaborador: id={}", id, e);
            throw e;
        }
    }
    
    private void validarUnicidadeEmailParaAtualizacao(Email email, Long id) {
        if (email != null) {
            String emailValor = email.getValor();
            if (colaboradorRepository.existePorEmailExcluindoId(emailValor, id)) {
                throw new DuplicateResourceException("Email", emailValor);
            }
        }
    }
    
    private void validarUnicidadeMatriculaParaAtualizacao(String matricula, Long id) {
        if (matricula != null && !matricula.trim().isEmpty()) {
            if (colaboradorRepository.existePorMatriculaExcluindoId(matricula, id)) {
                throw new DuplicateResourceException("Matrícula", matricula);
            }
        }
    }

    public void validarCpfUnico(Cpf cpf, Long idExcluir) {
        String cpfValor = cpf.getValorNumerico();
        boolean existe = (idExcluir == null)
                ? colaboradorRepository.existePorCpf(cpfValor)
                : colaboradorRepository.existePorCpfExcluindoId(cpfValor, idExcluir);

        if (existe) {
            throw new DuplicateResourceException("CPF", cpf.getValor());
        }
    }

    @Override
    public void deletar(Long id) {
        log.warn("Deletando colaborador: id={}", id);
        colaboradorRepository.deletar(id);
        log.info("Colaborador deletado: id={}", id);
    }

    @Override
    public Colaborador demitir(Long id, LocalDate dataDemissao) {
        Colaborador colaborador = buscarPorId(id);
        
        domainService.validarDemissao(colaborador, dataDemissao);
        
        colaborador.demitir(dataDemissao);
        
        return colaboradorRepository.salvar(colaborador);
    }

    @Override
    public Colaborador ativar(Long id) {
        Colaborador colaborador = buscarPorId(id);
        
        domainService.validarAtivacao(colaborador);
        
        colaborador.ativar();
        
        return colaboradorRepository.salvar(colaborador);
    }

    @Override
    public Colaborador inativar(Long id) {
        Colaborador colaborador = buscarPorId(id);
        colaborador.inativar();
        
        return colaboradorRepository.salvar(colaborador);
    }

    @Override
    public Colaborador excluir(Long id) {
        Colaborador colaborador = buscarPorId(id);
        
        domainService.validarExclusao(colaborador);
        
        colaborador.excluir();
        
        return colaboradorRepository.salvar(colaborador);
    }
}
