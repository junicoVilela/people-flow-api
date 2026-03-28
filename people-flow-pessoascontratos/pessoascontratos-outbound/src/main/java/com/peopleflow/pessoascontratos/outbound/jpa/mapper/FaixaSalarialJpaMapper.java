package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.FaixaSalarial;
import com.peopleflow.pessoascontratos.core.valueobject.MoedaFaixa;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.FaixaSalarialEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FaixaSalarialJpaMapper {

    @Mapping(target = "moeda", source = "moeda", qualifiedByName = "stringToMoeda")
    FaixaSalarial toDomain(FaixaSalarialEntity entity);

    @Mapping(target = "moeda", source = "moeda", qualifiedByName = "moedaToString")
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "excluidoPor", ignore = true)
    @Mapping(target = "excluidoEm", ignore = true)
    FaixaSalarialEntity toEntity(FaixaSalarial faixa);

    @Named("stringToMoeda")
    default MoedaFaixa stringToMoeda(String moeda) {
        return moeda == null ? MoedaFaixa.BRL : MoedaFaixa.of(moeda);
    }

    @Named("moedaToString")
    default String moedaToString(MoedaFaixa moeda) {
        return moeda != null ? moeda.getValor() : null;
    }
}
