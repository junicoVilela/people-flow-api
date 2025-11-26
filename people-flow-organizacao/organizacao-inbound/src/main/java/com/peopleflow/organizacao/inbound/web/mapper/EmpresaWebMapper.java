package com.peopleflow.organizacao.inbound.web.mapper;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.valueobject.Cnpj;
import com.peopleflow.common.valueobject.InscricaoEstadual;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import com.peopleflow.organizacao.core.valueobjects.StatusEmpresa;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaRequest;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
@Mapper(componentModel = "spring")
public interface EmpresaWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cnpj", source = "cnpj", qualifiedByName = "stringToCnpj")
    @Mapping(target = "inscricaoEstadual", source = "inscricaoEstadual", qualifiedByName = "stringToInscricaoEstadual")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Empresa toDomain(EmpresaRequest request);

    @Mapping(target = "cnpj", source = "cnpj", qualifiedByName = "cnpjToString")
    @Mapping(target = "inscricaoEstadual", source = "inscricaoEstadual", qualifiedByName = "inscricaoEstadualToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    EmpresaResponse toResponse(Empresa empresa);

    EmpresaFilter toDomain(EmpresaFilterRequest request);

    default PagedResult<EmpresaResponse> toPagedResponse(PagedResult<Empresa> pagedResult) {
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

    @Named("stringToCnpj")
    default Cnpj stringToCnpj(String cnpj) {
        return cnpj != null ? new Cnpj(cnpj) : null;
    }

    @Named("stringToInscricaoEstadual")
    default InscricaoEstadual stringToInscricaoEstadual(String ie) {
        return InscricaoEstadual.of(ie);
    }

    @Named("stringToStatus")
    default StatusEmpresa stringToStatus(String status) {
        return status != null ? StatusEmpresa.of(status) : null;
    }

    @Named("cnpjToString")
    default String cnpjToString(Cnpj cnpj) {
        return cnpj != null ? cnpj.getValor() : null;
    }

    @Named("inscricaoEstadualToString")
    default String inscricaoEstadualToString(InscricaoEstadual ie) {
        return ie != null ? ie.getValor() : null;
    }

    @Named("statusToString")
    default String statusToString(StatusEmpresa status) {
        return status != null ? status.getValor() : null;
    }

}
