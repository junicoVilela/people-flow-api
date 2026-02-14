package com.peopleflow.application.organizacao;

import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.organizacao.core.ports.output.ExisteColaboradorPorEmpresaPort;
import com.peopleflow.pessoascontratos.core.ports.output.ColaboradorRepositoryPort;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
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
        ColaboradorFilter filter = ColaboradorFilter.builder().empresaId(empresaId).build();
        return colaboradorRepository.buscarPorFiltros(filter, Pagination.of(0, 1)).totalElements() > 0;
    }
}
