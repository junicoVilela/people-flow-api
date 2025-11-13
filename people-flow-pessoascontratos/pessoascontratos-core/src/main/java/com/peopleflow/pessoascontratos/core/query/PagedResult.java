package com.peopleflow.pessoascontratos.core.query;

import java.util.List;

/**
 * Resultado paginado de uma consulta
 * 
 * Esta classe substitui Page do Spring Data, permitindo que o core
 * trabalhe com resultados paginados sem conhecer frameworks específicos.
 * 
 * @param <T> Tipo dos elementos
 */
public record PagedResult<T>(
    List<T> content,
    long totalElements,
    int totalPages,
    int page,
    int size
) {
    /**
     * Verifica se há próxima página
     * 
     * @return true se há próxima página
     */
    public boolean hasNext() {
        return page < totalPages - 1;
    }
    
    /**
     * Verifica se há página anterior
     * 
     * @return true se há página anterior
     */
    public boolean hasPrevious() {
        return page > 0;
    }
    
    /**
     * Verifica se está vazio
     * 
     * @return true se não há elementos
     */
    public boolean isEmpty() {
        return content.isEmpty();
    }
}

