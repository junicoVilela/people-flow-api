package com.peopleflow.organizacao.outbound.jpa.mapper;

import com.peopleflow.organizacao.core.domain.Unidade;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import com.peopleflow.organizacao.outbound.jpa.entity.UnidadeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UnidadeJpaMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Unidade toDomain(UnidadeEntity entity);

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    UnidadeEntity toEntity(Unidade unidade);

    @Named("stringToStatus")
    default StatusOrganizacao stringToStatus(String status) {
        return status != null ? StatusOrganizacao.of(status) : null;
    }

    @Named("statusToString")
    default String statusToString(StatusOrganizacao status) {
        return status != null ? status.getValor() : null;
    }
}
