package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.ContaBancaria;
import com.peopleflow.pessoascontratos.core.query.ContaBancariaFilter;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContaBancariaFilterRequest;
import com.peopleflow.pessoascontratos.core.valueobject.TipoContaBancaria;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContaBancariaRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContaBancariaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ContaBancariaWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colaboradorId", ignore = true)
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "strToTipo")
    ContaBancaria toDomain(ContaBancariaRequest request);

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoToStr")
    ContaBancariaResponse toResponse(ContaBancaria conta);

    ContaBancariaFilter toDomain(ContaBancariaFilterRequest request);

    @Named("strToTipo")
    default TipoContaBancaria strToTipo(String valor) {
        return TipoContaBancaria.ofNullable(valor);
    }

    @Named("tipoToStr")
    default String tipoToStr(TipoContaBancaria tipo) {
        return tipo != null ? tipo.getValor() : null;
    }
}
