package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.Colaborador;
import com.peopleflow.common.pagination.PagedResult;
import com.peopleflow.pessoascontratos.core.query.ColaboradorFilter;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

    /**
     * Converte PagedResult (core) para Page (Spring) para resposta HTTP
     */
    default Page<ColaboradorResponse> toPageResponse(PagedResult<Colaborador> pagedResult) {
        if (pagedResult == null) return null;
        
        PageRequest pageRequest = PageRequest.of(pagedResult.page(), pagedResult.size());
        return new PageImpl<>(
            pagedResult.content().stream()
                .map(this::toResponse)
                .toList(),
            pageRequest,
            pagedResult.totalElements()
        );
    }
    
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

    @Named("stringToCpf")
    default Cpf stringToCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return null; // Transformação: null/vazio → null
        }
        return new Cpf(cpf);
    }

    @Named("stringToEmail")
    default Email stringToEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return new Email(email);
    }

    @Named("stringToStatus")
    default StatusColaborador stringToStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return StatusColaborador.ATIVO;
        }
        return StatusColaborador.of(status);
    }
}
