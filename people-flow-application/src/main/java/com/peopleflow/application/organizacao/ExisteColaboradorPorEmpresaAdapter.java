package com.peopleflow.application.organizacao;

import com.peopleflow.organizacao.core.ports.output.ExisteColaboradorPorEmpresaPort;
import com.peopleflow.pessoascontratos.core.ports.output.ColaboradorRepositoryPort;
import org.springframework.stereotype.Component;

/**
 * Implementação do port que verifica se existem colaboradores vinculados a uma empresa.
 * Usado pelo EmpresaService para bloquear exclusão quando houver colaboradores.
 */
@Component
public class ExisteColaboradorPorEmpresaAdapter implements ExisteColaboradorPorEmpresaPort {

    private final ColaboradorRepositoryPort colaboradorRepository;

    public ExisteColaboradorPorEmpresaAdapter(ColaboradorRepositoryPort colaboradorRepository) {
        this.colaboradorRepository = colaboradorRepository;
    }

    @Override
    public boolean existePorEmpresaId(Long empresaId) {
        return colaboradorRepository.existeNaoExcluidoPorEmpresaId(empresaId);
    }
}
