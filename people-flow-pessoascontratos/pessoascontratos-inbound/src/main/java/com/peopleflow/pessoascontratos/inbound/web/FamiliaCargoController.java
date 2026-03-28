package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.pessoascontratos.core.domain.FamiliaCargo;
import com.peopleflow.pessoascontratos.core.ports.input.FamiliaCargoUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.FamiliaCargoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.FamiliaCargoResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.FamiliaCargoWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/familias-cargo")
@RequiredArgsConstructor
@Tag(name = "Famílias de cargo", description = "Cadastro de famílias de cargo")
public class FamiliaCargoController {

    private final FamiliaCargoUseCase useCase;
    private final FamiliaCargoWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Criar família de cargo")
    public ResponseEntity<FamiliaCargoResponse> criar(@Valid @RequestBody FamiliaCargoRequest request) {
        FamiliaCargo criado = useCase.adicionar(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(criado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Atualizar família de cargo")
    public ResponseEntity<FamiliaCargoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FamiliaCargoRequest request) {
        return ResponseEntity.ok(mapper.toResponse(useCase.atualizar(id, mapper.toDomain(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar família por ID")
    public ResponseEntity<FamiliaCargoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(useCase.buscarPorId(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Listar famílias ativas")
    public ResponseEntity<List<FamiliaCargoResponse>> listar() {
        return ResponseEntity.ok(useCase.listarTodos().stream().map(mapper::toResponse).toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:deletar')")
    @Operation(summary = "Excluir família (soft delete)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        useCase.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
