package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.in.ColaboradorUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.ColaboradorWebMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/colaboradores")
public class ColaboradorController {

    private final ColaboradorUseCase colaboradorUseCase;
    private final ColaboradorWebMapper mapper;

    public ColaboradorController(ColaboradorUseCase colaboradorUseCase, ColaboradorWebMapper mapper) {
        this.colaboradorUseCase = colaboradorUseCase;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ColaboradorResponse> criar(@Valid @RequestBody ColaboradorRequest request) {
        Colaborador colaborador = mapper.toDomain(request);
        Colaborador colaboradorCriado = colaboradorUseCase.criar(colaborador);
        ColaboradorResponse response = mapper.toResponse(colaboradorCriado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> buscarPorId(@PathVariable UUID id) {
        Colaborador colaborador = colaboradorUseCase.buscarPorId(id);
        ColaboradorResponse response = mapper.toResponse(colaborador);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ColaboradorResponse>> listarTodos() {
        List<Colaborador> colaboradores = colaboradorUseCase.listarTodos();
        List<ColaboradorResponse> responses = colaboradores.stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorResponse> atualizar(@PathVariable UUID id, @Valid @RequestBody ColaboradorRequest request) {
        Colaborador colaborador = mapper.toDomain(request);
        Colaborador colaboradorAtualizado = colaboradorUseCase.atualizar(id, colaborador);
        ColaboradorResponse response = mapper.toResponse(colaboradorAtualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        colaboradorUseCase.deletar(id);
        return ResponseEntity.noContent().build();
    }
} 