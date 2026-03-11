package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoColaborador;
import com.peopleflow.pessoascontratos.core.ports.output.DocumentoColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.DocumentoColaboradorEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.DocumentoColaboradorJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.DocumentoColaboradorJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class DocumentoColaboradorRepositoryAdapter implements DocumentoColaboradorRepositoryPort {

    private final DocumentoColaboradorJpaRepository repository;
    private final DocumentoColaboradorJpaMapper mapper;

    public DocumentoColaboradorRepositoryAdapter(
            DocumentoColaboradorJpaRepository repository,
            DocumentoColaboradorJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DocumentoColaborador salvar(DocumentoColaborador documento) {
        DocumentoColaboradorEntity entity = mapper.toEntity(documento);
        DocumentoColaboradorEntity salvo = repository.save(entity);
        return mapper.toDomain(salvo);
    }

    @Override
    public Optional<DocumentoColaborador> buscarPorId(Long id) {
        return repository.findByIdAndExcluidoEmIsNull(id)
                .map(mapper::toDomain);
    }

    @Override
    public PagedResult<DocumentoColaborador> buscarPorColaboradorId(Long colaboradorId, Pagination pagination) {
        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC, pagination.sortBy())
                : Sort.by(Sort.Direction.DESC, "criadoEm");

        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);

        Page<DocumentoColaboradorEntity> page =
                repository.findAllByColaboradorIdAndExcluidoEmIsNull(colaboradorId, pageRequest);

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
        DocumentoColaboradorEntity entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentoColaborador", id));
        entity.setExcluidoEm(Instant.now());
        repository.save(entity);
    }
}
