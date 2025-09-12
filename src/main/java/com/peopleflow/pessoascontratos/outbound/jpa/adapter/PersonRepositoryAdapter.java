package com.peopleflow.pessoascontratos.outbound.jpa.adapter;

import com.peopleflow.pessoascontratos.core.model.Person;
import com.peopleflow.pessoascontratos.core.ports.out.PersonRepositoryPort;
import com.peopleflow.pessoascontratos.outbound.jpa.mapper.PersonJpaMapper;
import com.peopleflow.pessoascontratos.outbound.jpa.repository.PersonJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PersonRepositoryAdapter implements PersonRepositoryPort {

    private final PersonJpaRepository repository;

    public PersonRepositoryAdapter(PersonJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Person> findAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
                .map(PersonJpaMapper::toDomain)
                .getContent();
    }

    @Override
    public Optional<Person> findById(UUID id) {
        return repository.findById(id).map(PersonJpaMapper::toDomain);
    }

    @Override
    public Person save(Person person) {
        return PersonJpaMapper.toDomain(repository.save(PersonJpaMapper.toEntity(person)));
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByDocument(String document) {
        if (document == null || document.isBlank()) return false;
        return repository.existsByDocument(document);
    }
} 