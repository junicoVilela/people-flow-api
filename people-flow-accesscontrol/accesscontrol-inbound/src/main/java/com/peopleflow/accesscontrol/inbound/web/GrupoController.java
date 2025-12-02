package com.peopleflow.accesscontrol.inbound.web;

import com.peopleflow.accesscontrol.core.application.GrupoService;
import com.peopleflow.accesscontrol.inbound.web.dto.CreateGrupoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/grupos")
@RequiredArgsConstructor
@Tag(name = "Grupos Keycloak", description = "Gerenciamento de grupos do Keycloak")
public class GrupoController {

    private final GrupoService grupoService;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Criar novo grupo", description = "Cria um novo grupo no Keycloak")
    public ResponseEntity<Map<String, String>> criar(@Valid @RequestBody CreateGrupoRequest request) {
        String groupId = grupoService.criar(request.getName());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("groupId", groupId, "name", request.getName()));
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Listar todos os grupos", description = "Lista todos os grupos do realm")
    public ResponseEntity<List<Map<String, Object>>> listar() {
        List<Map<String, Object>> grupos = grupoService.listarTodos();
        return ResponseEntity.ok(grupos);
    }

    @GetMapping("/{groupId}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Buscar grupo por ID", description = "Busca grupo específico por ID")
    public ResponseEntity<Map<String, Object>> buscar(@PathVariable String groupId) {
        Map<String, Object> grupo = grupoService.buscarPorId(groupId);
        return ResponseEntity.ok(grupo);
    }

    @DeleteMapping("/{groupId}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Deletar grupo", description = "Remove grupo do Keycloak")
    public ResponseEntity<Void> deletar(@PathVariable String groupId) {
        grupoService.deletar(groupId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{groupId}/membros")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Listar membros do grupo", description = "Lista todos os membros de um grupo")
    public ResponseEntity<List<Map<String, Object>>> listarMembros(@PathVariable String groupId) {
        List<Map<String, Object>> membros = grupoService.listarMembros(groupId);
        return ResponseEntity.ok(membros);
    }
}

