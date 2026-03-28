package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.Cargo;
import com.peopleflow.pessoascontratos.inbound.web.dto.CargoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.CargoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CargoWebMapper {

    @Mapping(target = "id", ignore = true)
    Cargo toDomain(CargoRequest request);

    CargoResponse toResponse(Cargo cargo);
}
