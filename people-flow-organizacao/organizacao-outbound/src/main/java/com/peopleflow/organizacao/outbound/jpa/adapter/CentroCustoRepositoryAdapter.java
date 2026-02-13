package com.peopleflow.organizacao.outbound.jpa.adapter;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.CentroCusto;
import com.peopleflow.organizacao.core.ports.output.CentroCustoRepositoryPort;
import com.peopleflow.organizacao.core.query.CentroCustoFilter;
import com.peopleflow.organizacao.outbound.jpa.entity.CentroCustoEntity;
import com.peopleflow.organizacao.outbound.jpa.mapper.CentroCustoJpaMapper;
import com.peopleflow.organizacao.outbound.jpa.repository.CentroCustoJpaRepository;
import com.peopleflow.organizacao.outbound.jpa.specification.CentroCustoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CentroCustoRepositoryAdapter implements CentroCustoRepositoryPort {

    private final CentroCustoJpaRepository centroCustoJpaRepository;
    private final CentroCustoJpaMapper mapper;

    public CentroCustoRepositoryAdapter(CentroCustoJpaRepository centroCustoJpaRepository, CentroCustoJpaMapper mapper) {
        this.centroCustoJpaRepository = centroCustoJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public CentroCusto salvar(CentroCusto centroCusto) {
        CentroCustoEntity entity = mapper.toEntity(centroCusto);
        CentroCustoEntity savedEntity = centroCustoJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<CentroCusto> buscarPorId(Long id) {
        return centroCustoJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<CentroCusto> buscarPorFiltros(CentroCustoFilter filter, Pagination pagination) {
        Specification<CentroCustoEntity> specification = CentroCustoSpecification.filter(filter);

        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC, pagination.sortBy())
                : Sort.unsorted();

        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);

        Page<CentroCustoEntity> page = centroCustoJpaRepository.findAll(specification, pageRequest);

        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        return centroCustoJpaRepository.existsByCodigoAndStatusNot(codigo, "excluido");
    }

    @Override
    public boolean existePorCodigoExcluindoId(String codigo, Long id) {
        return centroCustoJpaRepository.existsByCodigoAndIdNotAndStatusNot(codigo, id, "excluido");
    }
}
