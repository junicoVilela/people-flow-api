package com.peopleflow.accesscontrol.outbound.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.peopleflow.accesscontrol.outbound.keycloak.client")
public class FeignConfiguration {
}

