package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.FaixaSalarial;
import com.peopleflow.pessoascontratos.core.query.FaixaSalarialFilter;
import com.peopleflow.pessoascontratos.inbound.web.dto.FaixaSalarialFilterRequest;
import com.peopleflow.pessoascontratos.core.valueobject.MoedaFaixa;
import com.peopleflow.pessoascontratos.inbound.web.dto.FaixaSalarialRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.FaixaSalarialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FaixaSalarialWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cargoId", ignore = true)
    @Mapping(target = "moeda", source = "moeda", qualifiedByName = "strToMoeda")
    FaixaSalarial toDomain(FaixaSalarialRequest request);

    @Mapping(target = "moeda", source = "moeda", qualifiedByName = "moedaToStr")
    FaixaSalarialResponse toResponse(FaixaSalarial faixa);

    FaixaSalarialFilter toDomain(FaixaSalarialFilterRequest request);

    @Named("strToMoeda")
    default MoedaFaixa strToMoeda(String moeda) {
        return MoedaFaixa.of(moeda);
    }

    @Named("moedaToStr")
    default String moedaToStr(MoedaFaixa moeda) {
        return moeda != null ? moeda.getValor() : null;
    }
}
