package com.peopleflow.accesscontrol.inbound.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração de cache para o módulo Access Control
 * 
 * Caches:
 * - keycloak-admin-token: Token de administrador (TTL gerenciado manualmente)
 * - keycloak-client-uuid: UUID do client (cache permanente)
 */
@Configuration
@EnableCaching
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        
        cacheManager.setCaches(List.of(
            new ConcurrentMapCache("keycloak-admin-token"),
            new ConcurrentMapCache("keycloak-client-uuid")
        ));
        
        return cacheManager;
    }
}

