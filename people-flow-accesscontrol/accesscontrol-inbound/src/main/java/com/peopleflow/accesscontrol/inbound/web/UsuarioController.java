package com.peopleflow.accesscontrol.inbound.web;

import com.peopleflow.accesscontrol.core.application.UsuarioService;
import com.peopleflow.accesscontrol.inbound.web.dto.CreateUsuarioRequest;
import com.peopleflow.accesscontrol.inbound.web.dto.ResetSenhaRequest;
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
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários Keycloak", description = "Gerenciamento de usuários do Keycloak")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Criar novo usuário", description = "Cria um novo usuário no Keycloak")
    public ResponseEntity<Map<String, String>> criar(@Valid @RequestBody CreateUsuarioRequest request) {
        
        String userId = usuarioService.criar(
            request.getUsername(),
            request.getEmail(),
            request.getFirstName(),
            request.getLastName(),
            request.getPassword(),
            request.getAttributes()
        );
        
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("userId", userId, "username", request.getUsername()));
    }

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Listar todos os usuários", description = "Lista todos os usuários do realm")
    public ResponseEntity<List<Map<String, Object>>> listar() {
        List<Map<String, Object>> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Buscar usuário por username", description = "Busca usuário específico por username")
    public ResponseEntity<Map<String, Object>> buscar(@PathVariable String username) {
        Map<String, Object> usuario = usuarioService.buscarPorUsername(username);
        
        return usuario != null ? 
                ResponseEntity.ok(usuario) : 
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Deletar usuário", description = "Remove usuário do Keycloak")
    public ResponseEntity<Void> deletar(@PathVariable String userId) {
        usuarioService.deletar(userId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{userId}/ativar")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Ativar usuário", description = "Ativa um usuário desativado")
    public ResponseEntity<Void> ativar(@PathVariable String userId) {
        usuarioService.ativar(userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}/desativar")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Desativar usuário", description = "Desativa um usuário ativo")
    public ResponseEntity<Void> desativar(@PathVariable String userId) {
        usuarioService.desativar(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/reset-senha")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Resetar senha", description = "Reseta a senha de um usuário")
    public ResponseEntity<Void> resetarSenha(
            @PathVariable String userId,
            @Valid @RequestBody ResetSenhaRequest request) {
        
        usuarioService.resetarSenha(userId, request.getNovaSenha(), request.isTemporaria());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/grupos/{groupId}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Adicionar a grupo", description = "Adiciona usuário a um grupo")
    public ResponseEntity<Void> adicionarAoGrupo(
            @PathVariable String userId,
            @PathVariable String groupId) {
        
        usuarioService.adicionarAoGrupo(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/grupos/{groupId}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Remover de grupo", description = "Remove usuário de um grupo")
    public ResponseEntity<Void> removerDoGrupo(
            @PathVariable String userId,
            @PathVariable String groupId) {
        
        usuarioService.removerDoGrupo(userId, groupId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/logout")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Forçar logout", description = "Força logout de todas as sessões do usuário")
    public ResponseEntity<Void> forcarLogout(@PathVariable String userId) {
        usuarioService.forcarLogout(userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/colaborador/{colaboradorId}")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Buscar usuário por colaboradorId", 
               description = "Busca usuário Keycloak vinculado a um colaborador")
    public ResponseEntity<Map<String, Object>> buscarPorColaborador(
            @PathVariable Long colaboradorId) {
        
        List<Map<String, Object>> users = usuarioService.buscarPorAtributo(
            "colaboradorId", 
            colaboradorId.toString()
        );
        
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(users.get(0));
    }
    
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Atribuir roles ao usuário", 
               description = "Atribui roles de client ao usuário")
    public ResponseEntity<Void> atribuirRoles(
            @PathVariable String userId,
            @RequestBody List<String> roleNames) {
        
        usuarioService.atribuirRoles(userId, roleNames);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/{userId}/enviar-email-senha")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Enviar email para definir senha", 
               description = "Envia email para o usuário definir/redefinir sua senha")
    public ResponseEntity<Void> enviarEmailSenha(@PathVariable String userId) {
        usuarioService.enviarEmailDefinirSenha(userId);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{userId}/atributo")
    @PreAuthorize("hasRole('admin')")
    @Operation(summary = "Atualizar atributo customizado", 
               description = "Atualiza um atributo customizado do usuário")
    public ResponseEntity<Void> atualizarAtributo(
            @PathVariable String userId,
            @RequestBody Map<String, String> atributo) {
        
        String nome = atributo.get("nome");
        String valor = atributo.get("valor");
        
        if (nome == null || valor == null) {
            return ResponseEntity.badRequest().build();
        }
        
        usuarioService.atualizarAtributo(userId, nome, valor);
        return ResponseEntity.ok().build();
    }
}

