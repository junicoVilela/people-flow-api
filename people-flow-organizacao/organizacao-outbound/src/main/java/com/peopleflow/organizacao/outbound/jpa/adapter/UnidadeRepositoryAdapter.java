package com.peopleflow.organizacao.outbound.jpa.adapter;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.ports.output.UnidadeRepositoryPort;
import com.peopleflow.organizacao.core.query.UnidadeFilter;
import com.peopleflow.organizacao.outbound.jpa.entity.UnidadeEntity;
import com.peopleflow.organizacao.outbound.jpa.mapper.UnidadeJpaMapper;
import com.peopleflow.organizacao.outbound.jpa.repository.UnidadeJpaRepository;
import com.peopleflow.organizacao.outbound.jpa.specification.UnidadeSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UnidadeRepositoryAdapter implements UnidadeRepositoryPort {

    private final UnidadeJpaRepository unidadeJpaRepository;
    private final UnidadeJpaMapper mapper;

    public UnidadeRepositoryAdapter(UnidadeJpaRepository unidadeJpaRepository, UnidadeJpaMapper mapper) {
        this.unidadeJpaRepository = unidadeJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Unidade salvar(Unidade unidade) {
        UnidadeEntity entity = mapper.toEntity(unidade);
        UnidadeEntity savedEntity = unidadeJpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Unidade> buscarPorId(Long id) {
        return unidadeJpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<Unidade> buscarPorFiltros(UnidadeFilter filter, Pagination pagination) {
        Specification<UnidadeEntity> specification = UnidadeSpecification.filter(filter);

        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                ? Sort.Direction.ASC
                : Sort.Direction.DESC, pagination.sortBy())
                : Sort.unsorted();

        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);

        Page<UnidadeEntity> page = unidadeJpaRepository.findAll(specification, pageRequest);

        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public boolean existePorCodigoEEmpresa(String codigo, Long empresaId) {
        return unidadeJpaRepository.existsByCodigoAndEmpresaIdAndStatusNot(codigo, empresaId, "excluido");
    }

    @Override
    public boolean existePorCodigoEEmpresaExcluindoId(String codigo, Long empresaId, Long id) {
        return unidadeJpaRepository.existsByCodigoAndEmpresaIdAndIdNotAndStatusNot(codigo, empresaId, id, "excluido");
    }

    @Override
    public void excluirTodosPorEmpresaId(Long empresaId) {
        unidadeJpaRepository.excluirTodosPorEmpresaId(empresaId);
    }
}
