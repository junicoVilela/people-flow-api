package com.peopleflow.pessoascontratos.outbound.jpa.mapper;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
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
        colaborador.setCpf(entity.getCpf());
        colaborador.setMatricula(entity.getMatricula());
        colaborador.setEmail(entity.getEmail());
        colaborador.setDataAdmissao(entity.getDataAdmissao());
        colaborador.setDataDemissao(entity.getDataDemissao());
        colaborador.setStatus(entity.getStatus());
        colaborador.setCriadoEm(entity.getCriadoEm());
        colaborador.setAtualizadoEm(entity.getAtualizadoEm());

        return colaborador;
    }

    public ColaboradorEntity toEntity(Colaborador colaborador) {
        if (colaborador == null) return null;

        ColaboradorEntity entity = new ColaboradorEntity();
        entity.setId(colaborador.getId());
        entity.setClienteId(colaborador.getClienteId());
        entity.setEmpresaId(colaborador.getEmpresaId());
        entity.setDepartamentoId(colaborador.getDepartamentoId());
        entity.setCentroCustoId(colaborador.getCentroCustoId());
        entity.setNome(colaborador.getNome());
        entity.setCpf(colaborador.getCpf());
        entity.setMatricula(colaborador.getMatricula());
        entity.setEmail(colaborador.getEmail());
        entity.setDataAdmissao(colaborador.getDataAdmissao());
        entity.setDataDemissao(colaborador.getDataDemissao());
        entity.setStatus(colaborador.getStatus());
        entity.setCriadoEm(colaborador.getCriadoEm());
        entity.setAtualizadoEm(colaborador.getAtualizadoEm());

        return entity;
    }
} 