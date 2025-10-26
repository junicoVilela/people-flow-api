package com.peopleflow.pessoascontratos.outbound.jpa.specification;

import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ColaboradorSpecification {

    private ColaboradorSpecification() {
        // Utility class
    }

    /**
     * Constrói dinamicamente a Specification baseada nos filtros fornecidos.
     * Todos os filtros são aplicados com AND lógico.
     */
    public static Specification<ColaboradorEntity> comFiltros(ColaboradorFilter filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Validação básica
            if (filter == null) {
                return criteriaBuilder.conjunction();
            }

            // Filtro por nome (LIKE case-insensitive)
            if (filter.getNome() != null && !filter.getNome().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
                        "%" + filter.getNome().toLowerCase().trim() + "%"
                    )
                );
            }

            // Filtro por CPF (exact match ou LIKE para busca parcial)
            if (filter.getCpf() != null && !filter.getCpf().trim().isEmpty()) {
                String cpfLimpo = filter.getCpf().replaceAll("[^0-9]", "");
                predicates.add(
                    criteriaBuilder.like(
                        root.get("cpf"),
                        "%" + cpfLimpo + "%"
                    )
                );
            }

            // Filtro por email (LIKE case-insensitive)
            if (filter.getEmail() != null && !filter.getEmail().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        "%" + filter.getEmail().toLowerCase().trim() + "%"
                    )
                );
            }

            // Filtro por matrícula (exact match)
            if (filter.getMatricula() != null && !filter.getMatricula().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.equal(root.get("matricula"), filter.getMatricula().trim())
                );
            }

            // Filtro por status (exact match)
            if (filter.getStatus() != null && !filter.getStatus().trim().isEmpty()) {
                predicates.add(
                    criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("status")),
                        filter.getStatus().toLowerCase().trim()
                    )
                );
            }

            // Filtro por cliente ID
            if (filter.getClienteId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("clienteId"), filter.getClienteId())
                );
            }

            // Filtro por empresa ID
            if (filter.getEmpresaId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("empresaId"), filter.getEmpresaId())
                );
            }

            // Filtro por departamento ID
            if (filter.getDepartamentoId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("departamentoId"), filter.getDepartamentoId())
                );
            }

            // Filtro por centro de custo ID
            if (filter.getCentroCustoId() != null) {
                predicates.add(
                    criteriaBuilder.equal(root.get("centroCustoId"), filter.getCentroCustoId())
                );
            }

            // Filtro por período de admissão
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

            // Filtro por período de demissão
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

            // Retorna todos os predicates combinados com AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

