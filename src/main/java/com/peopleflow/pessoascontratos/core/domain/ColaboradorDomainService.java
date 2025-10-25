package com.peopleflow.pessoascontratos.core.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Serviço de Domínio para Colaborador
 * 
 * NOTA: As validações de invariantes foram movidas para o próprio modelo Colaborador,
 * seguindo o princípio de Rich Domain Model. Este service agora serve apenas para
 * lógicas de negócio que envolvem múltiplas entidades ou serviços externos.
 * 
 * @deprecated A maioria das validações agora está no próprio modelo Colaborador.
 * Este serviço pode ser removido no futuro se não houver lógicas complexas envolvendo múltiplas entidades.
 */
@Service
@Deprecated(since = "0.2.0", forRemoval = true)
public class ColaboradorDomainService {
    
    private static final Logger log = LoggerFactory.getLogger(ColaboradorDomainService.class);

    /**
     * Este serviço foi simplificado. Todas as validações de invariantes e regras de negócio
     * agora estão no modelo Colaborador (Rich Domain Model).
     * 
     * Métodos anteriores foram movidos para:
     * - validarDadosObrigatorios() → Colaborador.validarInvariantes() (chamado no builder)
     * - validarDemissao() → Colaborador.demitir()
     * - validarAtivacao() → Colaborador.ativar()
     * - validarExclusao() → Colaborador.excluir()
     * 
     * Mantenha este serviço apenas para lógicas que envolvem:
     * - Múltiplas agregações
     * - Serviços externos
     * - Regras complexas que não pertencem a uma única entidade
     */
    
    // Métodos futuros para lógicas de domínio complexas podem ser adicionados aqui
}
