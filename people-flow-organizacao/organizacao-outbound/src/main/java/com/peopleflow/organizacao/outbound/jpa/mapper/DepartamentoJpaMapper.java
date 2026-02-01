package com.peopleflow.organizacao.outbound.jpa.mapper;

import com.peopleflow.organizacao.core.domain.Departamento;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import com.peopleflow.organizacao.outbound.jpa.entity.DepartamentoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DepartamentoJpaMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Departamento toDomain(DepartamentoEntity entity);

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    DepartamentoEntity toEntity(Departamento departamento);

    @Named("stringToStatus")
    default StatusOrganizacao stringToStatus(String status) {
        return status != null ? StatusOrganizacao.of(status) : null;
    }

    @Named("statusToString")
    default String statusToString(StatusOrganizacao status) {
        return status != null ? status.getValor() : null;
    }
}
