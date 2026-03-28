package com.peopleflow.pessoascontratos.core.application;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Contrato;
import com.peopleflow.pessoascontratos.core.ports.input.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.core.ports.input.ContratoUseCase;
import com.peopleflow.pessoascontratos.core.ports.output.CargoCatalogoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.ContratoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.DocumentoContratoRepositoryPort;
import com.peopleflow.pessoascontratos.core.ports.output.JornadaTrabalhoRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class ContratoService implements ContratoUseCase {

    private static final Logger log = LoggerFactory.getLogger(ContratoService.class);

    private final ContratoRepositoryPort contratoRepository;
    private final ColaboradorUseCase colaboradorUseCase;
    private final JornadaTrabalhoRepositoryPort jornadaTrabalhoRepository;
    private final CargoCatalogoRepositoryPort cargoCatalogoRepository;
    private final DocumentoContratoRepositoryPort documentoContratoRepository;

    @Override
    public Contrato criar(Long colaboradorId, Contrato dados) {
        log.info("Criando contrato: colaboradorId={}", colaboradorId);
        colaboradorUseCase.buscarPorId(colaboradorId);
        validarReferencias(dados.getJornadaId(), dados.getCargoId());

        Contrato novo = Contrato.novo(
                colaboradorId,
                dados.getJornadaId(),
                dados.getCargoId(),
                dados.getInicio(),
                dados.getFim(),
                dados.getTipo(),
                dados.getRegime(),
                dados.getSalarioBase());

        Contrato salvo = contratoRepository.salvar(novo);
        log.info("Contrato criado: id={}, colaboradorId={}", salvo.getId(), colaboradorId);
        return salvo;
    }

    @Override
    public Contrato atualizar(Long colaboradorId, Long contratoId, Contrato dados) {
        log.info("Atualizando contrato: colaboradorId={}, contratoId={}", colaboradorId, contratoId);
        colaboradorUseCase.buscarPorId(colaboradorId);
        validarReferencias(dados.getJornadaId(), dados.getCargoId());

        Contrato existente = buscarContratoDoColaborador(colaboradorId, contratoId);
        Contrato atualizado = existente.atualizar(
                dados.getJornadaId(),
                dados.getCargoId(),
                dados.getInicio(),
                dados.getFim(),
                dados.getTipo(),
                dados.getRegime(),
                dados.getSalarioBase());

        Contrato salvo = contratoRepository.salvar(atualizado);
        log.info("Contrato atualizado: id={}", salvo.getId());
        return salvo;
    }

    @Override
    public Contrato buscarPorId(Long colaboradorId, Long contratoId) {
        colaboradorUseCase.buscarPorId(colaboradorId);
        return buscarContratoDoColaborador(colaboradorId, contratoId);
    }

    @Override
    public PagedResult<Contrato> listarPorColaborador(Long colaboradorId, Pagination pagination) {
        colaboradorUseCase.buscarPorId(colaboradorId);
        return contratoRepository.listarPorColaboradorId(colaboradorId, pagination);
    }

    @Override
    public void excluir(Long colaboradorId, Long contratoId) {
        log.info("Excluindo contrato (soft delete): colaboradorId={}, contratoId={}", colaboradorId, contratoId);
        buscarPorId(colaboradorId, contratoId);

        if (documentoContratoRepository.contarAtivosPorContratoId(contratoId) > 0) {
            throw new BusinessException(
                    "CONTRATO_POSSUI_DOCUMENTOS",
                    "Não é possível excluir o contrato enquanto existirem documentos ativos vinculados.");
        }

        contratoRepository.excluir(contratoId);
        log.info("Contrato excluído: id={}", contratoId);
    }

    private Contrato buscarContratoDoColaborador(Long colaboradorId, Long contratoId) {
        Contrato contrato = contratoRepository.buscarAtivoPorId(contratoId)
                .orElseThrow(() -> {
                    log.warn("Contrato não encontrado ou excluído: id={}", contratoId);
                    return new ResourceNotFoundException("Contrato", contratoId);
                });
        if (!contrato.getColaboradorId().equals(colaboradorId)) {
            throw new ResourceNotFoundException("Contrato", contratoId);
        }
        return contrato;
    }

    private void validarReferencias(Long jornadaId, Long cargoId) {
        if (!jornadaTrabalhoRepository.existeAtivaPorId(jornadaId)) {
            throw new BusinessException("JORNADA_NAO_ENCONTRADA", "Jornada de trabalho não encontrada ou inativa.");
        }
        if (!cargoCatalogoRepository.existeAtivoPorId(cargoId)) {
            throw new BusinessException("CARGO_NAO_ENCONTRADO", "Cargo não encontrado ou inativo.");
        }
    }
}
