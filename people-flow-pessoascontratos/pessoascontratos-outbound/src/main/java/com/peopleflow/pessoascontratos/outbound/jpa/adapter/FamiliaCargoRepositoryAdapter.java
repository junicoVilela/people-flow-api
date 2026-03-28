package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.pessoascontratos.core.domain.FamiliaCargo;
import com.peopleflow.pessoascontratos.core.ports.output.FamiliaCargoRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.FamiliaCargoEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.FamiliaCargoJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.CargoJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.FamiliaCargoJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class FamiliaCargoRepositoryAdapter implements FamiliaCargoRepositoryPort {

    private final FamiliaCargoJpaRepository jpaRepository;
    private final CargoJpaRepository cargoJpaRepository;
    private final FamiliaCargoJpaMapper mapper;

    public FamiliaCargoRepositoryAdapter(
            FamiliaCargoJpaRepository jpaRepository,
            CargoJpaRepository cargoJpaRepository,
            FamiliaCargoJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.cargoJpaRepository = cargoJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public FamiliaCargo salvar(FamiliaCargo familiaCargo) {
        if (familiaCargo.getId() == null) {
            return mapper.toDomain(jpaRepository.save(mapper.toEntity(familiaCargo)));
        }
        FamiliaCargoEntity entity = jpaRepository.findByIdAndExcluidoEmIsNull(familiaCargo.getId())
                .orElseThrow(() -> new ResourceNotFoundException("FamiliaCargo", familiaCargo.getId()));
        entity.setNome(familiaCargo.getNome());
        entity.setDescricao(familiaCargo.getDescricao());
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<FamiliaCargo> buscarAtivoPorId(Long id) {
        return jpaRepository.findByIdAndExcluidoEmIsNull(id).map(mapper::toDomain);
    }

    @Override
    public List<FamiliaCargo> listarAtivosNome() {
        return jpaRepository.findAllByExcluidoEmIsNullOrderByNomeAsc().stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existeNomeIgnorandoMaiusculas(String nome, Long excluirId) {
        if (excluirId == null) {
            return jpaRepository.existsByNomeIgnoreCaseAndExcluidoEmIsNull(nome);
        }
        return jpaRepository.existsByNomeIgnoreCaseAndExcluidoEmIsNullAndIdNot(nome, excluirId);
    }

    @Override
    public boolean existeAtivoPorId(Long id) {
        return jpaRepository.existsByIdAndExcluidoEmIsNull(id);
    }

    @Override
    public long contarCargosAtivosVinculados(Long familiaCargoId) {
        return cargoJpaRepository.countAtivosPorFamiliaCargoId(familiaCargoId);
    }

    @Override
    public void excluir(Long id) {
        FamiliaCargoEntity entity = jpaRepository.findByIdAndExcluidoEmIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("FamiliaCargo", id));
        entity.setExcluidoEm(Instant.now());
        jpaRepository.save(entity);
    }
}
