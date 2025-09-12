package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.pessoascontratos.core.model.Person;
import com.peopleflow.pessoascontratos.core.ports.in.PersonUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.PersonRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.PersonResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.PersonWebMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/people")
public class PeopleController {

    private static final Logger log = LoggerFactory.getLogger(PeopleController.class);
    private final PersonUseCase useCase;
    private final PersonWebMapper mapper;

    public PeopleController(PersonUseCase useCase, PersonWebMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> list(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        log.info("Listando pessoas - página: {}, tamanho: {}", page, size);
        
        List<Person> people = useCase.list(page, size);
        List<PersonResponse> body = people.stream().map(mapper::toResponse).toList();
        
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> get(@PathVariable UUID id) {
        log.info("Buscando pessoa com ID: {}", id);
        
        return useCase.get(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@Valid @RequestBody PersonRequest req) {
        log.info("Criando nova pessoa");
        
        Person created = useCase.create(mapper.toCreateCommand(req));
        PersonResponse response = mapper.toResponse(created);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> update(@PathVariable UUID id,
                                                 @Valid @RequestBody PersonRequest req) {
        log.info("Atualizando pessoa com ID: {}", id);
        
        Person updated = useCase.update(id, mapper.toUpdateCommand(req));
        PersonResponse response = mapper.toResponse(updated);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Deletando pessoa com ID: {}", id);
        
        useCase.delete(id);
        return ResponseEntity.noContent().build();
    }
} 