package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorFilter;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorFilterRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ColaboradorWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clienteId", ignore = true)
    @Mapping(target = "empresaId", ignore = true)
    @Mapping(target = "departamentoId", ignore = true)
    @Mapping(target = "centroCustoId", ignore = true)
    @Mapping(target = "dataDemissao", ignore = true)
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

    @Named("cpfToString")
    default String cpfToString(com.peopleflow.pessoascontratos.core.valueobject.Cpf cpf) {
        return cpf != null ? cpf.getValor() : null;
    }

    @Named("emailToString")
    default String emailToString(com.peopleflow.pessoascontratos.core.valueobject.Email email) {
        return email != null ? email.getValor() : null;
    }

    @Named("statusToString")
    default String statusToString(com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador status) {
        return status != null ? status.getValor() : null;
    }
}
