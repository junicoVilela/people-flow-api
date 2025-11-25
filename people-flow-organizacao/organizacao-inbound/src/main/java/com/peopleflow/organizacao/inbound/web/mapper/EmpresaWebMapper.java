package com.peopleflow.organizacao.inbound.web.mapper;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.valueobject.Cnpj;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.query.EmpresaFilter;
import com.peopleflow.organizacao.core.valueobjects.StatusEmpresa;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaFilterRequest;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaRequest;
import com.peopleflow.organizacao.inbound.web.dto.EmpresaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Mapper(componentModel = "spring")
public interface EmpresaWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clienteId", ignore = true)
    @Mapping(target = "cnpj", source = "cnpj", qualifiedByName = "stringToCnpj")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Empresa toDomain(EmpresaRequest request);

    @Mapping(target = "cnpj", source = "cnpj", qualifiedByName = "cnpjToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    EmpresaResponse toResponse(Empresa colaborador);

    EmpresaFilter toDomain(EmpresaFilterRequest request);

    default Page<EmpresaResponse> toPageResponse(PagedResult<Empresa> pagedResult) {
        if (pagedResult == null) return null;

        PageRequest pageRequest = PageRequest.of(pagedResult.page(), pagedResult.size());
        return new PageImpl<>(
                pagedResult.content().stream()
                        .map(this::toResponse)
                        .toList(),
                pageRequest,
                pagedResult.totalElements()
        );
    }

    @Named("stringToCnpj")
    default Cnpj stringToCnpj(String cnpj) {
        return cnpj != null ? new Cnpj(cnpj) : null;
    }

    @Named("stringToStatus")
    default StatusEmpresa stringToStatus(String status) {
        return status != null ? StatusEmpresa.of(status) : null;
    }

    @Named("cnpjToString")
    default String cnpjToString(Cnpj cnpj) {
        return cnpj != null ? cnpj.getValor() : null;
    }

    @Named("statusToString")
    default String statusToString(StatusEmpresa status) {
        return status != null ? status.getValor() : null;
    }

}
