package com.peopleflow.organizacao.outbound.jpa.mapper;

import com.peopleflow.common.valueobject.Cnpj;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.valueobjects.StatusEmpresa;
import com.peopleflow.organizacao.outbound.jpa.entity.EmpresaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EmpresaJpaMapper {

    @Mapping(target = "cnpj", source = "cnpj", qualifiedByName = "stringToCnpj")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Empresa toDomain(EmpresaEntity entity);

    @Mapping(target = "cnpj", source = "cnpj", qualifiedByName = "cnpjToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    EmpresaEntity toEntity(Empresa empresa);

    @Named("stringToCnpj")
    default Cnpj stringToCnpj(String cnpj) {
        return cnpj != null ? new Cnpj(cnpj) : null;
    }

    @Named("stringToStatus")
    default StatusEmpresa stringToStatus(String status) {
        return status != null ? StatusEmpresa.of(status) : null;
    }

    @Named("cnpjToString")
    default String cnpjToString(Cnpj cnpj) {
        return cnpj != null ? cnpj.getValor() : null;
    }

    @Named("statusToString")
    default String statusToString(StatusEmpresa status) {
        return status != null ? status.getValor() : null;
    }


}
