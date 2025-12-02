package com.peopleflow.accesscontrol.inbound.web;

import com.peopleflow.accesscontrol.core.application.RoleService;
import com.peopleflow.accesscontrol.inbound.web.dto.CreateRoleRequest;
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
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@Tag(name = "Roles Keycloak", description = "Gerenciamento de roles do Keycloak")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Criar nova role", description = "Cria uma nova role no client peopleflow-api")
    public ResponseEntity<Map<String, String>> criar(@Valid @RequestBody CreateRoleRequest request) {
        roleService.criar(request.getName(), request.getDescription());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("name", request.getName()));
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Listar todas as roles", description = "Lista todas as roles do client")
    public ResponseEntity<List<Map<String, Object>>> listar() {
        List<Map<String, Object>> roles = roleService.listarTodas();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{roleName}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Buscar role por nome", description = "Busca role específica por nome")
    public ResponseEntity<Map<String, Object>> buscar(@PathVariable String roleName) {
        Map<String, Object> role = roleService.buscarPorNome(roleName);
        
        return role != null ? 
                ResponseEntity.ok(role) : 
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{roleName}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Deletar role", description = "Remove role do client")
    public ResponseEntity<Void> deletar(@PathVariable String roleName) {
        roleService.deletar(roleName);
        return ResponseEntity.noContent().build();
    }
}

