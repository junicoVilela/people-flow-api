package com.peopleflow.pessoascontratos.outbound.jpa.specification;

import com.peopleflow.pessoascontratos.core.query.DependenteFilter;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.DependenteEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class DependenteSpecification {

    private DependenteSpecification() {
    }

    public static Specification<DependenteEntity> filter(Long colaboradorId, DependenteFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("excluidoEm")));
            predicates.add(cb.equal(root.get("colaboradorId"), colaboradorId));

            if (filter != null) {
                if (filter.getNome() != null && !filter.getNome().isBlank()) {
                    predicates.add(cb.like(
                            cb.lower(root.get("nome")),
                            "%" + filter.getNome().toLowerCase().trim() + "%"));
                }
                if (filter.getParentesco() != null && !filter.getParentesco().isBlank()) {
                    predicates.add(cb.equal(
                            cb.lower(root.get("parentesco")),
                            filter.getParentesco().toLowerCase().trim()));
                }
                if (filter.getCpf() != null && !filter.getCpf().isBlank()) {
                    String digits = filter.getCpf().replaceAll("[^0-9]", "");
                    predicates.add(cb.like(root.get("cpf"), "%" + (digits.isEmpty() ? filter.getCpf().trim() : digits) + "%"));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
