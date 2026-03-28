package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.ContaBancaria;
import com.peopleflow.pessoascontratos.core.valueobject.TipoContaBancaria;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ContaBancariaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ContaBancariaJpaMapper {

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "strToTipo")
    ContaBancaria toDomain(ContaBancariaEntity entity);

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoToStr")
    ContaBancariaEntity toEntity(ContaBancaria conta);

    @Named("strToTipo")
    default TipoContaBancaria strToTipo(String valor) {
        return TipoContaBancaria.ofNullable(valor);
    }

    @Named("tipoToStr")
    default String tipoToStr(TipoContaBancaria tipo) {
        return tipo != null ? tipo.getValor() : null;
    }
}
