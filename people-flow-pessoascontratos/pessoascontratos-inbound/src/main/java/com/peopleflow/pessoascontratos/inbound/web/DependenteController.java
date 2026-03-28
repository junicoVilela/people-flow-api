package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Dependente;
import com.peopleflow.pessoascontratos.core.ports.input.DependenteUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.DependenteRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.DependenteResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.DependenteWebMapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/colaboradores/{colaboradorId}/dependentes")
@RequiredArgsConstructor
@Tag(name = "Dependentes", description = "Dependentes do colaborador")
public class DependenteController {

    private final DependenteUseCase useCase;
    private final DependenteWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Incluir dependente")
    public ResponseEntity<DependenteResponse> adicionar(
            @PathVariable Long colaboradorId,
            @Valid @RequestBody DependenteRequest request) {
        Dependente salvo = useCase.adicionar(colaboradorId, mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salvo));
    }

    @PutMapping("/{dependenteId}")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Atualizar dependente")
    public ResponseEntity<DependenteResponse> atualizar(
            @PathVariable Long colaboradorId,
            @PathVariable Long dependenteId,
            @Valid @RequestBody DependenteRequest request) {
        return ResponseEntity.ok(mapper.toResponse(useCase.atualizar(colaboradorId, dependenteId, mapper.toDomain(request))));
    }

    @GetMapping("/{dependenteId}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar dependente por ID")
    public ResponseEntity<DependenteResponse> buscarPorId(
            @PathVariable Long colaboradorId,
            @PathVariable Long dependenteId) {
        return ResponseEntity.ok(mapper.toResponse(useCase.buscarPorId(colaboradorId, dependenteId)));
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Listar dependentes do colaborador")
    public ResponseEntity<PagedResult<DependenteResponse>> listar(
            @PathVariable Long colaboradorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pagination pagination = Pagination.of(page, size);
        PagedResult<Dependente> resultado = useCase.listarPorColaborador(colaboradorId, pagination);
        return ResponseEntity.ok(PagedResult.map(resultado, mapper::toResponse));
    }

    @DeleteMapping("/{dependenteId}")
    @PreAuthorize("hasRole('colaborador:deletar')")
    @Operation(summary = "Excluir dependente (soft delete)")
    public ResponseEntity<Void> excluir(
            @PathVariable Long colaboradorId,
            @PathVariable Long dependenteId) {
        useCase.excluir(colaboradorId, dependenteId);
        return ResponseEntity.noContent().build();
    }
}
