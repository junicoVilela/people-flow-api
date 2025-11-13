package com.peopleflow.pessoascontratos.core.ports.output;

import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorEvent;

/**
 * Port para publicação de eventos de domínio
 * 
 * Esta interface abstrai a publicação de eventos, permitindo que o core
 * publique eventos sem conhecer o mecanismo de implementação (Spring Events, 
 * Message Broker, etc.)
 */
public interface DomainEventPublisher {
    
    /**
     * Publica um evento de domínio
     * 
     * @param event Evento a ser publicado
     */
    void publish(ColaboradorEvent event);
}

