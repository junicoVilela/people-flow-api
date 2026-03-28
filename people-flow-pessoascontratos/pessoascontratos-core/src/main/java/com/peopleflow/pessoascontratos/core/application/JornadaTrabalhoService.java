package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.pessoascontratos.core.domain.JornadaTrabalho;
import com.peopleflow.pessoascontratos.core.ports.input.JornadaTrabalhoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.JornadaTrabalhoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class JornadaTrabalhoService implements JornadaTrabalhoUseCase {

    private static final Logger log = LoggerFactory.getLogger(JornadaTrabalhoService.class);

    private final JornadaTrabalhoRepositoryPort repository;

    @Override
    public JornadaTrabalho criar(JornadaTrabalho dados) {
        JornadaTrabalho novo = JornadaTrabalho.nova(dados.getDescricao(), dados.getCargaSemanalHoras());
        JornadaTrabalho salvo = repository.salvar(novo);
        log.info("Jornada criada: id={}", salvo.getId());
        return salvo;
    }

    @Override
    public JornadaTrabalho atualizar(Long id, JornadaTrabalho dados) {
        JornadaTrabalho existente = buscarAtivo(id);
        JornadaTrabalho atualizado = existente.atualizar(dados.getDescricao(), dados.getCargaSemanalHoras());
        JornadaTrabalho salvo = repository.salvar(atualizado);
        log.info("Jornada atualizada: id={}", salvo.getId());
        return salvo;
    }

    @Override
    public JornadaTrabalho buscarPorId(Long id) {
        return buscarAtivo(id);
    }

    @Override
    public List<JornadaTrabalho> listarTodos() {
        return repository.listarAtivos();
    }

    @Override
    public void excluir(Long id) {
        buscarAtivo(id);
        if (repository.contarContratosAtivosVinculados(id) > 0) {
            throw new BusinessException(
                    "JORNADA_EM_USO",
                    "Não é possível excluir: existem contratos ativos vinculados a esta jornada.");
        }
        repository.excluir(id);
        log.info("Jornada excluída: id={}", id);
    }

    private JornadaTrabalho buscarAtivo(Long id) {
        return repository.buscarAtivoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("JornadaTrabalho", id));
    }
}
