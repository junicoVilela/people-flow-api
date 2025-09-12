package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.model.Person;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.PersonEntity;

public class PersonJpaMapper {

    public static Person toDomain(PersonEntity entity) {
        if (entity == null) return null;
        
        return new Person(
            entity.getId(),
            entity.getName(),
            entity.getDocument(),
            Person.Status.valueOf(entity.getStatus()),
            entity.getCreatedAt()
        );
    }

    public static PersonEntity toEntity(Person domain) {
        if (domain == null) return null;
        
        PersonEntity entity = new PersonEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setDocument(domain.getDocument());
        entity.setStatus(domain.getStatus().name());
        entity.setCreatedAt(domain.getCreatedAt());
        
        return entity;
    }
}