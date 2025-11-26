package com.peopleflow.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuração de processamento assíncrono
 * 
 * Habilita o suporte a @Async para processar eventos de domínio
 * de forma assíncrona, melhorando performance e desacoplamento.
 * 
 * Thread Pool configurado para:
 * - Core: 5 threads (mínimo sempre disponível)
 * - Max: 10 threads (máximo permitido)
 * - Queue: 100 (tarefas aguardando)
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    
    private static final Logger log = LoggerFactory.getLogger(AsyncConfig.class);
    
    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        
        // Número de threads core (sempre mantidas vivas)
        executor.setCorePoolSize(5);
        
        // Número máximo de threads
        executor.setMaxPoolSize(10);
        
        // Capacidade da fila de espera
        executor.setQueueCapacity(100);
        
        // Prefixo do nome das threads (facilita debug)
        executor.setThreadNamePrefix("async-event-");
        
        // Aguarda tarefas pendentes antes de desligar
        executor.setWaitForTasksToCompleteOnShutdown(true);
        
        // Tempo máximo de espera no shutdown (30 segundos)
        executor.setAwaitTerminationSeconds(30);
        
        executor.initialize();
        
        log.info("Thread pool configurado: core={}, max={}, queue={}", 
                 executor.getCorePoolSize(), 
                 executor.getMaxPoolSize(), 
                 executor.getQueueCapacity());
        
        return executor;
    }
    
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (throwable, method, params) -> {
            log.error("Erro não tratado em método assíncrono: {}.{}()", 
                     method.getDeclaringClass().getSimpleName(),
                     method.getName(), 
                     throwable);
            
            // Aqui você pode adicionar lógica adicional como:
            // - Enviar para sistema de monitoramento
            // - Tentar reprocessar
            // - Enviar notificação
        };
    }
}

