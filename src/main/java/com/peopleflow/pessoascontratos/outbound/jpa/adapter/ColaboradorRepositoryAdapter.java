package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.model.ColaboradorFilter;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.ColaboradorJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.ColaboradorJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.specification.ColaboradorSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
        Specification<ColaboradorEntity> specification = ColaboradorSpecification.comFiltros(filter);
        return repository.findAll(specification, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void deletar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existePorCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    @Override
    public boolean existePorEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public boolean existePorMatricula(String matricula) {
        if (matricula == null || matricula.trim().isEmpty()) {
            return false;
        }
        return repository.existsByMatricula(matricula);
    }

    @Override
    public boolean existePorCpfExcluindoId(String cpf, Long id) {
        return repository.existsByCpfAndIdNot(cpf, id);
    }

    @Override
    public boolean existePorEmailExcluindoId(String email, Long id) {
        return repository.existsByEmailAndIdNot(email, id);
    }

    @Override
    public boolean existePorMatriculaExcluindoId(String matricula, Long id) {
        if (matricula == null || matricula.trim().isEmpty()) {
            return false;
        }
        return repository.existsByMatriculaAndIdNot(matricula, id);
    }
} 