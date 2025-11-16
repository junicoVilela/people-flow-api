package com.peopleflow.organizacao.core.application;

import com.peopleflow.common.exception.DuplicateResourceException;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.ports.input.EmpresaUseCase;
import com.peopleflow.organizacao.core.ports.output.EmpresaRepositoryPort;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class EmpresaService implements EmpresaUseCase {

    private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);

    private final EmpresaRepositoryPort empresaRepositoryPort;


    @Override
    public Empresa criar(Empresa empresa) {
        log.info("Iniciando criação de Empresa: nome={}, cnpj={}, inscricaoEstadual={}, status={}, clienteId={}",
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getInscricaoEstadual(),
                empresa.getStatus(),
                empresa.getClienteId());

        validarUnicidadeCriacao(empresa);

        Empresa empresaCriar = empresaRepositoryPort.salvar(empresa);

        log.info("Iniciando criação de Empresa: nome={}, cnpj={}, inscricaoEstadual={}, status={}, clienteId={}",
                empresa.getNome(),
                empresa.getCnpj(),
                empresa.getInscricaoEstadual(),
                empresa.getStatus(),
                empresa.getClienteId());

        return empresaCriar;
    }

    @Override
    public Empresa atualizar(Long id, Empresa colaborador) {
        return null;
    }

    @Override
    public Empresa buscarPorId(Long id) {
        return null;
    }

    @Override
    public PagedResult<Empresa> buscarPorFiltros(EmpresaFilter filter, Pagination pagination) {
        return null;
    }

    @Override
    public Empresa excluir(Long id) {
        return null;
    }

    private void validarUnicidadeCriacao(Empresa empresa) {
        validarUnicidadeCampo(
                "CNPJ",
                empresa.getCnpj(),
                empresaRepositoryPort::existePorCNPJ
        );
    }

    private void validarUnicidadeCampo(
            String nomeCampo,
            String valor,
            Predicate<String> validador) {

        if (validador.test(valor)) {
            throw new DuplicateResourceException(nomeCampo, valor);
        }
    }
}
