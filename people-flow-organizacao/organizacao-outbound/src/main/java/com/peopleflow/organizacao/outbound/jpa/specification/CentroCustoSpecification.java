package com.peopleflow.organizacao.outbound.jpa.specification;

import com.peopleflow.organizacao.core.query.CentroCustoFilter;
import com.peopleflow.organizacao.outbound.jpa.entity.CentroCustoEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CentroCustoSpecification {

    private CentroCustoSpecification() {
    }

    public static Specification<CentroCustoEntity> filter(CentroCustoFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return criteriaBuilder.notEqual(root.get("status"), "excluido");
            }

            predicates.add(criteriaBuilder.notEqual(root.get("status"), "excluido"));

            if (filter.getNome() != null && !filter.getNome().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + filter.getNome().toLowerCase().trim() + "%"
                    )
                );
            }

            if (filter.getCodigo() != null && !filter.getCodigo().trim().isEmpty()) {
                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("codigo")),
                                filter.getCodigo().toLowerCase().trim()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

