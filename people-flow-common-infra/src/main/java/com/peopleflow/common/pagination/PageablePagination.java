package com.peopleflow.common.pagination;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Converte {@link Pageable} do Spring Data em {@link Pagination} do domínio.
 * <p>Apenas o <strong>primeiro</strong> critério de {@link Sort} é considerado; demais são ignorados.
 */
public final class PageablePagination {

    private PageablePagination() {
    }

    public static Pagination from(Pageable pageable) {
        if (pageable == null) {
            return Pagination.of(0, Pagination.DEFAULT_PAGE_SIZE);
        }
        return pageable.getSort().stream()
                .findFirst()
                .map(order -> Pagination.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        order.getProperty(),
                        order.getDirection() == Sort.Direction.ASC
                                ? Pagination.SortDirection.ASC
                                : Pagination.SortDirection.DESC))
                .orElseGet(() -> Pagination.of(pageable.getPageNumber(), pageable.getPageSize()));
    }
}
