package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorFilter;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.ColaboradorJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.ColaboradorJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    public Optional<Colaborador> buscarPorId(Long id) {
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
    public Page<Colaborador> buscarPorFiltros(ColaboradorFilter filter, Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }
} 