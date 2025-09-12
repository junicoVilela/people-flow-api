package com.peopleflow.common.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtAuthConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = jwtGrantedAuthoritiesConverter.convert(jwt);
        
        Object realmAccessObj = jwt.getClaim("realm_access");
        if (realmAccessObj instanceof Map) {
            Map realmAccess = (Map) realmAccessObj;
            Object rolesObj = realmAccess.get("roles");
            if (rolesObj instanceof Collection) {
                Collection roles = (Collection) rolesObj;
                Collection<GrantedAuthority> realmRoles = (Collection<GrantedAuthority>) roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toSet());
                
                return Stream.concat(authorities.stream(), realmRoles.stream())
                        .collect(Collectors.toSet());
            }
        }
        
        return authorities != null ? authorities : Collections.emptyList();
    }
} 