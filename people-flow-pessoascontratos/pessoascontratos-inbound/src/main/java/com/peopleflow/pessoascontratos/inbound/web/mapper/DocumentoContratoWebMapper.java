package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.DocumentoContrato;
import com.peopleflow.pessoascontratos.core.query.DocumentoContratoFilter;
import com.peopleflow.pessoascontratos.core.valueobject.TipoDocumento;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoContratoFilterRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoContratoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoContratoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentoContratoWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contratoId", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "stringToTipo")
    DocumentoContrato toDomain(DocumentoContratoRequest request);

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoToString")
    DocumentoContratoResponse toResponse(DocumentoContrato documento);

    DocumentoContratoFilter toDomain(DocumentoContratoFilterRequest request);

    @Named("stringToTipo")
    default TipoDocumento stringToTipo(String tipo) {
        return tipo != null ? TipoDocumento.of(tipo) : null;
    }

    @Named("tipoToString")
    default String tipoToString(TipoDocumento tipo) {
        return tipo != null ? tipo.getValor() : null;
    }
}
