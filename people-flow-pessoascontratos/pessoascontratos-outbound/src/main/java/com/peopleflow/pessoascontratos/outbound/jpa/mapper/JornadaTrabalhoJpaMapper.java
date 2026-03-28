package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.JornadaTrabalho;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.JornadaTrabalhoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JornadaTrabalhoJpaMapper {

    JornadaTrabalho toDomain(JornadaTrabalhoEntity entity);

    JornadaTrabalhoEntity toEntity(JornadaTrabalho jornada);
}
