package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Contrato;
import com.peopleflow.pessoascontratos.core.query.ContratoFilter;
import com.peopleflow.pessoascontratos.core.ports.output.ContratoRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ContratoEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.ContratoJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.ContratoJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.specification.ContratoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class ContratoRepositoryAdapter implements ContratoRepositoryPort {

    private final ContratoJpaRepository repository;
    private final ContratoJpaMapper mapper;

    public ContratoRepositoryAdapter(ContratoJpaRepository repository, ContratoJpaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Contrato salvar(Contrato contrato) {
        if (contrato.getId() == null) {
            ContratoEntity entity = mapper.toEntity(contrato);
            return mapper.toDomain(repository.save(entity));
        }
        ContratoEntity entity = repository.findByIdAndExcluidoEmIsNull(contrato.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Contrato", contrato.getId()));
        entity.setColaboradorId(contrato.getColaboradorId());
        entity.setJornadaId(contrato.getJornadaId());
        entity.setCargoId(contrato.getCargoId());
        entity.setTipo(contrato.getTipo() != null ? contrato.getTipo().getValor() : null);
        entity.setRegime(contrato.getRegime() != null ? contrato.getRegime().getValor() : null);
        entity.setSalarioBase(contrato.getSalarioBase());
        entity.setInicio(contrato.getInicio());
        entity.setFim(contrato.getFim());
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Contrato> buscarAtivoPorId(Long id) {
        return repository.findByIdAndExcluidoEmIsNull(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<Contrato> buscarPorFiltros(Long colaboradorId, ContratoFilter filtros, Pagination pagination) {
        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC, pagination.sortBy())
                : Sort.by(Sort.Direction.DESC, "inicio");

        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);
        Specification<ContratoEntity> spec = ContratoSpecification.filter(colaboradorId, filtros);
        Page<ContratoEntity> page = repository.findAll(spec, pageRequest);

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
        ContratoEntity entity = repository.findByIdAndExcluidoEmIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato", id));
        entity.setExcluidoEm(Instant.now());
        repository.save(entity);
    }
}
