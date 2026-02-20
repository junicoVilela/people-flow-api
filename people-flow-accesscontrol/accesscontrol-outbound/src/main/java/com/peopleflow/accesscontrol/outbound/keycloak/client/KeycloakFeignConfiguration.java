package com.peopleflow.accesscontrol.outbound.keycloak.client;

import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * Encoder que suporta application/x-www-form-urlencoded (token Keycloak) e JSON.
     */
    @Bean
    public Encoder feignEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Só define Content-Type se ainda não foi definido (ex.: token usa form-urlencoded)
            if (!requestTemplate.headers().containsKey("Content-Type")) {
                requestTemplate.header("Content-Type", "application/json");
            }
            if (!requestTemplate.headers().containsKey("Accept")) {
                requestTemplate.header("Accept", "application/json");
            }
        };
    }

    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
            5000,  // connectTimeout: 5 segundos
            10000  // readTimeout: 10 segundos
        );
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
            1000,   // period: intervalo inicial entre tentativas (1s)
            2000,   // maxPeriod: intervalo máximo (2s)
            3       // maxAttempts: máximo de 3 tentativas
        );
    }

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

