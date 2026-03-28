package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Dependente;
import com.peopleflow.pessoascontratos.core.ports.output.DependenteRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.DependenteEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.DependenteJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.DependenteJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class DependenteRepositoryAdapter implements DependenteRepositoryPort {

    private final DependenteJpaRepository repository;
    private final DependenteJpaMapper mapper;

    public DependenteRepositoryAdapter(DependenteJpaRepository repository, DependenteJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Dependente salvar(Dependente dependente) {
        if (dependente.getId() == null) {
            return mapper.toDomain(repository.save(mapper.toEntity(dependente)));
        }
        DependenteEntity entity = repository.findByIdAndExcluidoEmIsNull(dependente.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Dependente", dependente.getId()));
        entity.setNome(dependente.getNome());
        entity.setParentesco(dependente.getParentesco().getValor());
        entity.setDataNascimento(dependente.getDataNascimento());
        entity.setCpf(dependente.getCpf());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Dependente> buscarAtivoPorId(Long id) {
        return repository.findByIdAndExcluidoEmIsNull(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<Dependente> listarPorColaboradorId(Long colaboradorId, Pagination pagination) {
        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC, pagination.sortBy())
                : Sort.by(Sort.Direction.ASC, "nome");
        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);
        Page<DependenteEntity> page = repository.findAllByColaboradorIdAndExcluidoEmIsNull(colaboradorId, pageRequest);
        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public void excluir(Long id) {
        DependenteEntity entity = repository.findByIdAndExcluidoEmIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dependente", id));
        entity.setExcluidoEm(Instant.now());
        repository.save(entity);
    }
}
