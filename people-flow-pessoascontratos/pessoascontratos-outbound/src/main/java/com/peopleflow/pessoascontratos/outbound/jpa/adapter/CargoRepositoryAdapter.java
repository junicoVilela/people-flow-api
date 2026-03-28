package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Cargo;
import com.peopleflow.pessoascontratos.core.ports.output.CargoRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.CargoEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.CargoJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.CargoJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.ColaboradorJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.FaixaSalarialJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.FamiliaCargoJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.NivelHierarquicoJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;

@Component
public class CargoRepositoryAdapter implements CargoRepositoryPort {

    private final CargoJpaRepository cargoJpaRepository;
    private final NivelHierarquicoJpaRepository nivelJpaRepository;
    private final FamiliaCargoJpaRepository familiaJpaRepository;
    private final ColaboradorJpaRepository colaboradorJpaRepository;
    private final FaixaSalarialJpaRepository faixaJpaRepository;
    private final CargoJpaMapper mapper;

    public CargoRepositoryAdapter(
            CargoJpaRepository cargoJpaRepository,
            NivelHierarquicoJpaRepository nivelJpaRepository,
            FamiliaCargoJpaRepository familiaJpaRepository,
            ColaboradorJpaRepository colaboradorJpaRepository,
            FaixaSalarialJpaRepository faixaJpaRepository,
            CargoJpaMapper mapper) {
        this.cargoJpaRepository = cargoJpaRepository;
        this.nivelJpaRepository = nivelJpaRepository;
        this.familiaJpaRepository = familiaJpaRepository;
        this.colaboradorJpaRepository = colaboradorJpaRepository;
        this.faixaJpaRepository = faixaJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existeAtivoPorId(Long cargoId) {
        return cargoJpaRepository.existsByIdAndExcluidoEmIsNull(cargoId);
    }

    @Override
    public Cargo salvar(Cargo cargo) {
        if (cargo.getId() == null) {
            CargoEntity entity = mapper.toEntity(cargo);
            entity.setNivelHierarquico(nivelJpaRepository.getReferenceById(cargo.getNivelHierarquicoId()));
            entity.setFamiliaCargo(familiaJpaRepository.getReferenceById(cargo.getFamiliaCargoId()));
            return mapper.toDomain(cargoJpaRepository.save(entity));
        }
        CargoEntity entity = cargoJpaRepository.findByIdAndExcluidoEmIsNull(cargo.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cargo", cargo.getId()));
        entity.setCodigo(cargo.getCodigo());
        entity.setNome(cargo.getNome());
        entity.setDescricao(cargo.getDescricao());
        entity.setDepartamentoId(cargo.getDepartamentoId());
        entity.setNivelHierarquico(nivelJpaRepository.getReferenceById(cargo.getNivelHierarquicoId()));
        entity.setFamiliaCargo(familiaJpaRepository.getReferenceById(cargo.getFamiliaCargoId()));
        return mapper.toDomain(cargoJpaRepository.save(entity));
    }

    @Override
    public Optional<Cargo> buscarAtivoPorId(Long id) {
        return cargoJpaRepository.findByIdAndExcluidoEmIsNull(id).map(mapper::toDomain);
    }

    @Override
    public PagedResult<Cargo> listarAtivos(Pagination pagination) {
        Sort sort = pagination.sortBy() != null
                ? Sort.by(pagination.direction() == Pagination.SortDirection.ASC
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC, pagination.sortBy())
                : Sort.by(Sort.Direction.ASC, "nome");
        PageRequest pageRequest = PageRequest.of(pagination.page(), pagination.size(), sort);
        Page<CargoEntity> page = cargoJpaRepository.findAllByExcluidoEmIsNull(pageRequest);
        return new PagedResult<>(
                page.map(mapper::toDomain).getContent(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public boolean existeCodigoIgnorandoMaiusculas(String codigo, Long excluirId) {
        if (excluirId == null) {
            return cargoJpaRepository.existsByCodigoIgnoreCaseAndExcluidoEmIsNull(codigo);
        }
        return cargoJpaRepository.existsByCodigoIgnoreCaseAndExcluidoEmIsNullAndIdNot(codigo, excluirId);
    }

    @Override
    public long contarContratosAtivosPorCargoId(Long cargoId) {
        return cargoJpaRepository.countContratosAtivosPorCargoId(cargoId);
    }

    @Override
    public long contarColaboradoresAtivosPorCargoId(Long cargoId) {
        return colaboradorJpaRepository.countAtivosPorCargoId(cargoId);
    }

    @Override
    public long contarFaixasAtivasPorCargoId(Long cargoId) {
        return faixaJpaRepository.countByCargoIdAndExcluidoEmIsNull(cargoId);
    }

    @Override
    public void excluir(Long id) {
        CargoEntity entity = cargoJpaRepository.findByIdAndExcluidoEmIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo", id));
        entity.setExcluidoEm(Instant.now());
        cargoJpaRepository.save(entity);
    }
}
