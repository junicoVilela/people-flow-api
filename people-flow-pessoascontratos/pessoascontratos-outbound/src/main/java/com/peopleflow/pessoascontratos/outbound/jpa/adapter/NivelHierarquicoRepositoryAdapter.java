package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.pessoascontratos.core.domain.NivelHierarquico;
import com.peopleflow.pessoascontratos.core.ports.output.NivelHierarquicoRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.NivelHierarquicoEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.NivelHierarquicoJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.CargoJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.NivelHierarquicoJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class NivelHierarquicoRepositoryAdapter implements NivelHierarquicoRepositoryPort {

    private final NivelHierarquicoJpaRepository jpaRepository;
    private final CargoJpaRepository cargoJpaRepository;
    private final NivelHierarquicoJpaMapper mapper;

    public NivelHierarquicoRepositoryAdapter(
            NivelHierarquicoJpaRepository jpaRepository,
            CargoJpaRepository cargoJpaRepository,
            NivelHierarquicoJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.cargoJpaRepository = cargoJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public NivelHierarquico salvar(NivelHierarquico nivelHierarquico) {
        NivelHierarquicoEntity entity;
        if (nivelHierarquico.getId() == null) {
            entity = mapper.toEntity(nivelHierarquico);
        } else {
            entity = jpaRepository.findByIdAndExcluidoEmIsNull(nivelHierarquico.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("NivelHierarquico", nivelHierarquico.getId()));
            entity.setNome(nivelHierarquico.getNome());
            entity.setDescricao(nivelHierarquico.getDescricao());
            entity.setOrdem(nivelHierarquico.getOrdem());
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<NivelHierarquico> buscarAtivoPorId(Long id) {
        return jpaRepository.findByIdAndExcluidoEmIsNull(id).map(mapper::toDomain);
    }

    @Override
    public List<NivelHierarquico> listarAtivosOrdemCrescente() {
        return jpaRepository.findAllByExcluidoEmIsNullOrderByOrdemAsc().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existeNomeIgnorandoMaiusculas(String nome, Long excluirId) {
        if (excluirId == null) {
            return jpaRepository.existsByNomeIgnoreCaseAndExcluidoEmIsNull(nome);
        }
        return jpaRepository.existsByNomeIgnoreCaseAndExcluidoEmIsNullAndIdNot(nome, excluirId);
    }

    @Override
    public long contarCargosAtivosVinculados(Long nivelHierarquicoId) {
        return cargoJpaRepository.countAtivosPorNivelHierarquicoId(nivelHierarquicoId);
    }

    @Override
    public void excluir(Long id) {
        NivelHierarquicoEntity entity = jpaRepository.findByIdAndExcluidoEmIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("NivelHierarquico", id));
        entity.setExcluidoEm(Instant.now());
        jpaRepository.save(entity);
    }
}
