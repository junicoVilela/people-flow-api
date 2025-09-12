package com.peopleflow.pessoascontratos.core.ports.out;

import com.peopleflow.pessoascontratos.core.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonRepositoryPort {

    List<Person> findAll(int page, int size);

    Optional<Person> findById(UUID id);

    Person save(Person person);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    boolean existsByDocument(String document);
}