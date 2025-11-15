package com.peopleflow.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${app.api.version:1.0.0}")
    private String apiVersion;

    @Value("${app.api.title:People Flow API}")
    private String apiTitle;

    @Value("${app.api.description:API para gestão de colaboradores e RH}")
    private String apiDescription;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(apiTitle)
                        .version(apiVersion)
                        .description(buildDescription())
                        .contact(new Contact()
                                .name("Equipe People Flow")
                                .email("contato@peopleflow.com")
                                .url("https://peopleflow.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Ambiente de Desenvolvimento"),
                        new Server()
                                .url("https://api-dev.peopleflow.com")
                                .description("Ambiente de Homologação"),
                        new Server()
                                .url("https://api.peopleflow.com")
                                .description("Ambiente de Produção")));
    }

    /**
     * Constrói a descrição completa da API incluindo informações técnicas
     */
    private String buildDescription() {
        return String.format("""
            %s
            
            ## 🏗️ Arquitetura
            
            Esta API foi desenvolvida seguindo os princípios de:
            - **Arquitetura Hexagonal** (Ports & Adapters)
            - **Domain-Driven Design** (DDD)
            - **Rich Domain Model** com validações de invariantes
            - **Domain Events** para desacoplamento
            - **CQRS Pattern** para separação de leitura/escrita
            
            ## 📋 Funcionalidades
            
            - Gerenciamento completo de colaboradores (CRUD)
            - Operações de negócio (demitir, ativar, inativar, excluir)
            - Busca avançada com filtros e paginação
            - Validações de regras de negócio
            - Auditoria automática de operações
            
            ## 📊 Códigos de Status
            
            - `200 OK` - Requisição bem-sucedida
            - `201 Created` - Recurso criado com sucesso
            - `204 No Content` - Requisição bem-sucedida sem retorno
            - `400 Bad Request` - Erro de validação nos dados
            - `404 Not Found` - Recurso não encontrado
            - `409 Conflict` - Conflito (ex: CPF duplicado)
            - `500 Internal Server Error` - Erro interno do servidor
            """, apiDescription);
    }
}

