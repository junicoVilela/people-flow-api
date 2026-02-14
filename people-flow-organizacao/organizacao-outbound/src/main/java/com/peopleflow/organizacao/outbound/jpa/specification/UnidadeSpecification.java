package com.peopleflow.organizacao.outbound.jpa.specification;

import com.peopleflow.organizacao.core.query.UnidadeFilter;
import com.peopleflow.organizacao.outbound.jpa.entity.UnidadeEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class UnidadeSpecification {

    private UnidadeSpecification() {
    }

    public static Specification<UnidadeEntity> filter(UnidadeFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return criteriaBuilder.notEqual(root.get("status"), "excluido");
            }

            predicates.add(criteriaBuilder.notEqual(root.get("status"), "excluido"));

            if (filter.getEmpresaId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("empresaId"), filter.getEmpresaId()));
            }

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

