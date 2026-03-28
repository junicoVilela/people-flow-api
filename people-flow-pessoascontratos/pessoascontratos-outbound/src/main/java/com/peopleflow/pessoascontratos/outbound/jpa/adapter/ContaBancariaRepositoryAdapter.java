package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.ContaBancaria;
import com.peopleflow.pessoascontratos.core.query.ContaBancariaFilter;
import com.peopleflow.pessoascontratos.core.ports.output.ContaBancariaRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ContaBancariaEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.ContaBancariaJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.ContaBancariaJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.specification.ContaBancariaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class ContaBancariaRepositoryAdapter implements ContaBancariaRepositoryPort {

    private final ContaBancariaJpaRepository repository;
    private final ContaBancariaJpaMapper mapper;

    public ContaBancariaRepositoryAdapter(ContaBancariaJpaRepository repository, ContaBancariaJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public ContaBancaria salvar(ContaBancaria conta) {
        if (conta.getId() == null) {
            return mapper.toDomain(repository.save(mapper.toEntity(conta)));
        }
        ContaBancariaEntity entity = repository.findByIdAndExcluidoEmIsNull(conta.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ContaBancaria", conta.getId()));
        entity.setBanco(conta.getBanco());
        entity.setAgencia(conta.getAgencia());
        entity.setConta(conta.getConta());
        entity.setTipo(conta.getTipo() != null ? conta.getTipo().getValor() : null);
        entity.setPix(conta.getPix());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<ContaBancaria> buscarAtivoPorId(Long id) {
        return repository.findByIdAndExcluidoEmIsNull(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<ContaBancaria> buscarPorFiltros(Long colaboradorId, ContaBancariaFilter filtros, Pagination pagination) {
        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC, pagination.sortBy())
                : Sort.by(Sort.Direction.ASC, "banco");
        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);
        Specification<ContaBancariaEntity> spec = ContaBancariaSpecification.filter(colaboradorId, filtros);
        Page<ContaBancariaEntity> page = repository.findAll(spec, pageRequest);
        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public void excluir(Long id) {
        ContaBancariaEntity entity = repository.findByIdAndExcluidoEmIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContaBancaria", id));
        entity.setExcluidoEm(Instant.now());
        repository.save(entity);
    }
}
