package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoColaborador;
import com.peopleflow.pessoascontratos.core.ports.input.DocumentoColaboradorUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoColaboradorResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.DocumentoColaboradorWebMapper;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/colaboradores/{colaboradorId}/documentos")
@RequiredArgsConstructor
@Tag(name = "Documentos de Colaborador", description = "Gerenciamento de documentos digitais dos colaboradores")
public class DocumentoColaboradorController {

    private final DocumentoColaboradorUseCase documentoUseCase;
    private final DocumentoColaboradorWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:criar')")
    @Operation(
        summary = "Registrar documento",
        description = "Registra um novo documento para o colaborador. " +
                      "O arquivo deve ser enviado previamente ao storage e a storageKey informada aqui."
    )
    public ResponseEntity<DocumentoColaboradorResponse> adicionar(
            @PathVariable Long colaboradorId,
            @Valid @RequestBody DocumentoColaboradorRequest request) {

        DocumentoColaborador documento = mapper.toDomain(request);
        DocumentoColaborador salvo = documentoUseCase.adicionar(colaboradorId, documento);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salvo));
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Listar documentos do colaborador", description = "Retorna os documentos ativos do colaborador paginados")
    public ResponseEntity<PagedResult<DocumentoColaboradorResponse>> listar(
            @PathVariable Long colaboradorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pagination pagination = Pagination.of(page, size);
        PagedResult<DocumentoColaborador> resultado = documentoUseCase.listarPorColaborador(colaboradorId, pagination);
        return ResponseEntity.ok(PagedResult.map(resultado, mapper::toResponse));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar documento por ID", description = "Retorna um documento específico do colaborador")
    public ResponseEntity<DocumentoColaboradorResponse> buscarPorId(
            @PathVariable Long colaboradorId,
            @PathVariable Long id) {

        DocumentoColaborador documento = documentoUseCase.buscarPorId(colaboradorId, id);
        return ResponseEntity.ok(mapper.toResponse(documento));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:excluir')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir documento", description = "Exclui (soft delete) um documento do colaborador")
    public ResponseEntity<Void> excluir(
            @PathVariable Long colaboradorId,
            @PathVariable Long id) {

        documentoUseCase.excluir(colaboradorId, id);
        return ResponseEntity.noContent().build();
    }
}
