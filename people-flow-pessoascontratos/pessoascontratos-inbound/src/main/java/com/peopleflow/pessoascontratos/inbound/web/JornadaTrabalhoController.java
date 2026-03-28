package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.pessoascontratos.core.domain.JornadaTrabalho;
import com.peopleflow.pessoascontratos.core.ports.input.JornadaTrabalhoUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.JornadaTrabalhoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.JornadaTrabalhoResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.JornadaTrabalhoWebMapper;
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
@RequestMapping("/api/v1/jornadas-trabalho")
@RequiredArgsConstructor
@Tag(name = "Jornadas de trabalho", description = "Cadastro de jornadas")
public class JornadaTrabalhoController {

    private final JornadaTrabalhoUseCase useCase;
    private final JornadaTrabalhoWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Criar jornada")
    public ResponseEntity<JornadaTrabalhoResponse> criar(@Valid @RequestBody JornadaTrabalhoRequest request) {
        JornadaTrabalho criado = useCase.criar(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(criado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Atualizar jornada")
    public ResponseEntity<JornadaTrabalhoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody JornadaTrabalhoRequest request) {
        return ResponseEntity.ok(mapper.toResponse(useCase.atualizar(id, mapper.toDomain(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar jornada por ID")
    public ResponseEntity<JornadaTrabalhoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(useCase.buscarPorId(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Listar jornadas ativas")
    public ResponseEntity<List<JornadaTrabalhoResponse>> listar() {
        return ResponseEntity.ok(useCase.listarTodos().stream().map(mapper::toResponse).toList());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:deletar')")
    @Operation(summary = "Excluir jornada (soft delete)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        useCase.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
