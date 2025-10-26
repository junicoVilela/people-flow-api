package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.pessoascontratos.core.valueobject.Cpf;
import com.peopleflow.pessoascontratos.core.valueobject.Email;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper de conversão entre Entidades JPA (persistência) e objetos de Domínio
 * 
 * IMPORTANTE: Este mapper NÃO faz validações, apenas TRANSFORMAÇÕES.
 * 
 * Responsabilidades:
 * - Converter tipos do banco (String) para Value Objects do domínio
 * - Converter Value Objects do domínio para tipos do banco (String)
 * - Gerenciar campos de auditoria (ignorados, gerenciados pelo JPA)
 * 
 * Validações são responsabilidade dos Value Objects.
 */
@Mapper(componentModel = "spring")
public interface ColaboradorJpaMapper {

    /**
     * Converte Entity (banco) para Domínio
     * Os Value Objects validam os dados vindos do banco
     */
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "stringToCpf")
    @Mapping(target = "email", source = "email", qualifiedByName = "stringToEmail")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Colaborador toDomain(ColaboradorEntity entity);

    /**
     * Converte Domínio para Entity (banco)
     * Campos de auditoria são gerenciados pelo JPA (@PrePersist, @PreUpdate)
     */
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "cpfToString")
    @Mapping(target = "email", source = "email", qualifiedByName = "emailToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    @Mapping(target = "criadoPor", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoPor", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    ColaboradorEntity toEntity(Colaborador colaborador);

    // ===== Conversões String → Value Objects (Banco → Domínio) =====
    
    /**
     * String → CPF: Value Object valida internamente
     */
    @Named("stringToCpf")
    default Cpf stringToCpf(String cpf) {
        return cpf != null ? new Cpf(cpf) : null;
    }

    /**
     * String → Email: Value Object valida internamente
     */
    @Named("stringToEmail")
    default Email stringToEmail(String email) {
        return email != null ? new Email(email) : null;
    }

    /**
     * String → StatusColaborador: Value Object valida internamente
     */
    @Named("stringToStatus")
    default StatusColaborador stringToStatus(String status) {
        return status != null ? StatusColaborador.of(status) : null;
    }

    // ===== Conversões Value Objects → String (Domínio → Banco) =====
    
    /**
     * CPF → String: Apenas extrai o valor formatado
     */
    @Named("cpfToString")
    default String cpfToString(Cpf cpf) {
        return cpf != null ? cpf.getValor() : null;
    }

    /**
     * Email → String: Apenas extrai o valor
     */
    @Named("emailToString")
    default String emailToString(Email email) {
        return email != null ? email.getValor() : null;
    }

    /**
     * StatusColaborador → String: Apenas extrai o valor
     */
    @Named("statusToString")
    default String statusToString(StatusColaborador status) {
        return status != null ? status.getValor() : null;
    }
}
