package com.peopleflow.pessoascontratos.inbound.web.dto.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;

import java.io.IOException;

public class StatusColaboradorDeserializer extends JsonDeserializer<StatusColaborador> {
    
    @Override
    public StatusColaborador deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        if (value == null || value.trim().isEmpty()) {
            return StatusColaborador.ATIVO; // Default
        }
        return StatusColaborador.of(value);
    }
}

