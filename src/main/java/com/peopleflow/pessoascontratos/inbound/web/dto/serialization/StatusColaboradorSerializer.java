package com.peopleflow.pessoascontratos.inbound.web.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;

import java.io.IOException;

public class StatusColaboradorSerializer extends JsonSerializer<StatusColaborador> {
    
    @Override
    public void serialize(StatusColaborador value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeString(value.getValor());
        } else {
            gen.writeNull();
        }
    }
}

