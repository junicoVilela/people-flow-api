package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.JornadaTrabalho;
import com.peopleflow.pessoascontratos.inbound.web.dto.JornadaTrabalhoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.JornadaTrabalhoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface JornadaTrabalhoWebMapper {

    @Mapping(target = "id", ignore = true)
    JornadaTrabalho toDomain(JornadaTrabalhoRequest request);

    JornadaTrabalhoResponse toResponse(JornadaTrabalho jornada);
}
