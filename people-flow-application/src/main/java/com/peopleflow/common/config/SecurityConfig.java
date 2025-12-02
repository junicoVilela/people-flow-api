package com.peopleflow.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Configuração de Segurança com OAuth2 Resource Server
 * Integração com Keycloak para autenticação e autorização via JWT
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    /**
     * Configura o filtro de segurança HTTP
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sem autenticação)
                .requestMatchers(
                    "/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/actuator/health",
                    "/actuator/info"
                ).permitAll()
                // Todos os outros endpoints requerem autenticação
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );

        return http.build();
    }

    /**
     * Decodificador de JWT usando JWK Set URI do Keycloak
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    /**
     * Conversor de JWT para Authentication
     * Extrai roles do Keycloak e converte em GrantedAuthorities do Spring Security
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(new KeycloakGrantedAuthoritiesConverter());
        return converter;
    }

    /**
     * Converte as roles do Keycloak em authorities do Spring Security
     * 
     * Extrai roles de duas fontes:
     * 1. Realm roles: roles globais do realm
     * 2. Client roles: roles específicas do client (peopleflow-api)
     */
    static class KeycloakGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            // Extrair roles do realm
            Collection<GrantedAuthority> realmRoles = extractRealmRoles(jwt);
            
            // Extrair roles do client (peopleflow-api)
            Collection<GrantedAuthority> clientRoles = extractClientRoles(jwt, "peopleflow-api");
            
            // Combinar ambas as fontes
            return Stream.concat(realmRoles.stream(), clientRoles.stream())
                    .collect(Collectors.toList());
        }

        /**
         * Extrai roles do realm (roles globais)
         */
        private Collection<GrantedAuthority> extractRealmRoles(Jwt jwt) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess == null) {
                return List.of();
            }
            
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles == null) {
                return List.of();
            }
            
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        }

        /**
         * Extrai roles do client (roles específicas da aplicação)
         * Formato no JWT: resource_access.peopleflow-api.roles
         */
        private Collection<GrantedAuthority> extractClientRoles(Jwt jwt, String clientId) {
            Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
            if (resourceAccess == null) {
                return List.of();
            }
            
            @SuppressWarnings("unchecked")
            Map<String, Object> client = (Map<String, Object>) resourceAccess.get(clientId);
            if (client == null) {
                return List.of();
            }
            
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) client.get("roles");
            if (roles == null) {
                return List.of();
            }
            
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        }
    }
}

