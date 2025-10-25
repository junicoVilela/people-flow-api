package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.model.ColaboradorFilter;
import com.peopleflow.pessoascontratos.core.valueobject.Cpf;
import com.peopleflow.pessoascontratos.core.valueobject.Email;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorFilterRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

/**
 * Mapper de conversão entre DTOs da camada Web e objetos de Domínio
 * 
 * IMPORTANTE: Este mapper NÃO faz validações, apenas TRANSFORMAÇÕES.
 * 
 * Validações são feitas em:
 * - DTO (Request): Bean Validation (@NotBlank, @Pattern, etc)
 * - Domínio (Value Objects): Validações completas de negócio
 * 
 * Responsabilidades deste mapper:
 * - Converter tipos primitivos para Value Objects
 * - Converter Value Objects para tipos primitivos
 * - Transformar null/vazio adequadamente
 */
@Mapper(componentModel = "spring")
public interface ColaboradorWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clienteId", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "departamentoId", ignore = true)
    @Mapping(target = "centroCustoId", ignore = true)
    @Mapping(target = "dataDemissao", ignore = true)
    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "stringToCpf")
    @Mapping(target = "email", source = "email", qualifiedByName = "stringToEmail")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Colaborador toDomain(ColaboradorRequest request);

    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "cpfToString")
    @Mapping(target = "email", source = "email", qualifiedByName = "emailToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    ColaboradorResponse toResponse(Colaborador colaborador);

    ColaboradorFilter toDomain(ColaboradorFilterRequest request);

    default Page<ColaboradorResponse> toPageResponse(Page<Colaborador> page) {
        if (page == null) return null;
        return page.map(this::toResponse);
    }

    // ===== Conversões de Domain para String (Response) =====
    
    @Named("cpfToString")
    default String cpfToString(Cpf cpf) {
        return cpf != null ? cpf.getValor() : null;
    }

    @Named("emailToString")
    default String emailToString(Email email) {
        return email != null ? email.getValor() : null;
    }

    @Named("statusToString")
    default String statusToString(StatusColaborador status) {
        return status != null ? status.getValor() : null;
    }

    // ===== Conversões de String para Domain (Request) =====
    // NOTA: Não fazemos validações aqui, apenas transformações.
    // As validações são feitas:
    // 1. No DTO (@NotBlank, @Pattern) - validação de entrada
    // 2. No Value Object (new Cpf, new Email) - validação de domínio
    
    /**
     * Converte String para CPF (Value Object)
     * Não valida - a validação é feita pelo próprio Value Object Cpf
     */
    @Named("stringToCpf")
    default Cpf stringToCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return null; // Transformação: null/vazio → null
        }
        return new Cpf(cpf); // CPF valida internamente
    }

    /**
     * Converte String para Email (Value Object)
     * Não valida - a validação é feita pelo próprio Value Object Email
     */
    @Named("stringToEmail")
    default Email stringToEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null; // Transformação: null/vazio → null
        }
        return new Email(email); // Email valida internamente
    }

    /**
     * Converte String para StatusColaborador (Value Object)
     * Não valida - a validação é feita por StatusColaborador.of()
     */
    @Named("stringToStatus")
    default StatusColaborador stringToStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return StatusColaborador.ATIVO; // Transformação: vazio → padrão ATIVO
        }
        return StatusColaborador.of(status); // StatusColaborador valida internamente
    }
}
