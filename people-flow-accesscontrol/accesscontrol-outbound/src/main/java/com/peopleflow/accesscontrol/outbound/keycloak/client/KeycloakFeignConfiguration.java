package com.peopleflow.accesscontrol.outbound.keycloak.client;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Feign Client para Keycloak
 */
@Slf4j
@Configuration
public class KeycloakFeignConfiguration {

    /**
     * Nível de logging do Feign
     * NONE: Sem logs
     * BASIC: Log de request method, URL e response status
     * HEADERS: BASIC + headers
     * FULL: HEADERS + body
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    /**
     * Interceptor para adicionar headers comuns em todas as requisições
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
        };
    }

    /**
     * Decodificador de erros customizado
     * Converte erros HTTP do Keycloak em exceções específicas
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            log.error("Erro ao chamar Keycloak API: método={}, status={}", 
                     methodKey, response.status());
            
            return switch (response.status()) {
                case 400 -> new KeycloakBadRequestException(
                    "Requisição inválida ao Keycloak: " + methodKey
                );
                case 401 -> new KeycloakAuthenticationException(
                    "Não autorizado - credenciais admin inválidas"
                );
                case 403 -> new KeycloakAuthorizationException(
                    "Sem permissão para executar esta operação"
                );
                case 404 -> new KeycloakNotFoundException(
                    "Recurso não encontrado no Keycloak"
                );
                case 409 -> new KeycloakConflictException(
                    "Recurso já existe no Keycloak (conflito)"
                );
                case 500 -> new KeycloakServerException(
                    "Erro interno do servidor Keycloak"
                );
                default -> new KeycloakException(
                    "Erro ao chamar Keycloak: HTTP " + response.status()
                );
            };
        };
    }
}

// ==================== EXCEÇÕES CUSTOMIZADAS ====================

/**
 * Exceção base para erros do Keycloak
 */
class KeycloakException extends RuntimeException {
    public KeycloakException(String message) {
        super(message);
    }
    
    public KeycloakException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * Erro 400 - Requisição inválida
 */
class KeycloakBadRequestException extends KeycloakException {
    public KeycloakBadRequestException(String message) {
        super(message);
    }
}

/**
 * Erro 401 - Não autenticado
 */
class KeycloakAuthenticationException extends KeycloakException {
    public KeycloakAuthenticationException(String message) {
        super(message);
    }
}

/**
 * Erro 403 - Sem permissão
 */
class KeycloakAuthorizationException extends KeycloakException {
    public KeycloakAuthorizationException(String message) {
        super(message);
    }
}

/**
 * Erro 404 - Não encontrado
 */
class KeycloakNotFoundException extends KeycloakException {
    public KeycloakNotFoundException(String message) {
        super(message);
    }
}

/**
 * Erro 409 - Conflito (recurso já existe)
 */
class KeycloakConflictException extends KeycloakException {
    public KeycloakConflictException(String message) {
        super(message);
    }
}

/**
 * Erro 500 - Erro do servidor
 */
class KeycloakServerException extends KeycloakException {
    public KeycloakServerException(String message) {
        super(message);
    }
}

