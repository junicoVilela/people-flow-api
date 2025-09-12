package com.peopleflow.pessoascontratos.core.model;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Person {

    public enum Status { ATIVO, INATIVO, BLOQUEADO }

    private UUID id;
    private String name;
    private String document;
    private Status status;
    private Instant createdAt;

    public Person() {
    }

    public Person(UUID id, String name, String document, Status status, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.document = document;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Person newPerson(String name, String document) {
        return new Person(UUID.randomUUID(), name, document, Status.ATIVO, Instant.now());
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getDocument() { return document; }
    public Status getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }

    public void setName(String name) { this.name = name; }
    public void setDocument(String document) { this.document = document; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
} 