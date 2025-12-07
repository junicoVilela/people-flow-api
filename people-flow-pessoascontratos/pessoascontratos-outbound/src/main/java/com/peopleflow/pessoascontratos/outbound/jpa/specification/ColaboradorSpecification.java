package com.peopleflow.pessoascontratos.outbound.jpa.specification;

import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ColaboradorSpecification {

    private ColaboradorSpecification() {
    }

    public static Specification<ColaboradorEntity> filter(ColaboradorFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter == null) {
                return criteriaBuilder.isNull(root.get("excluidoEm"));
            }

            predicates.add(criteriaBuilder.isNull(root.get("excluidoEm")));

            if (filter.getNome() != null && !filter.getNome().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + filter.getNome().toLowerCase().trim() + "%"
                    )
                );
            }

            if (filter.getCpf() != null && !filter.getCpf().trim().isEmpty()) {
                String cpfLimpo = filter.getCpf().replaceAll("[^0-9]", "");
                predicates.add(
                    criteriaBuilder.like(
                        root.get("cpf"),
                        "%" + cpfLimpo + "%"
                    )
                );
            }

            if (filter.getEmail() != null && !filter.getEmail().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + filter.getEmail().toLowerCase().trim() + "%"
                    )
                );
            }

            if (filter.getMatricula() != null && !filter.getMatricula().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.equal(root.get("matricula"), filter.getMatricula().trim())
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

            if (filter.getClienteId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("clienteId"), filter.getClienteId())
                );
            }

            if (filter.getEmpresaId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("empresaId"), filter.getEmpresaId())
                );
            }

            if (filter.getDepartamentoId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("departamentoId"), filter.getDepartamentoId())
                );
            }

            if (filter.getCentroCustoId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("centroCustoId"), filter.getCentroCustoId())
                );
            }

            if (filter.getDataAdmissaoInicio() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("dataAdmissao"), 
                        filter.getDataAdmissaoInicio()
                    )
                );
            }
            if (filter.getDataAdmissaoFim() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("dataAdmissao"), 
                        filter.getDataAdmissaoFim()
                    )
                );
            }

            if (filter.getDataDemissaoInicio() != null) {
                predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                        root.get("dataDemissao"), 
                        filter.getDataDemissaoInicio()
                    )
                );
            }

            if (filter.getDataDemissaoFim() != null) {
                predicates.add(
                    criteriaBuilder.lessThanOrEqualTo(
                        root.get("dataDemissao"), 
                        filter.getDataDemissaoFim()
                    )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

