package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.NivelHierarquico;
import com.peopleflow.pessoascontratos.inbound.web.dto.NivelHierarquicoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.NivelHierarquicoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NivelHierarquicoWebMapper {

    @Mapping(target = "id", ignore = true)
    NivelHierarquico toDomain(NivelHierarquicoRequest request);

    NivelHierarquicoResponse toResponse(NivelHierarquico nivelHierarquico);
}
