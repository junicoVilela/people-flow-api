package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.Contrato;
import com.peopleflow.pessoascontratos.core.ports.input.ContratoUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContratoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ContratoResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.ContratoWebMapper;
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
@RequestMapping("/api/v1/colaboradores/{colaboradorId}/contratos")
@RequiredArgsConstructor
@Tag(name = "Contratos", description = "Contratos de trabalho dos colaboradores")
public class ContratoController {

    private final ContratoUseCase contratoUseCase;
    private final ContratoWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Criar contrato", description = "Cadastra um novo contrato para o colaborador")
    public ResponseEntity<ContratoResponse> criar(
            @PathVariable Long colaboradorId,
            @Valid @RequestBody ContratoRequest request) {

        Contrato criado = contratoUseCase.criar(colaboradorId, mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(criado));
    }

    @PutMapping("/{contratoId}")
    @PreAuthorize("hasRole('colaborador:editar')")
    @Operation(summary = "Atualizar contrato", description = "Atualiza dados do contrato")
    public ResponseEntity<ContratoResponse> atualizar(
            @PathVariable Long colaboradorId,
            @PathVariable Long contratoId,
            @Valid @RequestBody ContratoRequest request) {

        Contrato salvo = contratoUseCase.atualizar(colaboradorId, contratoId, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(salvo));
    }

    @GetMapping("/{contratoId}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar contrato por ID", description = "Retorna o contrato dentro do escopo do colaborador")
    public ResponseEntity<ContratoResponse> buscarPorId(
            @PathVariable Long colaboradorId,
            @PathVariable Long contratoId) {

        Contrato contrato = contratoUseCase.buscarPorId(colaboradorId, contratoId);
        return ResponseEntity.ok(mapper.toResponse(contrato));
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Listar contratos do colaborador", description = "Lista contratos ativos, paginados")
    public ResponseEntity<PagedResult<ContratoResponse>> listar(
            @PathVariable Long colaboradorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pagination pagination = Pagination.of(page, size);
        PagedResult<Contrato> resultado = contratoUseCase.listarPorColaborador(colaboradorId, pagination);
        return ResponseEntity.ok(PagedResult.map(resultado, mapper::toResponse));
    }

    @DeleteMapping("/{contratoId}")
    @PreAuthorize("hasRole('colaborador:deletar')")
    @Operation(
            summary = "Excluir contrato",
            description = "Soft delete. Bloqueado se houver documentos ativos no contrato."
    )
    public ResponseEntity<Void> excluir(
            @PathVariable Long colaboradorId,
            @PathVariable Long contratoId) {

        contratoUseCase.excluir(colaboradorId, contratoId);
        return ResponseEntity.noContent().build();
    }
}
