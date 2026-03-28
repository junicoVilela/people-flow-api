package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.Contrato;
import com.peopleflow.pessoascontratos.core.query.ContratoFilter;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContratoFilterRequest;
import com.peopleflow.pessoascontratos.core.valueobject.RegimeContrato;
import com.peopleflow.pessoascontratos.core.valueobject.TipoContrato;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContratoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContratoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ContratoWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colaboradorId", ignore = true)
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "requestTipoToEnum")
    @Mapping(target = "regime", source = "regime", qualifiedByName = "requestRegimeToEnum")
    Contrato toDomain(ContratoRequest request);

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoToResponseString")
    @Mapping(target = "regime", source = "regime", qualifiedByName = "regimeToResponseString")
    ContratoResponse toResponse(Contrato contrato);

    ContratoFilter toDomain(ContratoFilterRequest request);

    @Named("requestTipoToEnum")
    default TipoContrato requestTipoToEnum(String tipo) {
        return TipoContrato.ofNullable(tipo);
    }

    @Named("requestRegimeToEnum")
    default RegimeContrato requestRegimeToEnum(String regime) {
        return RegimeContrato.ofNullable(regime);
    }

    @Named("tipoToResponseString")
    default String tipoToResponseString(TipoContrato tipo) {
        return tipo != null ? tipo.getValor() : null;
    }

    @Named("regimeToResponseString")
    default String regimeToResponseString(RegimeContrato regime) {
        return regime != null ? regime.getValor() : null;
    }
}
