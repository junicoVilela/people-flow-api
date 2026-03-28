package com.peopleflow.pessoascontratos.outbound.jpa.specification;

import com.peopleflow.pessoascontratos.core.query.FaixaSalarialFilter;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.FaixaSalarialEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class FaixaSalarialSpecification {

    private FaixaSalarialSpecification() {
    }

    public static Specification<FaixaSalarialEntity> filter(Long cargoId, FaixaSalarialFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("excluidoEm")));
            predicates.add(cb.equal(root.get("cargoId"), cargoId));

            if (filter != null && filter.getMoeda() != null && !filter.getMoeda().isBlank()) {
                predicates.add(cb.equal(
                        cb.upper(root.get("moeda")),
                        filter.getMoeda().toUpperCase().trim()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
