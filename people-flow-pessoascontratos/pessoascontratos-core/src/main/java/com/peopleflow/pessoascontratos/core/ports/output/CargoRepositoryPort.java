package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Cargo;

import java.util.Optional;

public interface CargoRepositoryPort {

    boolean existeAtivoPorId(Long cargoId);

    Cargo salvar(Cargo cargo);

    Optional<Cargo> buscarAtivoPorId(Long id);

    PagedResult<Cargo> listarAtivos(Pagination pagination);

    boolean existeCodigoIgnorandoMaiusculas(String codigo, Long excluirId);

    long contarContratosAtivosPorCargoId(Long cargoId);

    long contarColaboradoresAtivosPorCargoId(Long cargoId);

    long contarFaixasAtivasPorCargoId(Long cargoId);

    void excluir(Long id);
}
