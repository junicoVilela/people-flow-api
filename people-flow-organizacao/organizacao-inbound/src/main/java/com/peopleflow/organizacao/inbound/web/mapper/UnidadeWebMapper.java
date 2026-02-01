package com.peopleflow.organizacao.inbound.web.mapper;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.valueobject.Cnpj;
import com.peopleflow.common.valueobject.InscricaoEstadual;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import com.peopleflow.organizacao.core.query.UnidadeFilter;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaRequest;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaResponse;
import com.peopleflow.organizacao.inbound.web.dto.UnidadeFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.UnidadeRequest;
import com.peopleflow.organizacao.inbound.web.dto.UnidadeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UnidadeWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Unidade toDomain(UnidadeRequest request);

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    UnidadeResponse toResponse(Unidade unidade);

    UnidadeFilter toDomain(UnidadeFilterRequest request);

    default PagedResult<UnidadeResponse> toPagedResponse(PagedResult<Unidade> pagedResult) {
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
