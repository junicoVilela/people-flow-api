package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.FaixaSalarial;
import com.peopleflow.pessoascontratos.core.ports.output.FaixaSalarialRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.FaixaSalarialEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.FaixaSalarialJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.FaixaSalarialJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class FaixaSalarialRepositoryAdapter implements FaixaSalarialRepositoryPort {

    private final FaixaSalarialJpaRepository repository;
    private final FaixaSalarialJpaMapper mapper;

    public FaixaSalarialRepositoryAdapter(FaixaSalarialJpaRepository repository, FaixaSalarialJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public FaixaSalarial salvar(FaixaSalarial faixa) {
        if (faixa.getId() == null) {
            return mapper.toDomain(repository.save(mapper.toEntity(faixa)));
        }
        FaixaSalarialEntity entity = repository.findByIdAndExcluidoEmIsNull(faixa.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FaixaSalarial", faixa.getId()));
        entity.setFaixaMin(faixa.getFaixaMin());
        entity.setFaixaMax(faixa.getFaixaMax());
        entity.setMoeda(faixa.getMoeda().getValor());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<FaixaSalarial> buscarAtivoPorId(Long id) {
        return repository.findByIdAndExcluidoEmIsNull(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<FaixaSalarial> listarPorCargoId(Long cargoId, Pagination pagination) {
        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC, pagination.sortBy())
                : Sort.by(Sort.Direction.ASC, "faixaMin");
        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);
        Page<FaixaSalarialEntity> page = repository.findAllByCargoIdAndExcluidoEmIsNull(cargoId, pageRequest);
        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public void excluir(Long id) {
        FaixaSalarialEntity entity = repository.findByIdAndExcluidoEmIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("FaixaSalarial", id));
        entity.setExcluidoEm(Instant.now());
        repository.save(entity);
    }
}
