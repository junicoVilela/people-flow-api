package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Cargo;
import com.peopleflow.pessoascontratos.core.ports.input.CargoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.CargoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.FamiliaCargoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.NivelHierarquicoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class CargoService implements CargoUseCase {

    private static final Logger log = LoggerFactory.getLogger(CargoService.class);

    private final CargoRepositoryPort cargoRepository;
    private final NivelHierarquicoRepositoryPort nivelHierarquicoRepository;
    private final FamiliaCargoRepositoryPort familiaCargoRepository;

    @Override
    public Cargo criar(Cargo dados) {
        validarReferencias(dados.getNivelHierarquicoId(), dados.getFamiliaCargoId());
        validarCodigoUnico(dados.getCodigo(), null);
        Cargo novo = Cargo.novo(
                dados.getCodigo(),
                dados.getNome(),
                dados.getDescricao(),
                dados.getNivelHierarquicoId(),
                dados.getFamiliaCargoId(),
                dados.getDepartamentoId());
        Cargo salvo = cargoRepository.salvar(novo);
        log.info("Cargo criado: id={}, codigo={}", salvo.getId(), salvo.getCodigo());
        return salvo;
    }

    @Override
    public Cargo atualizar(Long id, Cargo dados) {
        buscarAtivo(id);
        validarReferencias(dados.getNivelHierarquicoId(), dados.getFamiliaCargoId());
        validarCodigoUnico(dados.getCodigo(), id);
        Cargo existente = buscarAtivo(id);
        Cargo atualizado = existente.atualizar(
                dados.getCodigo(),
                dados.getNome(),
                dados.getDescricao(),
                dados.getNivelHierarquicoId(),
                dados.getFamiliaCargoId(),
                dados.getDepartamentoId());
        return cargoRepository.salvar(atualizado);
    }

    @Override
    public Cargo buscarPorId(Long id) {
        return buscarAtivo(id);
    }

    @Override
    public PagedResult<Cargo> listar(Pagination pagination) {
        return cargoRepository.listarAtivos(pagination);
    }

    @Override
    public void excluir(Long id) {
        buscarAtivo(id);
        if (cargoRepository.contarContratosAtivosPorCargoId(id) > 0) {
            throw new BusinessException("CARGO_EM_USO_CONTRATO", "Existem contratos ativos vinculados a este cargo.");
        }
        if (cargoRepository.contarColaboradoresAtivosPorCargoId(id) > 0) {
            throw new BusinessException("CARGO_EM_USO_COLABORADOR", "Existem colaboradores ativos vinculados a este cargo.");
        }
        if (cargoRepository.contarFaixasAtivasPorCargoId(id) > 0) {
            throw new BusinessException("CARGO_EM_USO_FAIXA", "Existem faixas salariais ativas para este cargo. Exclua-as primeiro.");
        }
        cargoRepository.excluir(id);
        log.info("Cargo excluído: id={}", id);
    }

    private Cargo buscarAtivo(Long id) {
        return cargoRepository.buscarAtivoPorId(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo", id));
    }

    private void validarReferencias(Long nivelId, Long familiaId) {
        if (!nivelHierarquicoRepository.existeAtivoPorId(nivelId)) {
            throw new BusinessException("NIVEL_NAO_ENCONTRADO", "Nível hierárquico não encontrado ou inativo.");
        }
        if (!familiaCargoRepository.existeAtivoPorId(familiaId)) {
            throw new BusinessException("FAMILIA_NAO_ENCONTRADA", "Família de cargo não encontrada ou inativa.");
        }
    }

    private void validarCodigoUnico(String codigo, Long excluirId) {
        if (cargoRepository.existeCodigoIgnorandoMaiusculas(codigo, excluirId)) {
            throw new BusinessException("CARGO_CODIGO_DUPLICADO", "Já existe um cargo ativo com este código.");
        }
    }
}
