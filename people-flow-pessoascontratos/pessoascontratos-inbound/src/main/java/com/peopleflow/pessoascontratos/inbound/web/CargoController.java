package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Cargo;
import com.peopleflow.pessoascontratos.core.ports.input.CargoUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.CargoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.CargoResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.CargoWebMapper;
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
@RequestMapping("/api/v1/cargos")
@RequiredArgsConstructor
@Tag(name = "Cargos", description = "Cadastro de cargos")
public class CargoController {

    private final CargoUseCase useCase;
    private final CargoWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Criar cargo")
    public ResponseEntity<CargoResponse> criar(@Valid @RequestBody CargoRequest request) {
        Cargo criado = useCase.criar(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(criado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Atualizar cargo")
    public ResponseEntity<CargoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody CargoRequest request) {
        return ResponseEntity.ok(mapper.toResponse(useCase.atualizar(id, mapper.toDomain(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar cargo por ID")
    public ResponseEntity<CargoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(useCase.buscarPorId(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Listar cargos ativos (paginado)")
    public ResponseEntity<PagedResult<CargoResponse>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pagination pagination = new Pagination(page, size, "nome", Pagination.SortDirection.ASC);
        PagedResult<Cargo> resultado = useCase.listar(pagination);
        return ResponseEntity.ok(PagedResult.map(resultado, mapper::toResponse));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:deletar')")
    @Operation(summary = "Excluir cargo (soft delete)")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        useCase.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
