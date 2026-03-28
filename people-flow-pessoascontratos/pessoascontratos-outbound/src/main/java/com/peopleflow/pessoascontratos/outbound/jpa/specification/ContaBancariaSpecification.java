package com.peopleflow.pessoascontratos.outbound.jpa.specification;

import com.peopleflow.pessoascontratos.core.query.ContaBancariaFilter;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ContaBancariaEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class ContaBancariaSpecification {

    private ContaBancariaSpecification() {
    }

    public static Specification<ContaBancariaEntity> filter(Long colaboradorId, ContaBancariaFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("excluidoEm")));
            predicates.add(cb.equal(root.get("colaboradorId"), colaboradorId));

            if (filter != null) {
                if (filter.getBanco() != null && !filter.getBanco().isBlank()) {
                    predicates.add(cb.like(
                            cb.lower(root.get("banco")),
                            "%" + filter.getBanco().toLowerCase().trim() + "%"));
                }
                if (filter.getAgencia() != null && !filter.getAgencia().isBlank()) {
                    predicates.add(cb.like(
                            cb.lower(root.get("agencia")),
                            "%" + filter.getAgencia().toLowerCase().trim() + "%"));
                }
                if (filter.getConta() != null && !filter.getConta().isBlank()) {
                    predicates.add(cb.like(
                            cb.lower(root.get("conta")),
                            "%" + filter.getConta().toLowerCase().trim() + "%"));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
