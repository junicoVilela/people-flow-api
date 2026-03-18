package com.peopleflow.organizacao.outbound.jpa.mapper;

import com.peopleflow.organizacao.core.domain.Area;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import com.peopleflow.organizacao.outbound.jpa.entity.AreaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AreaJpaMapper {

    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Area toDomain(AreaEntity entity);

    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    AreaEntity toEntity(Area area);

    @Named("stringToStatus")
    default StatusOrganizacao stringToStatus(String status) {
        return status != null ? StatusOrganizacao.of(status) : null;
    }

    @Named("statusToString")
    default String statusToString(StatusOrganizacao status) {
        return status != null ? status.getValor() : null;
    }
}
