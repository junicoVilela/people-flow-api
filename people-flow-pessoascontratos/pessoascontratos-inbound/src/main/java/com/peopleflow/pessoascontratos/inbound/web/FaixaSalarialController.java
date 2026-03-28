package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.FaixaSalarial;
import com.peopleflow.pessoascontratos.core.ports.input.FaixaSalarialUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.FaixaSalarialRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.FaixaSalarialResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.FaixaSalarialWebMapper;
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
@RequestMapping("/api/v1/cargos/{cargoId}/faixas-salariais")
@RequiredArgsConstructor
@Tag(name = "Faixas salariais", description = "Faixas salariais por cargo")
public class FaixaSalarialController {

    private final FaixaSalarialUseCase useCase;
    private final FaixaSalarialWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Criar faixa salarial")
    public ResponseEntity<FaixaSalarialResponse> adicionar(
            @PathVariable Long cargoId,
            @Valid @RequestBody FaixaSalarialRequest request) {
        FaixaSalarial salva = useCase.adicionar(cargoId, mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salva));
    }

    @PutMapping("/{faixaId}")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Atualizar faixa salarial")
    public ResponseEntity<FaixaSalarialResponse> atualizar(
            @PathVariable Long cargoId,
            @PathVariable Long faixaId,
            @Valid @RequestBody FaixaSalarialRequest request) {
        return ResponseEntity.ok(mapper.toResponse(useCase.atualizar(cargoId, faixaId, mapper.toDomain(request))));
    }

    @GetMapping("/{faixaId}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar faixa por ID")
    public ResponseEntity<FaixaSalarialResponse> buscarPorId(
            @PathVariable Long cargoId,
            @PathVariable Long faixaId) {
        return ResponseEntity.ok(mapper.toResponse(useCase.buscarPorId(cargoId, faixaId)));
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Listar faixas do cargo")
    public ResponseEntity<PagedResult<FaixaSalarialResponse>> listar(
            @PathVariable Long cargoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pagination pagination = Pagination.of(page, size);
        PagedResult<FaixaSalarial> resultado = useCase.listarPorCargo(cargoId, pagination);
        return ResponseEntity.ok(PagedResult.map(resultado, mapper::toResponse));
    }

    @DeleteMapping("/{faixaId}")
    @PreAuthorize("hasRole('colaborador:deletar')")
    @Operation(summary = "Excluir faixa (soft delete)")
    public ResponseEntity<Void> excluir(@PathVariable Long cargoId, @PathVariable Long faixaId) {
        useCase.excluir(cargoId, faixaId);
        return ResponseEntity.noContent().build();
    }
}
