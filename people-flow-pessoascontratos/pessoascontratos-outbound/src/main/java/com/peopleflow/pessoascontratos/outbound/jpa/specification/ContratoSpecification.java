package com.peopleflow.pessoascontratos.outbound.jpa.specification;

import com.peopleflow.pessoascontratos.core.query.ContratoFilter;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ContratoEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class ContratoSpecification {

    private ContratoSpecification() {
    }

    public static Specification<ContratoEntity> filter(Long colaboradorId, ContratoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("excluidoEm")));
            predicates.add(cb.equal(root.get("colaboradorId"), colaboradorId));

            if (filter != null) {
                if (filter.getTipo() != null && !filter.getTipo().isBlank()) {
                    predicates.add(cb.equal(
                            cb.lower(root.get("tipo")),
                            filter.getTipo().toLowerCase().trim()));
                }
                if (filter.getRegime() != null && !filter.getRegime().isBlank()) {
                    predicates.add(cb.equal(
                            cb.lower(root.get("regime")),
                            filter.getRegime().toLowerCase().trim()));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
