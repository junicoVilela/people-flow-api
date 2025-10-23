package com.peopleflow.pessoascontratos.inbound.web.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.peopleflow.pessoascontratos.core.valueobject.Email;

import java.io.IOException;

public class EmailSerializer extends JsonSerializer<Email> {
    
    @Override
    public void serialize(Email value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeString(value.getValor());
        } else {
            gen.writeNull();
        }
    }
}

