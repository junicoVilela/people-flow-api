package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.common.pagination.Pagination;
import com.peopleflow.pessoascontratos.core.domain.DocumentoContrato;
import com.peopleflow.pessoascontratos.core.ports.input.DocumentoContratoUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoContratoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.DocumentoContratoResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.DocumentoContratoWebMapper;
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
@RequestMapping("/api/v1/contratos/{contratoId}/documentos")
@RequiredArgsConstructor
@Tag(name = "Documentos de contrato", description = "Documentos digitais vinculados ao contrato de trabalho")
public class DocumentoContratoController {

    private final DocumentoContratoUseCase documentoContratoUseCase;
    private final DocumentoContratoWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('colaborador:criar')")
    @Operation(
            summary = "Registrar documento do contrato",
            description = "Registra um novo documento para o contrato. O arquivo deve ser enviado ao storage e a storageKey informada aqui."
    )
    public ResponseEntity<DocumentoContratoResponse> adicionar(
            @PathVariable Long contratoId,
            @Valid @RequestBody DocumentoContratoRequest request) {

        DocumentoContrato documento = mapper.toDomain(request);
        DocumentoContrato salvo = documentoContratoUseCase.adicionar(contratoId, documento);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(salvo));
    }

    @GetMapping
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Listar documentos do contrato", description = "Lista documentos ativos do contrato (paginado)")
    public ResponseEntity<PagedResult<DocumentoContratoResponse>> listar(
            @PathVariable Long contratoId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pagination pagination = new Pagination(page, size, null, null);
        PagedResult<DocumentoContrato> resultado = documentoContratoUseCase.listarPorContrato(contratoId, pagination);

        PagedResult<DocumentoContratoResponse> response = new PagedResult<>(
                resultado.content().stream().map(mapper::toResponse).toList(),
                resultado.totalElements(),
                resultado.totalPages(),
                resultado.page(),
                resultado.size()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:ler')")
    @Operation(summary = "Buscar documento por ID", description = "Retorna um documento específico do contrato")
    public ResponseEntity<DocumentoContratoResponse> buscarPorId(
            @PathVariable Long contratoId,
            @PathVariable Long id) {

        DocumentoContrato documento = documentoContratoUseCase.buscarPorId(contratoId, id);
        return ResponseEntity.ok(mapper.toResponse(documento));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('colaborador:excluir')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir documento do contrato", description = "Exclui (soft delete) o documento")
    public ResponseEntity<Void> excluir(
            @PathVariable Long contratoId,
            @PathVariable Long id) {

        documentoContratoUseCase.excluir(contratoId, id);
        return ResponseEntity.noContent().build();
    }
}
