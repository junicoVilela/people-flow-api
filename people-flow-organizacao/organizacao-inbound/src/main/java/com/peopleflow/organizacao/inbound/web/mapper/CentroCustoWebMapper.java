package com.peopleflow.organizacao.inbound.web.mapper;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.organizacao.core.domain.CentroCusto;
import com.peopleflow.organizacao.core.query.CentroCustoFilter;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import com.peopleflow.organizacao.inbound.web.dto.CentroCustoFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.CentroCustoRequest;
import com.peopleflow.organizacao.inbound.web.dto.CentroCustoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CentroCustoWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    CentroCusto toDomain(CentroCustoRequest request);

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    CentroCustoResponse toResponse(CentroCusto centroCusto);

    CentroCustoFilter toDomain(CentroCustoFilterRequest request);

    default PagedResult<CentroCustoResponse> toPagedResponse(PagedResult<CentroCusto> pagedResult) {
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
