package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.domain.Dependente;
import com.peopleflow.pessoascontratos.core.valueobject.ParentescoDependente;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.DependenteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DependenteJpaMapper {

    @Mapping(target = "parentesco", source = "parentesco", qualifiedByName = "strToParentesco")
    Dependente toDomain(DependenteEntity entity);

    @Mapping(target = "parentesco", source = "parentesco", qualifiedByName = "parentescoToStr")
    DependenteEntity toEntity(Dependente dependente);

    @Named("strToParentesco")
    default ParentescoDependente strToParentesco(String valor) {
        return valor != null ? ParentescoDependente.of(valor) : null;
    }

    @Named("parentescoToStr")
    default String parentescoToStr(ParentescoDependente p) {
        return p != null ? p.getValor() : null;
    }
}
