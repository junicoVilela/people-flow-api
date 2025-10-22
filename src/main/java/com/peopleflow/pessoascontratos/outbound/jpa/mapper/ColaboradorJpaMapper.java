package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.valueobject.Cpf;
import com.peopleflow.pessoascontratos.core.valueobject.Email;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ColaboradorJpaMapper {

    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "stringToCpf")
    @Mapping(target = "email", source = "email", qualifiedByName = "stringToEmail")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Colaborador toDomain(ColaboradorEntity entity);

    @Mapping(target = "cpf", source = "cpf", qualifiedByName = "cpfToString")
    @Mapping(target = "email", source = "email", qualifiedByName = "emailToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    ColaboradorEntity toEntity(Colaborador colaborador);

    @Named("stringToCpf")
    default Cpf stringToCpf(String cpf) {
        return cpf != null ? new Cpf(cpf) : null;
    }

    @Named("stringToEmail")
    default Email stringToEmail(String email) {
        return email != null ? new Email(email) : null;
    }

    @Named("stringToStatus")
    default StatusColaborador stringToStatus(String status) {
        return status != null ? StatusColaborador.of(status) : null;
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
}
