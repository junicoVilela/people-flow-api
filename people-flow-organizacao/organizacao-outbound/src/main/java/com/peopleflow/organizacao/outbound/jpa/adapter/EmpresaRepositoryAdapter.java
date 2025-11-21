package com.peopleflow.organizacao.outbound.jpa.adapter;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.ports.output.EmpresaRepositoryPort;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import com.peopleflow.organizacao.outbound.jpa.entity.EmpresaEntity;
import com.peopleflow.organizacao.outbound.jpa.mapper.EmpresaJpaMapper;
import com.peopleflow.organizacao.outbound.jpa.repository.EmpresaJpaRepository;
import com.peopleflow.organizacao.outbound.jpa.specification.EmpresaSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EmpresaRepositoryAdapter implements EmpresaRepositoryPort {

    private final EmpresaJpaRepository empresaJpaRepository;
    private final EmpresaJpaMapper mapper;

    public EmpresaRepositoryAdapter(final EmpresaJpaRepository empresaJpaRepository, final EmpresaJpaMapper mapper) {
        this.empresaJpaRepository = empresaJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Empresa salvar(Empresa colaborador) {
        EmpresaEntity entity = mapper.toEntity(colaborador);
        EmpresaEntity savedEntity = empresaJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Empresa> buscarPorId(Long id) {
        return empresaJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<Empresa> buscarPorFiltros(EmpresaFilter filter, Pagination pagination) {
        Specification<EmpresaEntity> specification = EmpresaSpecification.filter(filter);

        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC, pagination.sortBy())
                : Sort.unsorted();

        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);

        Page<EmpresaEntity> page = empresaJpaRepository.findAll(specification, pageRequest);

        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public boolean existePorCnpj(String cnpj) {
        return empresaJpaRepository.existsByCnpj(cnpj);
    }

    @Override
    public boolean existePorCnpjExcluindoId(String cnpj, Long id) {
        return empresaJpaRepository.existsByCnpjAndIdNot(cnpj, id);
    }
}
