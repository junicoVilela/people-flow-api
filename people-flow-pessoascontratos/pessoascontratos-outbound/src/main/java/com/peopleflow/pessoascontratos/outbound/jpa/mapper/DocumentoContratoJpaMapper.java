package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.DocumentoContrato;
import com.peopleflow.pessoascontratos.core.valueobject.TipoDocumento;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.DocumentoContratoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DocumentoContratoJpaMapper {

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "stringToTipo")
    DocumentoContrato toDomain(DocumentoContratoEntity entity);

    @Mapping(target = "tipo", source = "tipo", qualifiedByName = "tipoToString")
    DocumentoContratoEntity toEntity(DocumentoContrato documento);

    @Named("stringToTipo")
    default TipoDocumento stringToTipo(String tipo) {
        return tipo != null ? TipoDocumento.of(tipo) : null;
    }

    @Named("tipoToString")
    default String tipoToString(TipoDocumento tipo) {
        return tipo != null ? tipo.getValor() : null;
    }
}
