package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.DocumentoColaborador;
import com.peopleflow.pessoascontratos.core.query.DocumentoColaboradorFilter;
import com.peopleflow.pessoascontratos.core.valueobject.TipoDocumento;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoColaboradorFilterRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoColaboradorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentoColaboradorWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colaboradorId", ignore = true)
    @Mapping(target = "versao", ignore = true)
    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "stringToTipo")
    DocumentoColaborador toDomain(DocumentoColaboradorRequest request);

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoToString")
    DocumentoColaboradorResponse toResponse(DocumentoColaborador documento);

    DocumentoColaboradorFilter toDomain(DocumentoColaboradorFilterRequest request);

    @Named("stringToTipo")
    default TipoDocumento stringToTipo(String tipo) {
        return tipo != null ? TipoDocumento.of(tipo) : null;
    }

    @Named("tipoToString")
    default String tipoToString(TipoDocumento tipo) {
        return tipo != null ? tipo.getValor() : null;
    }
}
