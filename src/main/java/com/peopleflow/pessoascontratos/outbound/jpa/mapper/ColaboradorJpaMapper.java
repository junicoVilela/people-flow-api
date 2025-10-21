package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.valueobject.Cpf;
import com.peopleflow.pessoascontratos.core.valueobject.Email;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import com.peopleflow.pessoascontratos.outbound.jpa.entity.ColaboradorEntity;
import org.springframework.stereotype.Component;

@Component
public class ColaboradorJpaMapper {

    public Colaborador toDomain(ColaboradorEntity entity) {
        if (entity == null) return null;

        Colaborador colaborador = new Colaborador();
        colaborador.setId(entity.getId());
        colaborador.setClienteId(entity.getClienteId());
        colaborador.setEmpresaId(entity.getEmpresaId());
        colaborador.setDepartamentoId(entity.getDepartamentoId());
        colaborador.setCentroCustoId(entity.getCentroCustoId());
        colaborador.setNome(entity.getNome());
        colaborador.setCpf(entity.getCpf() != null ? new Cpf(entity.getCpf()) : null);
        colaborador.setMatricula(entity.getMatricula());
        colaborador.setEmail(entity.getEmail() != null ? new Email(entity.getEmail()) : null);
        colaborador.setDataAdmissao(entity.getDataAdmissao());
        colaborador.setDataDemissao(entity.getDataDemissao());
        colaborador.setStatus(entity.getStatus() != null ? StatusColaborador.of(entity.getStatus()) : null);

        return colaborador;
    }

    public ColaboradorEntity toEntity(Colaborador colaborador) {
        if (colaborador == null) return null;

        ColaboradorEntity entity = new ColaboradorEntity();
        
        // Só seta o ID se não for null (para updates)
        if (colaborador.getId() != null) {
            entity.setId(colaborador.getId());
        }
        
        entity.setClienteId(colaborador.getClienteId());
        entity.setEmpresaId(colaborador.getEmpresaId());
        entity.setDepartamentoId(colaborador.getDepartamentoId());
        entity.setCentroCustoId(colaborador.getCentroCustoId());
        entity.setNome(colaborador.getNome());
        entity.setCpf(colaborador.getCpf() != null ? colaborador.getCpf().getValor() : null);
        entity.setMatricula(colaborador.getMatricula());
        entity.setEmail(colaborador.getEmail() != null ? colaborador.getEmail().getValor() : null);
        entity.setDataAdmissao(colaborador.getDataAdmissao());
        entity.setDataDemissao(colaborador.getDataDemissao());
        entity.setStatus(colaborador.getStatus() != null ? colaborador.getStatus().getValor() : null);

        return entity;
    }
}
