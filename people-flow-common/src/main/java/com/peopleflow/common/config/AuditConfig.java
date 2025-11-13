package com.peopleflow.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

/**
 * Configuração de auditoria JPA
 * 
 * Captura automaticamente o usuário autenticado para preencher os campos
 * criadoPor e atualizadoPor das entidades auditáveis.
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * Implementação de AuditorAware que extrai o usuário do contexto de segurança
     * 
     * Prioridades:
     * 1. Usuário autenticado via JWT (preferred_username ou sub claim)
     * 2. Usuário autenticado via Spring Security (getName)
     * 3. Header HTTP X-User-ID (para sistemas legados ou testes)
     * 4. Fallback para "system" (operações internas)
     */
    public static class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                
                if (authentication == null || !authentication.isAuthenticated()) {
                    return Optional.of("anonymous");
                }
                
                // Se for autenticação JWT (OAuth2)
                if (authentication.getPrincipal() instanceof Jwt jwt) {
                    // Tenta extrair preferred_username (padrão Keycloak/OAuth2)
                    String preferredUsername = jwt.getClaimAsString("preferred_username");
                    if (preferredUsername != null && !preferredUsername.isBlank()) {
                        return Optional.of(preferredUsername);
                    }
                    
                    // Fallback para email
                    String email = jwt.getClaimAsString("email");
                    if (email != null && !email.isBlank()) {
                        return Optional.of(email);
                    }
                    
                    // Fallback para subject (ID do usuário)
                    String subject = jwt.getSubject();
                    if (subject != null && !subject.isBlank()) {
                        return Optional.of(subject);
                    }
                }
                
                // Para autenticação básica ou outros tipos
                String name = authentication.getName();
                if (name != null && !name.isBlank() && !"anonymousUser".equals(name)) {
                    return Optional.of(name);
                }
                
            } catch (Exception e) {
                // Em caso de erro, registra e usa fallback
                // (Evita quebrar o fluxo de auditoria)
                System.err.println("Erro ao obter auditor: " + e.getMessage());
            }
            
            // Fallback para operações sem contexto de segurança
            return Optional.of("system");
        }
    }
}
