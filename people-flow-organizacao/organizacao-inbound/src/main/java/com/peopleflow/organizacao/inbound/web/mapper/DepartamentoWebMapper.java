package com.peopleflow.organizacao.inbound.web.mapper;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.organizacao.core.domain.Departamento;
import com.peopleflow.organizacao.core.query.DepartamentoFilter;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import com.peopleflow.organizacao.inbound.web.dto.DepartamentoFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.DepartamentoRequest;
import com.peopleflow.organizacao.inbound.web.dto.DepartamentoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DepartamentoWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Departamento toDomain(DepartamentoRequest request);

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    DepartamentoResponse toResponse(Departamento departamento);

    DepartamentoFilter toDomain(DepartamentoFilterRequest request);

    default PagedResult<DepartamentoResponse> toPagedResponse(PagedResult<Departamento> pagedResult) {
        if (pagedResult == null) return null;

        return new PagedResult<>(
                pagedResult.content().stream()
                        .map(this::toResponse)
                        .toList(),
                pagedResult.totalElements(),
                pagedResult.totalPages(),
                pagedResult.page(),
                pagedResult.size()
        );
    }

    @Named("stringToStatus")
    default StatusOrganizacao stringToStatus(String status) {
        return status != null ? StatusOrganizacao.of(status) : null;
    }

    @Named("statusToString")
    default String statusToString(StatusOrganizacao status) {
        return status != null ? status.getValor() : null;
    }

}
