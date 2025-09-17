package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.ColaboradorJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.ColaboradorJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ColaboradorRepositoryAdapter implements ColaboradorRepositoryPort {

    private final ColaboradorJpaRepository repository;
    private final ColaboradorJpaMapper mapper;

    public ColaboradorRepositoryAdapter(ColaboradorJpaRepository repository, ColaboradorJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Colaborador salvar(Colaborador colaborador) {
        ColaboradorEntity entity = mapper.toEntity(colaborador);
        ColaboradorEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Colaborador> buscarPorId(UUID id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<Colaborador> listarTodos() {
        return repository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deletar(UUID id) {
        repository.deleteById(id);
    }
} 