package com.peopleflow.pessoascontratos.core.ports.in;

import com.peopleflow.pessoascontratos.core.model.Person;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PersonUseCase {

    class CreateCommand {
        private final String name;
        private final String document;
        
        public CreateCommand(String name, String document) {
            this.name = name;
            this.document = document;
        }
        
        public String name() { return name; }
        public String document() { return document; }
    }
    
    class UpdateCommand {
        private final String name;
        private final String document;
        
        public UpdateCommand(String name, String document) {
            this.name = name;
            this.document = document;
        }
        
        public String name() { return name; }
        public String document() { return document; }
    }

    List<Person> list(int page, int size);

    Optional<Person> get(UUID id);

    Person create(CreateCommand cmd);

    Person update(UUID id, UpdateCommand cmd);

    void delete(UUID id);
} 