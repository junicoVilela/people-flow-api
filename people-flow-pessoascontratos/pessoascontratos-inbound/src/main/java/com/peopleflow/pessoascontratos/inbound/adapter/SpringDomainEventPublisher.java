package com.peopleflow.pessoascontratos.inbound.adapter;

import com.peopleflow.pessoascontratos.core.domain.events.ColaboradorEvent;
import com.peopleflow.pessoascontratos.core.ports.output.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Adaptador que implementa DomainEventPublisher usando Spring ApplicationEventPublisher
 * 
 * Este adaptador converte eventos de domínio para eventos do Spring,
 * permitindo que o core publique eventos sem conhecer o Spring.
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {
    
    private final ApplicationEventPublisher springPublisher;
    
    public SpringDomainEventPublisher(ApplicationEventPublisher springPublisher) {
        this.springPublisher = springPublisher;
    }
    
    @Override
    public void publish(ColaboradorEvent event) {
        springPublisher.publishEvent(event);
    }
}

