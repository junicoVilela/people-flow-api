package com.peopleflow.organizacao.outbound.jpa.adapter;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Departamento;
import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.ports.output.DepartamentoRepositoryPort;
import com.peopleflow.organizacao.core.ports.output.UnidadeRepositoryPort;
import com.peopleflow.organizacao.core.query.DepartamentoFilter;
import com.peopleflow.organizacao.core.query.UnidadeFilter;
import com.peopleflow.organizacao.outbound.jpa.entity.DepartamentoEntity;
import com.peopleflow.organizacao.outbound.jpa.entity.UnidadeEntity;
import com.peopleflow.organizacao.outbound.jpa.mapper.DepartamentoJpaMapper;
import com.peopleflow.organizacao.outbound.jpa.mapper.UnidadeJpaMapper;
import com.peopleflow.organizacao.outbound.jpa.repository.DepartamentoJpaRepository;
import com.peopleflow.organizacao.outbound.jpa.repository.UnidadeJpaRepository;
import com.peopleflow.organizacao.outbound.jpa.specification.DepartamentoSpecification;
import com.peopleflow.organizacao.outbound.jpa.specification.UnidadeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DepartamentoRepositoryAdapter implements DepartamentoRepositoryPort {

    private final DepartamentoJpaRepository departamentoJpaRepository;
    private final DepartamentoJpaMapper mapper;

    public DepartamentoRepositoryAdapter(DepartamentoJpaRepository departamentoJpaRepository, DepartamentoJpaMapper mapper) {
        this.departamentoJpaRepository = departamentoJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Departamento salvar(Departamento departamento) {
        DepartamentoEntity entity = mapper.toEntity(departamento);
        DepartamentoEntity savedEntity = departamentoJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Departamento> buscarPorId(Long id) {
        return departamentoJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<Departamento> buscarPorFiltros(DepartamentoFilter filter, Pagination pagination) {
        Specification<DepartamentoEntity> specification = DepartamentoSpecification.filter(filter);

        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC, pagination.sortBy())
                : Sort.unsorted();

        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);

        Page<DepartamentoEntity> page = departamentoJpaRepository.findAll(specification, pageRequest);

        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        return departamentoJpaRepository.existsByCodigoAndStatusNot(codigo, "excluido");
    }

    @Override
    public boolean existePorCodigoExcluindoId(String codigo, Long id) {
        return departamentoJpaRepository.existsByCodigoAndIdNotAndStatusNot(codigo, id, "excluido");
    }
}
