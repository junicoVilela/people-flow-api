package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.FaixaSalarial;
import com.peopleflow.pessoascontratos.core.ports.input.FaixaSalarialUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.CargoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.FaixaSalarialRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class FaixaSalarialService implements FaixaSalarialUseCase {

    private static final Logger log = LoggerFactory.getLogger(FaixaSalarialService.class);

    private final FaixaSalarialRepositoryPort faixaRepository;
    private final CargoRepositoryPort cargoRepository;

    @Override
    public FaixaSalarial adicionar(Long cargoId, FaixaSalarial dados) {
        validarCargo(cargoId);
        FaixaSalarial nova = FaixaSalarial.nova(cargoId, dados.getFaixaMin(), dados.getFaixaMax(), dados.getMoeda());
        FaixaSalarial salva = faixaRepository.salvar(nova);
        log.info("Faixa salarial criada: id={}, cargoId={}", salva.getId(), cargoId);
        return salva;
    }

    @Override
    public FaixaSalarial atualizar(Long cargoId, Long faixaId, FaixaSalarial dados) {
        FaixaSalarial existente = buscarDoCargo(cargoId, faixaId);
        FaixaSalarial atualizada = existente.atualizar(dados.getFaixaMin(), dados.getFaixaMax(), dados.getMoeda());
        return faixaRepository.salvar(atualizada);
    }

    @Override
    public FaixaSalarial buscarPorId(Long cargoId, Long faixaId) {
        return buscarDoCargo(cargoId, faixaId);
    }

    @Override
    public PagedResult<FaixaSalarial> listarPorCargo(Long cargoId, Pagination pagination) {
        validarCargo(cargoId);
        return faixaRepository.listarPorCargoId(cargoId, pagination);
    }

    @Override
    public void excluir(Long cargoId, Long faixaId) {
        FaixaSalarial faixa = buscarDoCargo(cargoId, faixaId);
        faixaRepository.excluir(faixa.getId());
        log.info("Faixa salarial excluída: id={}", faixaId);
    }

    private void validarCargo(Long cargoId) {
        if (!cargoRepository.existeAtivoPorId(cargoId)) {
            throw new ResourceNotFoundException("Cargo", cargoId);
        }
    }

    private FaixaSalarial buscarDoCargo(Long cargoId, Long faixaId) {
        validarCargo(cargoId);
        FaixaSalarial faixa = faixaRepository.buscarAtivoPorId(faixaId)
                .orElseThrow(() -> new ResourceNotFoundException("FaixaSalarial", faixaId));
        if (!faixa.getCargoId().equals(cargoId)) {
            throw new ResourceNotFoundException("FaixaSalarial", faixaId);
        }
        return faixa;
    }
}
