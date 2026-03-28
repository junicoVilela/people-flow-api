package com.peopleflow.pessoascontratos.inbound.web;

import com.peopleflow.pessoascontratos.core.domain.NivelHierarquico;
import com.peopleflow.pessoascontratos.core.ports.input.NivelHierarquicoUseCase;
import com.peopleflow.pessoascontratos.inbound.web.dto.NivelHierarquicoRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.NivelHierarquicoResponse;
import com.peopleflow.pessoascontratos.inbound.web.mapper.NivelHierarquicoWebMapper;
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
@RequestMapping("/api/v1/niveis-hierarquicos")
@RequiredArgsConstructor
@Tag(name = "Níveis hierárquicos", description = "Cadastro de níveis hierárquicos de cargo (ex.: Júnior, Pleno, Sênior)")
public class NivelHierarquicoController {

    private final NivelHierarquicoUseCase nivelHierarquicoUseCase;
    private final NivelHierarquicoWebMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('nivel_hierarquico:criar')")
    @Operation(summary = "Criar nível hierárquico", description = "Cadastra um novo nível hierárquico")
    public ResponseEntity<NivelHierarquicoResponse> criar(@Valid @RequestBody NivelHierarquicoRequest request) {
        NivelHierarquico criado = nivelHierarquicoUseCase.adicionar(mapper.toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(criado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('nivel_hierarquico:editar')")
    @Operation(summary = "Atualizar nível hierárquico", description = "Atualiza nome, descrição e ordem de exibição/senioridade")
    public ResponseEntity<NivelHierarquicoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody NivelHierarquicoRequest request) {
        NivelHierarquico atualizado = nivelHierarquicoUseCase.atualizar(id, mapper.toDomain(request));
        return ResponseEntity.ok(mapper.toResponse(atualizado));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('nivel_hierarquico:ler')")
    @Operation(summary = "Buscar nível hierárquico por ID", description = "Retorna um nível hierárquico pelo identificador")
    public ResponseEntity<NivelHierarquicoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mapper.toResponse(nivelHierarquicoUseCase.buscarPorId(id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('nivel_hierarquico:ler')")
    @Operation(
            summary = "Listar níveis hierárquicos",
            description = "Lista todos os níveis ativos, ordenados pelo campo ordem (senioridade crescente)"
    )
    public ResponseEntity<List<NivelHierarquicoResponse>> listar() {
        List<NivelHierarquicoResponse> lista = nivelHierarquicoUseCase.listarTodos().stream()
                .map(mapper::toResponse)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('nivel_hierarquico:deletar')")
    @Operation(
            summary = "Excluir nível hierárquico",
            description = "Soft delete. Não permitido se existirem cargos ativos utilizando este nível."
    )
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        nivelHierarquicoUseCase.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
