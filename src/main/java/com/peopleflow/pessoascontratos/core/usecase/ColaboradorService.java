package com.peopleflow.pessoascontratos.core.usecase;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.in.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ColaboradorService implements ColaboradorUseCase {

    private final ColaboradorRepositoryPort colaboradorRepository;

    public ColaboradorService(ColaboradorRepositoryPort colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    @Override
    public Colaborador criar(Colaborador colaborador) {
        return colaboradorRepository.salvar(colaborador);
    }

    @Override
    public Colaborador buscarPorId(UUID id) {
        return colaboradorRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
    }

    @Override
    public List<Colaborador> listarTodos() {
        return colaboradorRepository.listarTodos();
    }

    @Override
    public Colaborador atualizar(UUID id, Colaborador colaborador) {
        Colaborador colaboradorExistente = buscarPorId(id);
        colaborador.setId(id);
        return colaboradorRepository.salvar(colaborador);
    }

    @Override
    public void deletar(UUID id) {
        colaboradorRepository.deletar(id);
    }
} 