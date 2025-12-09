package com.peopleflow.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Helper para extrair informações do contexto de segurança (JWT)
 * 
 * Esta classe facilita o acesso a informações do usuário autenticado
 * através do token JWT fornecido pelo Keycloak.
 * 
 * Informações disponíveis:
 * - Username (preferred_username)
 * - Email
 * - EmpresaId (atributo customizado)
 * - Subject (ID do usuário no Keycloak)
 * - Roles/Permissões
 */
@Component
public class SecurityContextHelper {

    /**
     * Obtém o username do usuário logado
     * @return username ou "system" se não autenticado
     */
    public String getUsername() {
        return getJwt()
                .map(jwt -> jwt.getClaimAsString("preferred_username"))
                .orElse("system");
    }

    /**
     * Obtém o email do usuário logado
     * @return email ou null se não disponível
     */
    public String getEmail() {
        return getJwt()
                .map(jwt -> jwt.getClaimAsString("email"))
                .orElse(null);
    }

    /**
     * Obtém o nome completo do usuário
     * @return nome ou null se não disponível
     */
    public String getFullName() {
        return getJwt()
                .map(jwt -> {
                    String givenName = jwt.getClaimAsString("given_name");
                    String familyName = jwt.getClaimAsString("family_name");
                    if (givenName != null && familyName != null) {
                        return givenName + " " + familyName;
                    }
                    return jwt.getClaimAsString("name");
                })
                .orElse(null);
    }

    /**
     * Obtém o empresaId do contexto do usuário
     * Este é um atributo customizado configurado no Keycloak
     * @return empresaId ou null se não disponível
     */
    public Long getEmpresaId() {
        return getJwt()
                .map(jwt -> {
                    Object empresaId = jwt.getClaim("empresaId");
                    if (empresaId == null) {
                        return null;
                    }
                    if (empresaId instanceof String) {
                        return Long.parseLong((String) empresaId);
                    } else if (empresaId instanceof Number) {
                        return ((Number) empresaId).longValue();
                    }
                    return null;
                })
                .orElse(null);
    }

    /**
     * Obtém o subject (ID do usuário no Keycloak)
     * @return subject UUID ou null se não disponível
     */
    public String getSubject() {
        return getJwt()
                .map(Jwt::getSubject)
                .orElse(null);
    }

    /**
     * Verifica se o usuário tem uma role específica
     * @param role nome da role (sem prefixo ROLE_)
     * @return true se o usuário possui a role
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }

    /**
     * Verifica se o usuário tem qualquer uma das roles especificadas
     * @param roles nomes das roles
     * @return true se o usuário possui ao menos uma das roles
     */
    public boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se o usuário tem todas as roles especificadas
     * @param roles nomes das roles
     * @return true se o usuário possui todas as roles
     */
    public boolean hasAllRoles(String... roles) {
        for (String role : roles) {
            if (!hasRole(role)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtém todas as roles do usuário
     * @return conjunto de roles (sem o prefixo ROLE_)
     */
    public Set<String> getRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return Set.of();
        }
        
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(auth -> auth.startsWith("ROLE_"))
                .map(auth -> auth.substring(5)) // Remove "ROLE_"
                .collect(Collectors.toSet());
    }

    /**
     * Verifica se o usuário está autenticado
     * @return true se há um usuário autenticado
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * Obtém o JWT completo
     * @return Optional contendo o JWT ou empty se não disponível
     */
    private Optional<Jwt> getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            return Optional.empty();
        }
        return Optional.of((Jwt) authentication.getPrincipal());
    }

    /**
     * Obtém todos os claims do JWT
     * @return mapa com todos os claims ou mapa vazio se não disponível
     */
    public Map<String, Object> getAllClaims() {
        return getJwt()
                .map(Jwt::getClaims)
                .orElse(Map.of());
    }

    /**
     * Obtém um claim específico do JWT
     * @param claimName nome do claim
     * @return valor do claim ou null se não disponível
     */
    public Object getClaim(String claimName) {
        return getJwt()
                .map(jwt -> jwt.getClaim(claimName))
                .orElse(null);
    }
}

