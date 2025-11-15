package com.peopleflow.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/**
 * Configuração de auditoria JPA
 * 
 * Captura automaticamente o usuário para preencher os campos
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
     * Implementação de AuditorAware que retorna um usuário padrão
     * 
     * Sem segurança configurada, sempre retorna "system" como auditor.
     */
    public static class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            return Optional.of("system");
        }
    }
}
