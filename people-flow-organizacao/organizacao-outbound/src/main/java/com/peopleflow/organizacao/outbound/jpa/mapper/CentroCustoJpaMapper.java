package com.peopleflow.organizacao.outbound.jpa.mapper;

import com.peopleflow.organizacao.core.domain.CentroCusto;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import com.peopleflow.organizacao.outbound.jpa.entity.CentroCustoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CentroCustoJpaMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    CentroCusto toDomain(CentroCustoEntity entity);

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    CentroCustoEntity toEntity(CentroCusto centroCusto);

    @Named("stringToStatus")
    default StatusOrganizacao stringToStatus(String status) {
        return status != null ? StatusOrganizacao.of(status) : null;
    }

    @Named("statusToString")
    default String statusToString(StatusOrganizacao status) {
        return status != null ? status.getValor() : null;
    }
}
