package com.peopleflow.pessoascontratos.core.usecase;

import com.peopleflow.common.exception.ResourceNotFoundException;
import com.peopleflow.common.exception.ValidationException;
import com.peopleflow.pessoascontratos.core.model.Person;
import com.peopleflow.pessoascontratos.core.ports.in.PersonUseCase;
import com.peopleflow.pessoascontratos.core.ports.out.PersonRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PersonService implements PersonUseCase {

    private static final Logger log = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepositoryPort repository;

    public PersonService(PersonRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Person> list(int page, int size) {
        log.info("Listando pessoas - página: {}, tamanho: {}", page, size);
        
        if (page < 0) {
            throw new ValidationException("Página deve ser maior ou igual a zero");
        }
        if (size <= 0 || size > 100) {
            throw new ValidationException("Tamanho deve estar entre 1 e 100");
        }
        
        return repository.findAll(page, size);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Person> get(UUID id) {
        log.info("Buscando pessoa com ID: {}", id);
        
        if (id == null) {
            throw new ValidationException("ID é obrigatório");
        }
        
        return repository.findById(id);
    }

    @Override
    public Person create(CreateCommand cmd) {
        log.info("Criando pessoa com nome: {}", cmd.name());
        
        validateCreateCommand(cmd);
        
        if (cmd.document() != null && !cmd.document().isBlank() && repository.existsByDocument(cmd.document())) {
            throw new ValidationException("Documento já existe no sistema");
        }
        
        Person person = Person.newPerson(cmd.name().trim(), cmd.document() == null ? null : cmd.document().trim());
        Person saved = repository.save(person);
        
        log.info("Pessoa criada com sucesso - ID: {}", saved.getId());
        return saved;
    }

    @Override
    public Person update(UUID id, UpdateCommand cmd) {
        log.info("Atualizando pessoa com ID: {}", id);
        
        if (id == null) {
            throw new ValidationException("ID é obrigatório");
        }
        
        validateUpdateCommand(cmd);
        
        Person existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa", id));
        
        if (cmd.name() != null && !cmd.name().isBlank()) {
            existing.setName(cmd.name().trim());
        }
        
        if (cmd.document() != null) {
            String newDoc = cmd.document().trim();
            if (!newDoc.equals(existing.getDocument()) && repository.existsByDocument(newDoc)) {
                throw new ValidationException("Documento já existe no sistema");
            }
            existing.setDocument(newDoc);
        }
        
        Person updated = repository.save(existing);
        log.info("Pessoa atualizada com sucesso - ID: {}", updated.getId());
        return updated;
    }

    @Override
    public void delete(UUID id) {
        log.info("Deletando pessoa com ID: {}", id);
        
        if (id == null) {
            throw new ValidationException("ID é obrigatório");
        }
        
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Pessoa", id);
        }
        
        repository.deleteById(id);
        log.info("Pessoa deletada com sucesso - ID: {}", id);
    }

    private void validateCreateCommand(CreateCommand cmd) {
        if (cmd == null) {
            throw new ValidationException("Dados da pessoa são obrigatórios");
        }
        if (cmd.name() == null || cmd.name().isBlank()) {
            throw new ValidationException("Nome é obrigatório");
        }
        if (cmd.name().trim().length() < 2) {
            throw new ValidationException("Nome deve ter pelo menos 2 caracteres");
        }
        if (cmd.name().trim().length() > 100) {
            throw new ValidationException("Nome deve ter no máximo 100 caracteres");
        }
    }

    private void validateUpdateCommand(UpdateCommand cmd) {
        if (cmd == null) {
            throw new ValidationException("Dados da atualização são obrigatórios");
        }
        if (cmd.name() != null && cmd.name().trim().length() < 2) {
            throw new ValidationException("Nome deve ter pelo menos 2 caracteres");
        }
        if (cmd.name() != null && cmd.name().trim().length() > 100) {
            throw new ValidationException("Nome deve ter no máximo 100 caracteres");
        }
    }
} 