package com.peopleflow.pessoascontratos.outbound.jpa.specification;

import com.peopleflow.pessoascontratos.core.query.DocumentoColaboradorFilter;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.DocumentoColaboradorEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public final class DocumentoColaboradorSpecification {

    private DocumentoColaboradorSpecification() {
    }

    public static Specification<DocumentoColaboradorEntity> filter(Long colaboradorId, DocumentoColaboradorFilter filter) {
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
                if (filter.getNomeArquivo() != null && !filter.getNomeArquivo().isBlank()) {
                    predicates.add(cb.like(
                            cb.lower(root.get("nomeArquivo")),
                            "%" + filter.getNomeArquivo().toLowerCase().trim() + "%"));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
