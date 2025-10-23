package com.peopleflow.pessoascontratos.inbound.web.dto.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.peopleflow.pessoascontratos.core.valueobject.Email;

import java.io.IOException;

public class EmailDeserializer extends JsonDeserializer<Email> {
    
    @Override
    public Email deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String value = parser.getValueAsString();
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return new Email(value);
    }
}

