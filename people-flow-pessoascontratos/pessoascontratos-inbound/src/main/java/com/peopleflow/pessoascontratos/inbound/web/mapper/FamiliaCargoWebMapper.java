package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.FamiliaCargo;
import com.peopleflow.pessoascontratos.inbound.web.dto.FamiliaCargoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.FamiliaCargoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FamiliaCargoWebMapper {

    @Mapping(target = "id", ignore = true)
    FamiliaCargo toDomain(FamiliaCargoRequest request);

    FamiliaCargoResponse toResponse(FamiliaCargo familiaCargo);
}
