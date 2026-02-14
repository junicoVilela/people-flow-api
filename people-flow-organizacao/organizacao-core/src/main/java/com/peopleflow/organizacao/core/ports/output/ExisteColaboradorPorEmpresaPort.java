package com.peopleflow.organizacao.core.ports.output;

public interface ExisteColaboradorPorEmpresaPort {

    /**
     * @param empresaId ID da empresa
     * @return true se existe ao menos um colaborador (não excluído) vinculado à empresa
     */
    boolean existePorEmpresaId(Long empresaId);
}
