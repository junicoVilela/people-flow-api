package com.peopleflow.common.validation;

/**
 * Port para validação de acesso a recursos
 * Implementação deve estar no módulo de infraestrutura
 */
public interface AccessValidatorPort {
    
    /**
     * Valida se o usuário tem acesso à empresaId especificada
     * 
     * @param empresaId ID da empresa a ser validada
     * @throws AccessViolationException se o usuário não tiver acesso
     */
    void validarAcessoEmpresa(Long empresaId);
    
    /**
     * Retorna a empresaId do usuário autenticado
     * 
     * @return empresaId ou null se não atribuído
     */
    Long getEmpresaIdUsuario();
    
    /**
     * Verifica se o usuário é admin (bypass de validações)
     * 
     * @return true se o usuário tem role 'admin'
     */
    boolean isAdmin();
}
