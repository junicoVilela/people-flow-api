package com.peopleflow.pessoascontratos.core.usecase;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.pessoascontratos.core.domain.ColaboradorDomainService;
import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.in.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorFiltros;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ColaboradorService implements ColaboradorUseCase {

    private final ColaboradorRepositoryPort colaboradorRepository;
    private final ColaboradorDomainService domainService;

    public ColaboradorService(ColaboradorRepositoryPort colaboradorRepository, 
                             ColaboradorDomainService domainService) {
        this.colaboradorRepository = colaboradorRepository;
        this.domainService = domainService;
    }

    @Override
    public Colaborador criar(Colaborador colaborador) {
        return colaboradorRepository.salvar(colaborador);
    }

    @Override
    public Colaborador buscarPorId(Long id) {
        return colaboradorRepository.buscarPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Colaborador", id));
    }

    @Override
    public Page<Colaborador> listarTodos(Pageable pageable) {
        return colaboradorRepository.listarTodos(pageable);
    }

    @Override
    public Page<Colaborador> buscarPorFiltros(ColaboradorFiltros filtros, Pageable pageable) {
        return colaboradorRepository.buscarPorFiltros(filtros, pageable);
    }

    @Override
    public Colaborador atualizar(Long id, Colaborador colaborador) {
        Colaborador colaboradorExistente = buscarPorId(id);
        colaborador.setId(id);

        return colaboradorRepository.salvar(colaborador);
    }

    @Override
    public void deletar(Long id) {
        colaboradorRepository.deletar(id);
    }

    public Colaborador demitir(Long id, LocalDate dataDemissao) {
        Colaborador colaborador = buscarPorId(id);
        
        domainService.validarDemissao(colaborador, dataDemissao);
        
        colaborador.demitir(dataDemissao);
        
        return colaboradorRepository.salvar(colaborador);
    }

    public Colaborador ativar(Long id) {
        Colaborador colaborador = buscarPorId(id);
        
        domainService.validarAtivacao(colaborador);
        
        colaborador.ativar();
        
        return colaboradorRepository.salvar(colaborador);
    }

    public Colaborador inativar(Long id) {
        Colaborador colaborador = buscarPorId(id);
        colaborador.inativar();
        
        return colaboradorRepository.salvar(colaborador);
    }

    public Colaborador excluir(Long id) {
        Colaborador colaborador = buscarPorId(id);
        
        domainService.validarExclusao(colaborador);
        
        colaborador.excluir();
        
        return colaboradorRepository.salvar(colaborador);
    }
}
