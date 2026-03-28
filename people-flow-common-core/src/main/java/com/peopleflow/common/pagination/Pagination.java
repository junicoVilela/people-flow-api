package com.peopleflow.common.pagination;

/**
 * Abstração para paginação de resultados
 * 
 * Esta classe substitui Pageable do Spring Data, permitindo que o core
 * trabalhe com paginação sem conhecer frameworks específicos.
 * 
 * <p><strong>Nota Arquitetural:</strong> Esta é uma abstração pura (sem dependências Spring)
 * que pode ser usada pelo módulo core sem violar os princípios da Arquitetura Hexagonal.
 * Esta classe está no módulo common-core, que não possui dependências Spring.
 */
public record Pagination(
    int page,
    int size,
    String sortBy,
    SortDirection direction
) {

    /** Tamanho padrão de página na API (alinhar a {@code @RequestParam} / {@code @PageableDefault}). */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /** Limite superior de itens por página. */
    public static final int MAX_PAGE_SIZE = 200;

    /** Para {@code @RequestParam(defaultValue = ...)} — manter igual a {@link #DEFAULT_PAGE_SIZE}. */
    public static final String DEFAULT_PAGE_SIZE_PARAM = "20";

    public enum SortDirection {
        ASC, DESC
    }

    public static int normalizePage(int page) {
        return Math.max(0, page);
    }

    public static int normalizeSize(int size) {
        if (size < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, MAX_PAGE_SIZE);
    }

    /**
     * Cria uma paginação simples sem ordenação
     *
     * @param page Número da página (0-indexed)
     * @param size Tamanho da página
     * @return Pagination
     */
    public static Pagination of(int page, int size) {
        return new Pagination(normalizePage(page), normalizeSize(size), null, SortDirection.ASC);
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
        return new Pagination(normalizePage(page), normalizeSize(size), sortBy, direction);
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
        return new Pagination(normalizePage(page), normalizeSize(size), sortBy, SortDirection.ASC);
    }
}

