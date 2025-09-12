package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.model.Person;
import com.peopleflow.pessoascontratos.core.ports.in.PersonUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.PersonRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.PersonResponse;
import org.springframework.stereotype.Component;

@Component
public class PersonWebMapper {

    public PersonUseCase.CreateCommand toCreateCommand(PersonRequest req) {
        return new PersonUseCase.CreateCommand(req.getName(), req.getDocument());
    }

    public PersonUseCase.UpdateCommand toUpdateCommand(PersonRequest req) {
        return new PersonUseCase.UpdateCommand(req.getName(), req.getDocument());
    }

    public PersonResponse toResponse(Person p) {
        PersonResponse r = new PersonResponse();
        r.setId(p.getId());
        r.setName(p.getName());
        r.setDocument(p.getDocument());
        r.setStatus(p.getStatus().name());
        r.setCreatedAt(p.getCreatedAt());
        return r;
    }
}