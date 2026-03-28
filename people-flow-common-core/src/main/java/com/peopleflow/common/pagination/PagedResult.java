package com.peopleflow.common.pagination;

import java.util.List;
import java.util.function.Function;

/**
 * Resultado paginado de uma consulta
 * 
 * Esta classe substitui Page do Spring Data, permitindo que o core
 * trabalhe com resultados paginados sem conhecer frameworks específicos.
 * 
 * <p><strong>Nota Arquitetural:</strong> Esta é uma abstração pura (sem dependências Spring)
 * que pode ser usada pelo módulo core sem violar os princípios da Arquitetura Hexagonal.
 * Esta classe está no módulo common-core, que não possui dependências Spring.
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

    /**
     * Mapeia os itens da página preservando metadados de paginação.
     */
    public <R> PagedResult<R> map(Function<T, R> mapper) {
        return new PagedResult<>(
                content.stream().map(mapper).toList(),
                totalElements,
                totalPages,
                page,
                size);
    }

    public static <T, R> PagedResult<R> map(PagedResult<T> source, Function<T, R> mapper) {
        if (source == null) {
            return null;
        }
        return source.map(mapper);
    }
}

