package com.peopleflow.organizacao.outbound.jpa.specification;

import com.peopleflow.organizacao.core.query.EmpresaFilter;
import com.peopleflow.organizacao.outbound.jpa.entity.EmpresaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmpresaSpecification {

    private EmpresaSpecification() {
    }

    public static Specification<EmpresaEntity> filter(EmpresaFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            if (filter.getNome() != null && !filter.getNome().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + filter.getNome().toLowerCase().trim() + "%"
                    )
                );
            }

            if (filter.getStatus() != null && !filter.getStatus().trim().isEmpty()) {
                predicates.add(
                        criteriaBuilder.equal(
                                criteriaBuilder.lower(root.get("status")),
                                filter.getStatus().toLowerCase().trim()
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

