package com.peopleflow.pessoascontratos.core.model.events;

import java.time.LocalDateTime;

/**
 * Interface base para eventos de domínio de Colaborador
 * 
 * Eventos representam fatos que aconteceram no passado e são imutáveis.
 * Eles permitem comunicação assíncrona entre diferentes partes do sistema.
 * 
 * Princípios:
 * - Eventos são IMUTÁVEIS (use records)
 * - Eventos representam o PASSADO (use verbos no passado)
 * - Eventos contêm DADOS necessários para reagir
 * 
 * @see ColaboradorCriado
 * @see ColaboradorDemitido
 */
public sealed interface ColaboradorEvent permits 
    ColaboradorCriado,
    ColaboradorAtualizado,
    ColaboradorDemitido,
    ColaboradorAtivado,
    ColaboradorInativado,
    ColaboradorExcluido {
    
    /**
     * ID do colaborador que gerou o evento
     */
    Long colaboradorId();
    
    /**
     * Momento em que o evento ocorreu
     */
    LocalDateTime ocorridoEm();
    
    /**
     * Nome do colaborador (útil para logs e notificações)
     */
    String nomeColaborador();
}

