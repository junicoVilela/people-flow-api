package com.peopleflow.pessoascontratos.core.ports.input;

import java.util.Set;

/**
 * Port para acesso ao contexto de segurança
 * 
 * Esta interface abstrai o acesso a informações de segurança, permitindo que o core
 * valide permissões sem conhecer o mecanismo de implementação (Spring Security, 
 * JWT, etc.)
 */
public interface SecurityContext {
    
    /**
     * Obtém o nome do usuário autenticado
     * 
     * @return Username ou "anonymous" se não autenticado
     */
    String getCurrentUsername();
    
    /**
     * Obtém a lista de IDs de clientes que o usuário tem permissão de acessar
     * 
     * @return Lista de clienteIds ou lista vazia se sem restrições (admin global)
     */
    Set<Long> getAllowedClienteIds();
    
    /**
     * Obtém a lista de IDs de empresas que o usuário tem permissão de acessar
     * 
     * @return Lista de empresaIds ou lista vazia se sem restrições (admin global)
     */
    Set<Long> getAllowedEmpresaIds();
    
    /**
     * Verifica se o usuário é admin global (sem restrições de cliente/empresa)
     * 
     * @return true se é admin global
     */
    boolean isGlobalAdmin();
    
    /**
     * Verifica se o usuário pode acessar dados de um cliente específico
     * 
     * @param clienteId ID do cliente
     * @return true se tem permissão
     */
    boolean canAccessCliente(Long clienteId);
    
    /**
     * Verifica se o usuário pode acessar dados de uma empresa específica
     * 
     * @param empresaId ID da empresa
     * @return true se tem permissão
     */
    boolean canAccessEmpresa(Long empresaId);
}

