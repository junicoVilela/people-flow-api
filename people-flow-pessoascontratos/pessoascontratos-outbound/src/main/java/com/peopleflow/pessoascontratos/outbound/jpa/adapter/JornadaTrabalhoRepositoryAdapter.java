package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.pessoascontratos.core.domain.JornadaTrabalho;
import com.peopleflow.pessoascontratos.core.ports.output.JornadaTrabalhoRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.JornadaTrabalhoEntity;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.JornadaTrabalhoJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.ContratoJpaRepository;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.JornadaTrabalhoJpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class JornadaTrabalhoRepositoryAdapter implements JornadaTrabalhoRepositoryPort {

    private final JornadaTrabalhoJpaRepository jpaRepository;
    private final ContratoJpaRepository contratoJpaRepository;
    private final JornadaTrabalhoJpaMapper mapper;

    public JornadaTrabalhoRepositoryAdapter(
            JornadaTrabalhoJpaRepository jpaRepository,
            ContratoJpaRepository contratoJpaRepository,
            JornadaTrabalhoJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.contratoJpaRepository = contratoJpaRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean existeAtivaPorId(Long jornadaId) {
        return jpaRepository.existsByIdAndExcluidoEmIsNull(jornadaId);
    }

    @Override
    public JornadaTrabalho salvar(JornadaTrabalho jornada) {
        if (jornada.getId() == null) {
            return mapper.toDomain(jpaRepository.save(mapper.toEntity(jornada)));
        }
        JornadaTrabalhoEntity entity = jpaRepository.findByIdAndExcluidoEmIsNull(jornada.getId())
                .orElseThrow(() -> new ResourceNotFoundException("JornadaTrabalho", jornada.getId()));
        entity.setDescricao(jornada.getDescricao());
        entity.setCargaSemanalHoras(jornada.getCargaSemanalHoras());
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<JornadaTrabalho> buscarAtivoPorId(Long id) {
        return jpaRepository.findByIdAndExcluidoEmIsNull(id).map(mapper::toDomain);
    }

    @Override
    public List<JornadaTrabalho> listarAtivos() {
        return jpaRepository.findAllByExcluidoEmIsNullOrderByDescricaoAsc().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long contarContratosAtivosVinculados(Long jornadaId) {
        return contratoJpaRepository.countAtivosPorJornadaId(jornadaId);
    }

    @Override
    public void excluir(Long id) {
        JornadaTrabalhoEntity entity = jpaRepository.findByIdAndExcluidoEmIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("JornadaTrabalho", id));
        entity.setExcluidoEm(Instant.now());
        jpaRepository.save(entity);
    }
}
