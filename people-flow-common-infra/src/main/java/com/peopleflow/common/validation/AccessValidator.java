package com.peopleflow.common.validation;

import com.peopleflow.common.security.SecurityContextHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Validador de acesso para garantir que usuários
 * acessem apenas dados de sua própria empresa
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccessValidator implements AccessValidatorPort {

    private final SecurityContextHelper securityHelper;

    @Override
    public void validarAcessoEmpresa(Long empresaId) {
        Long userEmpresaId = securityHelper.getEmpresaId();
        
        if (userEmpresaId == null) {
            log.warn("⚠️ Usuário sem empresaId atribuído: {}", securityHelper.getUsername());
            throw new AccessViolationException(
                "Usuário não possui empresaId atribuído. Contate o administrador."
            );
        }

        if (!userEmpresaId.equals(empresaId)) {
            log.error("🚨 VIOLAÇÃO DE ACESSO: Usuário {} (empresaId={}) tentou acessar dados da empresaId={}",
                    securityHelper.getUsername(), userEmpresaId, empresaId);
            throw new AccessViolationException(
                String.format("Acesso negado: você não tem permissão para acessar dados da empresa %d", empresaId)
            );
        }

        log.debug("✅ Acesso à empresaId {} validado para usuário {}", empresaId, securityHelper.getUsername());
    }

    @Override
    public Long getEmpresaIdUsuario() {
        return securityHelper.getEmpresaId();
    }

    @Override
    public boolean isAdmin() {
        return securityHelper.hasRole("admin");
    }
}

