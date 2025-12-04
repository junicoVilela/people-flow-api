package com.peopleflow.common.validation;

import com.peopleflow.common.security.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validador de multi-tenancy para garantir que usuários
 * acessem apenas dados de seu próprio cliente/empresa
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MultiTenancyValidator {

    private final SecurityContextHelper securityHelper;

    /**
     * Valida se o usuário tem acesso ao clienteId especificado
     * 
     * @param clienteId ID do cliente a ser validado
     * @throws MultiTenancyViolationException se o usuário não tiver acesso
     */
    public void validarAcessoCliente(Long clienteId) {
        Long userClienteId = securityHelper.getClienteId();
        
        if (userClienteId == null) {
            log.warn("⚠️ Usuário sem clienteId atribuído: {}", securityHelper.getUsername());
            throw new MultiTenancyViolationException(
                "Usuário não possui clienteId atribuído. Contate o administrador."
            );
        }

        if (!userClienteId.equals(clienteId)) {
            log.error("🚨 VIOLAÇÃO DE MULTI-TENANCY: Usuário {} (clienteId={}) tentou acessar dados do clienteId={}",
                    securityHelper.getUsername(), userClienteId, clienteId);
            throw new MultiTenancyViolationException(
                String.format("Acesso negado: você não tem permissão para acessar dados do cliente %d", clienteId)
            );
        }

        log.debug("✅ Acesso ao clienteId {} validado para usuário {}", clienteId, securityHelper.getUsername());
    }

    /**
     * Valida se o usuário tem acesso à empresaId especificada
     * 
     * @param empresaId ID da empresa a ser validada
     * @throws MultiTenancyViolationException se o usuário não tiver acesso
     */
    public void validarAcessoEmpresa(Long empresaId) {
        Long userEmpresaId = securityHelper.getEmpresaId();
        
        if (userEmpresaId == null) {
            log.warn("⚠️ Usuário sem empresaId atribuído: {}", securityHelper.getUsername());
            throw new MultiTenancyViolationException(
                "Usuário não possui empresaId atribuído. Contate o administrador."
            );
        }

        if (!userEmpresaId.equals(empresaId)) {
            log.error("🚨 VIOLAÇÃO DE MULTI-TENANCY: Usuário {} (empresaId={}) tentou acessar dados da empresaId={}",
                    securityHelper.getUsername(), userEmpresaId, empresaId);
            throw new MultiTenancyViolationException(
                String.format("Acesso negado: você não tem permissão para acessar dados da empresa %d", empresaId)
            );
        }

        log.debug("✅ Acesso à empresaId {} validado para usuário {}", empresaId, securityHelper.getUsername());
    }

    /**
     * Valida acesso tanto ao cliente quanto à empresa
     * 
     * @param clienteId ID do cliente
     * @param empresaId ID da empresa
     * @throws MultiTenancyViolationException se o usuário não tiver acesso
     */
    public void validarAcessoCompleto(Long clienteId, Long empresaId) {
        validarAcessoCliente(clienteId);
        validarAcessoEmpresa(empresaId);
    }

    /**
     * Retorna o clienteId do usuário autenticado
     * 
     * @return clienteId ou null se não atribuído
     */
    public Long getClienteIdUsuario() {
        return securityHelper.getClienteId();
    }

    /**
     * Retorna a empresaId do usuário autenticado
     * 
     * @return empresaId ou null se não atribuído
     */
    public Long getEmpresaIdUsuario() {
        return securityHelper.getEmpresaId();
    }

    /**
     * Verifica se o usuário é admin (bypass de validações)
     * 
     * @return true se o usuário tem role 'admin'
     */
    public boolean isAdmin() {
        return securityHelper.hasRole("admin");
    }
}

