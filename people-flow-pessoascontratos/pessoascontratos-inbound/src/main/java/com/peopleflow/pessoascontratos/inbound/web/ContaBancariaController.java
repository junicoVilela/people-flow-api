package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.common.pagination.PageablePagination;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.ContaBancaria;
import com.peopleflow.pessoascontratos.core.ports.input.ContaBancariaUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContaBancariaFilterRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContaBancariaRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContaBancariaResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.ContaBancariaWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/colaboradores/{colaboradorId}/contas-bancarias")
@RequiredArgsConstructor
@Tag(name = "Contas bancárias", description = "Dados bancários do colaborador")
public class ContaBancariaController {

    private final ContaBancariaUseCase useCase;
    private final ContaBancariaWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Incluir conta bancária")
    public ResponseEntity<ContaBancariaResponse> adicionar(
            @PathVariable Long colaboradorId,
            @Valid @RequestBody ContaBancariaRequest request) {
        ContaBancaria salva = useCase.adicionar(colaboradorId, mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salva));
    }

    @PutMapping("/{contaId}")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Atualizar conta bancária")
    public ResponseEntity<ContaBancariaResponse> atualizar(
            @PathVariable Long colaboradorId,
            @PathVariable Long contaId,
            @Valid @RequestBody ContaBancariaRequest request) {
        return ResponseEntity.ok(mapper.toResponse(useCase.atualizar(colaboradorId, contaId, mapper.toDomain(request))));
    }

    @GetMapping("/{contaId}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar conta por ID")
    public ResponseEntity<ContaBancariaResponse> buscarPorId(
            @PathVariable Long colaboradorId,
            @PathVariable Long contaId) {
        return ResponseEntity.ok(mapper.toResponse(useCase.buscarPorId(colaboradorId, contaId)));
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(
            summary = "Buscar contas com filtros opcionais e paginação",
            description = "Filtros opcionais: banco, agencia, conta. Paginação Spring Data; padrão: banco ascendente."
    )
    public ResponseEntity<PagedResult<ContaBancariaResponse>> buscarPorFiltros(
            @PathVariable Long colaboradorId,
            @ModelAttribute ContaBancariaFilterRequest filtrosRequest,
            @PageableDefault(size = Pagination.DEFAULT_PAGE_SIZE, sort = "banco") Pageable pageable) {
        Pagination pagination = PageablePagination.from(pageable);
        PagedResult<ContaBancaria> resultado = useCase.buscarPorFiltros(colaboradorId, mapper.toDomain(filtrosRequest), pagination);
        return ResponseEntity.ok(PagedResult.map(resultado, mapper::toResponse));
    }

    @DeleteMapping("/{contaId}")
    @PreAuthorize("hasRole('colaborador:deletar')")
    @Operation(summary = "Excluir conta (soft delete)")
    public ResponseEntity<Void> excluir(
            @PathVariable Long colaboradorId,
            @PathVariable Long contaId) {
        useCase.excluir(colaboradorId, contaId);
        return ResponseEntity.noContent().build();
    }
}
