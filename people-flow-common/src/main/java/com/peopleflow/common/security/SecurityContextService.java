package com.peopleflow.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Serviço para extrair informações do contexto de segurança
 * 
 * Centraliza a lógica de extração de dados do JWT e do Authentication,
 * facilitando a validação de permissões e auditoria.
 */
@Service
public class SecurityContextService {

    /**
     * Obtém o nome do usuário autenticado
     * 
     * @return Username ou "anonymous" se não autenticado
     */
    public String getCurrentUsername() {
        Authentication authentication = getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "anonymous";
        }
        
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String preferredUsername = jwt.getClaimAsString("preferred_username");
            if (preferredUsername != null) {
                return preferredUsername;
            }
            
            String email = jwt.getClaimAsString("email");
            if (email != null) {
                return email;
            }
            
            return jwt.getSubject();
        }
        
        return authentication.getName();
    }

    /**
     * Obtém a lista de IDs de clientes que o usuário tem permissão de acessar
     * 
     * @return Lista de clienteIds ou lista vazia se sem restrições (admin global)
     */
    public Set<Long> getAllowedClienteIds() {
        Jwt jwt = getCurrentJwt();
        
        if (jwt == null) {
            // Em modo desenvolvimento sem JWT, permite tudo
            return Set.of();
        }
        
        // Verifica se é admin global (sem restrições)
        if (hasRole("ADMIN")) {
            return Set.of(); // Vazio = sem restrições
        }
        
        // Extrai clienteIds do JWT (claim customizado)
        List<String> clienteIds = jwt.getClaimAsStringList("cliente_ids");
        if (clienteIds != null && !clienteIds.isEmpty()) {
            return clienteIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        }
        
        // Fallback para cliente_id único (caso não seja lista)
        Object clienteId = jwt.getClaim("cliente_id");
        if (clienteId != null) {
            return Set.of(Long.valueOf(clienteId.toString()));
        }
        
        // Se não tem clienteId no token, não pode acessar nada
        return Set.of();
    }

    /**
     * Obtém a lista de IDs de empresas que o usuário tem permissão de acessar
     * 
     * @return Lista de empresaIds ou lista vazia se sem restrições (admin global)
     */
    public Set<Long> getAllowedEmpresaIds() {
        Jwt jwt = getCurrentJwt();
        
        if (jwt == null) {
            // Em modo desenvolvimento sem JWT, permite tudo
            return Set.of();
        }
        
        // Verifica se é admin global (sem restrições)
        if (hasRole("ADMIN")) {
            return Set.of(); // Vazio = sem restrições
        }
        
        // Extrai empresaIds do JWT (claim customizado)
        List<String> empresaIds = jwt.getClaimAsStringList("empresa_ids");
        if (empresaIds != null && !empresaIds.isEmpty()) {
            return empresaIds.stream()
                .map(Long::valueOf)
                .collect(Collectors.toSet());
        }
        
        // Fallback para empresa_id único (caso não seja lista)
        Object empresaId = jwt.getClaim("empresa_id");
        if (empresaId != null) {
            return Set.of(Long.valueOf(empresaId.toString()));
        }
        
        // Se não tem empresaId no token, não pode acessar nada
        return Set.of();
    }

    /**
     * Verifica se o usuário tem uma role específica
     * 
     * @param role Nome da role (sem prefixo ROLE_)
     * @return true se possui a role
     */
    public boolean hasRole(String role) {
        Authentication authentication = getAuthentication();
        
        if (authentication == null) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
            .anyMatch(auth -> 
                auth.getAuthority().equals("ROLE_" + role) || 
                auth.getAuthority().equals(role)
            );
    }

    /**
     * Verifica se o usuário pode acessar dados de um cliente específico
     * 
     * @param clienteId ID do cliente
     * @return true se tem permissão
     */
    public boolean canAccessCliente(Long clienteId) {
        if (clienteId == null) {
            return false;
        }
        
        Set<Long> allowedIds = getAllowedClienteIds();
        
        // Set vazio = admin sem restrições
        if (allowedIds.isEmpty() && hasRole("ADMIN")) {
            return true;
        }
        
        return allowedIds.contains(clienteId);
    }

    /**
     * Verifica se o usuário pode acessar dados de uma empresa específica
     * 
     * @param empresaId ID da empresa
     * @return true se tem permissão
     */
    public boolean canAccessEmpresa(Long empresaId) {
        if (empresaId == null) {
            return false;
        }
        
        Set<Long> allowedIds = getAllowedEmpresaIds();
        
        // Set vazio = admin sem restrições
        if (allowedIds.isEmpty() && hasRole("ADMIN")) {
            return true;
        }
        
        return allowedIds.contains(empresaId);
    }

    /**
     * Verifica se o usuário é admin global (sem restrições de cliente/empresa)
     * 
     * @return true se é admin global
     */
    public boolean isGlobalAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * Obtém o JWT atual do contexto de segurança
     * 
     * @return JWT ou null se não disponível
     */
    private Jwt getCurrentJwt() {
        Authentication authentication = getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        
        return null;
    }

    /**
     * Obtém o Authentication atual
     * 
     * @return Authentication ou null
     */
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}

