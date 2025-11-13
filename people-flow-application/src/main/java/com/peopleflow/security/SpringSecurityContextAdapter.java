package com.peopleflow.security;

import com.peopleflow.pessoascontratos.core.ports.input.SecurityContext;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Adaptador que implementa SecurityContext usando Spring SecurityContextService
 * 
 * Este adaptador converte chamadas do core para o SecurityContextService do Spring,
 * permitindo que o core valide permissões sem conhecer o Spring Security.
 */
@Component
public class SpringSecurityContextAdapter implements SecurityContext {
    
    private final SecurityContextService securityContextService;
    
    public SpringSecurityContextAdapter(SecurityContextService securityContextService) {
        this.securityContextService = securityContextService;
    }
    
    @Override
    public String getCurrentUsername() {
        return securityContextService.getCurrentUsername();
    }
    
    @Override
    public Set<Long> getAllowedClienteIds() {
        return securityContextService.getAllowedClienteIds();
    }
    
    @Override
    public Set<Long> getAllowedEmpresaIds() {
        return securityContextService.getAllowedEmpresaIds();
    }
    
    @Override
    public boolean isGlobalAdmin() {
        return securityContextService.isGlobalAdmin();
    }
    
    @Override
    public boolean canAccessCliente(Long clienteId) {
        return securityContextService.canAccessCliente(clienteId);
    }
    
    @Override
    public boolean canAccessEmpresa(Long empresaId) {
        return securityContextService.canAccessEmpresa(empresaId);
    }
}

