package com.peopleflow.organizacao.outbound.jpa.mapper;

import com.peopleflow.common.valueobject.Cnpj;
import com.peopleflow.common.valueobject.InscricaoEstadual;
import com.peopleflow.organizacao.core.domain.Empresa;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import com.peopleflow.organizacao.outbound.jpa.entity.EmpresaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EmpresaJpaMapper {

    @Mapping(target = "cnpj", source = "cnpj", qualifiedByName = "stringToCnpj")
    @Mapping(target = "inscricaoEstadual", source = "inscricaoEstadual", qualifiedByName = "stringToInscricaoEstadual")
    @Mapping(target = "status", source = "status", qualifiedByName = "stringToStatus")
    Empresa toDomain(EmpresaEntity entity);

    @Mapping(target = "cnpj", source = "cnpj", qualifiedByName = "cnpjToString")
    @Mapping(target = "inscricaoEstadual", source = "inscricaoEstadual", qualifiedByName = "inscricaoEstadualToString")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusToString")
    EmpresaEntity toEntity(Empresa empresa);

    @Named("stringToCnpj")
    default Cnpj stringToCnpj(String cnpj) {
        return Cnpj.fromStorage(cnpj);
    }

    @Named("stringToInscricaoEstadual")
    default InscricaoEstadual stringToInscricaoEstadual(String ie) {
        return InscricaoEstadual.of(ie);
    }

    @Named("stringToStatus")
    default StatusOrganizacao stringToStatus(String status) {
        return status != null ? StatusOrganizacao.of(status) : null;
    }

    @Named("cnpjToString")
    default String cnpjToString(Cnpj cnpj) {
        return cnpj != null ? cnpj.getValor() : null;
    }

    @Named("inscricaoEstadualToString")
    default String inscricaoEstadualToString(InscricaoEstadual ie) {
        return ie != null ? ie.getValor() : null;
    }

    @Named("statusToString")
    default String statusToString(StatusOrganizacao status) {
        return status != null ? status.getValor() : null;
    }


}
