package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoContrato;
import com.peopleflow.pessoascontratos.core.query.DocumentoContratoFilter;
import com.peopleflow.pessoascontratos.core.ports.output.DocumentoContratoRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.DocumentoContratoEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.DocumentoContratoJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.DocumentoContratoJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.specification.DocumentoContratoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class DocumentoContratoRepositoryAdapter implements DocumentoContratoRepositoryPort {

    private final DocumentoContratoJpaRepository repository;
    private final DocumentoContratoJpaMapper mapper;

    public DocumentoContratoRepositoryAdapter(
            DocumentoContratoJpaRepository repository,
            DocumentoContratoJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DocumentoContrato salvar(DocumentoContrato documento) {
        DocumentoContratoEntity entity = mapper.toEntity(documento);
        DocumentoContratoEntity salvo = repository.save(entity);
        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<DocumentoContrato> buscarPorId(Long id) {
        return repository.findByIdAndExcluidoEmIsNull(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<DocumentoContrato> buscarPorFiltros(
            Long contratoId, DocumentoContratoFilter filtros, Pagination pagination) {
        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC, pagination.sortBy())
                : Sort.by(Sort.Direction.DESC, "criadoEm");

        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);
        Specification<DocumentoContratoEntity> spec = DocumentoContratoSpecification.filter(contratoId, filtros);
        Page<DocumentoContratoEntity> page = repository.findAll(spec, pageRequest);

        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize()
        );
    }

    @Override
    public void excluir(Long id) {
        DocumentoContratoEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentoContrato", id));
        entity.setExcluidoEm(Instant.now());
        repository.save(entity);
    }

    @Override
    public long contarAtivosPorContratoId(Long contratoId) {
        return repository.countByContratoIdAndExcluidoEmIsNull(contratoId);
    }
}
