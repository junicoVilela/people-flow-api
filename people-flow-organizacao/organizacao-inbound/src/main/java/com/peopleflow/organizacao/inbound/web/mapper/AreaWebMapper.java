package com.peopleflow.organizacao.inbound.web.mapper;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.organizacao.core.domain.Area;
import com.peopleflow.organizacao.core.query.AreaFilter;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import com.peopleflow.organizacao.inbound.web.dto.AreaFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.AreaRequest;
import com.peopleflow.organizacao.inbound.web.dto.AreaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AreaWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Area toDomain(AreaRequest request);

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    AreaResponse toResponse(Area area);

    AreaFilter toDomain(AreaFilterRequest request);

    default PagedResult<AreaResponse> toPagedResponse(PagedResult<Area> pagedResult) {
        return PagedResult.map(pagedResult, this::toResponse);
    }

    @Named("statusToString")
    default String statusToString(StatusOrganizacao status) {
        return status != null ? status.getValor() : null;
    }
}
