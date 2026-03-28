package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.domain.Dependente;
import com.peopleflow.pessoascontratos.core.valueobject.ParentescoDependente;
import com.peopleflow.pessoascontratos.inbound.web.dto.DependenteRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.DependenteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DependenteWebMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colaboradorId", ignore = true)
    @Mapping(target = "parentesco", source = "parentesco", qualifiedByName = "strToParentesco")
    Dependente toDomain(DependenteRequest request);

    @Mapping(target = "parentesco", source = "parentesco", qualifiedByName = "parentescoToStr")
    DependenteResponse toResponse(Dependente dependente);

    @Named("strToParentesco")
    default ParentescoDependente strToParentesco(String valor) {
        return ParentescoDependente.of(valor);
    }

    @Named("parentescoToStr")
    default String parentescoToStr(ParentescoDependente p) {
        return p != null ? p.getValor() : null;
    }
}
