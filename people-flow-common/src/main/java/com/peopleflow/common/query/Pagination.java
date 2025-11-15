package com.peopleflow.common.query;

/**
 * Abstração para paginação de resultados
 * 
 * Esta classe substitui Pageable do Spring Data, permitindo que o core
 * trabalhe com paginação sem conhecer frameworks específicos.
 * 
 * <p><strong>Nota Arquitetural:</strong> Esta é uma abstração pura (sem dependências Spring)
 * que pode ser usada pelo módulo core sem violar os princípios da Arquitetura Hexagonal.
 * Embora esteja no módulo common (que tem dependências Spring), esta classe específica
 * não possui nenhuma dependência de framework.
 */
public record Pagination(
    int page,
    int size,
    String sortBy,
    SortDirection direction
) {
    
    public enum SortDirection {
        ASC, DESC
    }
    
    /**
     * Cria uma paginação simples sem ordenação
     * 
     * @param page Número da página (0-indexed)
     * @param size Tamanho da página
     * @return Pagination
     */
    public static Pagination of(int page, int size) {
        return new Pagination(page, size, null, SortDirection.ASC);
    }
    
    /**
     * Cria uma paginação com ordenação
     * 
     * @param page Número da página (0-indexed)
     * @param size Tamanho da página
     * @param sortBy Campo para ordenação
     * @param direction Direção da ordenação
     * @return Pagination
     */
    public static Pagination of(int page, int size, String sortBy, SortDirection direction) {
        return new Pagination(page, size, sortBy, direction);
    }
    
    /**
     * Cria uma paginação com ordenação ascendente
     * 
     * @param page Número da página (0-indexed)
     * @param size Tamanho da página
     * @param sortBy Campo para ordenação
     * @return Pagination
     */
    public static Pagination of(int page, int size, String sortBy) {
        return new Pagination(page, size, sortBy, SortDirection.ASC);
    }
}

