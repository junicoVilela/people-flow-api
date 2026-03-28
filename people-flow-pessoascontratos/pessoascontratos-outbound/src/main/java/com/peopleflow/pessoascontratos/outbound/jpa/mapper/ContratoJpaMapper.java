package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.Contrato;
import com.peopleflow.pessoascontratos.core.valueobject.RegimeContrato;
import com.peopleflow.pessoascontratos.core.valueobject.TipoContrato;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ContratoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ContratoJpaMapper {

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "stringToTipoContrato")
    @Mapping(target = "regime", source = "regime", qualifiedByName = "stringToRegimeContrato")
    Contrato toDomain(ContratoEntity entity);

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoContratoToString")
    @Mapping(target = "regime", source = "regime", qualifiedByName = "regimeContratoToString")
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "excluidoPor", ignore = true)
    @Mapping(target = "excluidoEm", ignore = true)
    ContratoEntity toEntity(Contrato contrato);

    @Named("stringToTipoContrato")
    default TipoContrato stringToTipoContrato(String tipo) {
        return TipoContrato.ofNullable(tipo);
    }

    @Named("stringToRegimeContrato")
    default RegimeContrato stringToRegimeContrato(String regime) {
        return RegimeContrato.ofNullable(regime);
    }

    @Named("tipoContratoToString")
    default String tipoContratoToString(TipoContrato tipo) {
        return tipo != null ? tipo.getValor() : null;
    }

    @Named("regimeContratoToString")
    default String regimeContratoToString(RegimeContrato regime) {
        return regime != null ? regime.getValor() : null;
    }
}
