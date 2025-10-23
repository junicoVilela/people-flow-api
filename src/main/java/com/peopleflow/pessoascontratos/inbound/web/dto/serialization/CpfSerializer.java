package com.peopleflow.pessoascontratos.inbound.web.dto.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.peopleflow.pessoascontratos.core.valueobject.Cpf;

import java.io.IOException;

public class CpfSerializer extends JsonSerializer<Cpf> {
    
    @Override
    public void serialize(Cpf value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            gen.writeString(value.getValor());
        } else {
            gen.writeNull();
        }
    }
}

