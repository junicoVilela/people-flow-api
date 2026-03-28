package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.Cargo;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.CargoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CargoJpaMapper {

    @Mapping(target = "nivelHierarquicoId", source = "nivelHierarquico.id")
    @Mapping(target = "familiaCargoId", source = "familiaCargo.id")
    Cargo toDomain(CargoEntity entity);

    @Mapping(target = "nivelHierarquico", ignore = true)
    @Mapping(target = "familiaCargo", ignore = true)
    CargoEntity toEntity(Cargo cargo);
}
