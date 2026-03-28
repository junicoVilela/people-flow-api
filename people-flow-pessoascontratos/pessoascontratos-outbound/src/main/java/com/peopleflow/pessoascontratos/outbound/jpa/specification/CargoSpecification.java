package com.peopleflow.pessoascontratos.outbound.jpa.specification;

import com.peopleflow.pessoascontratos.core.query.CargoFilter;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.CargoEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class CargoSpecification {

    private CargoSpecification() {
    }

    public static Specification<CargoEntity> filter(CargoFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("excluidoEm")));

            if (filter != null) {
                if (filter.getNome() != null && !filter.getNome().isBlank()) {
                    predicates.add(cb.like(
                            cb.lower(root.get("nome")),
                            "%" + filter.getNome().toLowerCase().trim() + "%"));
                }
                if (filter.getCodigo() != null && !filter.getCodigo().isBlank()) {
                    predicates.add(cb.like(
                            cb.lower(root.get("codigo")),
                            "%" + filter.getCodigo().toLowerCase().trim() + "%"));
                }
                if (filter.getNivelHierarquicoId() != null) {
                    predicates.add(cb.equal(root.join("nivelHierarquico").get("id"), filter.getNivelHierarquicoId()));
                }
                if (filter.getFamiliaCargoId() != null) {
                    predicates.add(cb.equal(root.join("familiaCargo").get("id"), filter.getFamiliaCargoId()));
                }
                if (filter.getDepartamentoId() != null) {
                    predicates.add(cb.equal(root.get("departamentoId"), filter.getDepartamentoId()));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
