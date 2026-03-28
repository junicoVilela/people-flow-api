package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.NivelHierarquico;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.NivelHierarquicoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NivelHierarquicoJpaMapper {

    NivelHierarquico toDomain(NivelHierarquicoEntity entity);

    NivelHierarquicoEntity toEntity(NivelHierarquico nivelHierarquico);
}
