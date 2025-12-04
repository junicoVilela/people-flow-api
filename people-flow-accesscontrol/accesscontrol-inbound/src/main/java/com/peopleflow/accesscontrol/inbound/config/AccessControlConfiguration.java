package com.peopleflow.accesscontrol.inbound.config;

import com.peopleflow.accesscontrol.core.application.AutoAtribuicaoService;
import com.peopleflow.accesscontrol.core.application.GrupoService;
import com.peopleflow.accesscontrol.core.application.RoleService;
import com.peopleflow.accesscontrol.core.application.UsuarioService;
import com.peopleflow.accesscontrol.core.ports.output.CargoRoleMappingPort;
import com.peopleflow.accesscontrol.core.ports.output.DepartamentoGrupoMappingPort;
import com.peopleflow.accesscontrol.core.ports.output.KeycloakGrupoPort;
import com.peopleflow.accesscontrol.core.ports.output.KeycloakRolePort;
import com.peopleflow.accesscontrol.core.ports.output.KeycloakUsuarioPort;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class AccessControlConfiguration {

    @Bean
    public UsuarioService usuarioService(KeycloakUsuarioPort keycloakUsuarioPort) {
        return new UsuarioService(keycloakUsuarioPort);
    }

    @Bean
    public GrupoService grupoService(KeycloakGrupoPort keycloakGrupoPort) {
        return new GrupoService(keycloakGrupoPort);
    }

    @Bean
    public RoleService roleService(KeycloakRolePort keycloakRolePort) {
        return new RoleService(keycloakRolePort);
    }

    @Bean
    public AutoAtribuicaoService autoAtribuicaoService(
            KeycloakUsuarioPort usuarioPort,
            CargoRoleMappingPort cargoMappingPort,
            DepartamentoGrupoMappingPort departamentoMappingPort) {
        return new AutoAtribuicaoService(usuarioPort, cargoMappingPort, departamentoMappingPort);
    }
}
