package com.peopleflow.organizacao.outbound.jpa.adapter;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Area;
import com.peopleflow.organizacao.core.ports.output.AreaRepositoryPort;
import com.peopleflow.organizacao.core.query.AreaFilter;
import com.peopleflow.organizacao.outbound.jpa.entity.AreaEntity;
import com.peopleflow.organizacao.outbound.jpa.mapper.AreaJpaMapper;
import com.peopleflow.organizacao.outbound.jpa.repository.AreaJpaRepository;
import com.peopleflow.organizacao.outbound.jpa.specification.AreaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AreaRepositoryAdapter implements AreaRepositoryPort {

    private final AreaJpaRepository areaJpaRepository;
    private final AreaJpaMapper mapper;

    public AreaRepositoryAdapter(AreaJpaRepository areaJpaRepository, AreaJpaMapper mapper) {
        this.areaJpaRepository = areaJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Area salvar(Area area) {
        AreaEntity entity = mapper.toEntity(area);
        AreaEntity saved = areaJpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Area> buscarPorId(Long id) {
        return areaJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<Area> buscarPorFiltros(AreaFilter filter, Pagination pagination) {
        Specification<AreaEntity> specification = AreaSpecification.filter(filter);

        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC, pagination.sortBy())
                : Sort.unsorted();

        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);
        Page<AreaEntity> page = areaJpaRepository.findAll(specification, pageRequest);

        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        return areaJpaRepository.existsByCodigoAndStatusNot(codigo, "excluido");
    }

    @Override
    public boolean existePorCodigoExcluindoId(String codigo, Long id) {
        return areaJpaRepository.existsByCodigoAndIdNotAndStatusNot(codigo, id, "excluido");
    }
}
