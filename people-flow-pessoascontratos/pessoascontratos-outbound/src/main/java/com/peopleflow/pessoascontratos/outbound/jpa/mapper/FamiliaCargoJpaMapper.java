package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.FamiliaCargo;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.FamiliaCargoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FamiliaCargoJpaMapper {

    FamiliaCargo toDomain(FamiliaCargoEntity entity);

    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "excluidoPor", ignore = true)
    @Mapping(target = "excluidoEm", ignore = true)
    FamiliaCargoEntity toEntity(FamiliaCargo familiaCargo);
}
