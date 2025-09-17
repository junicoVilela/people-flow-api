package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorResponse;
import org.springframework.stereotype.Component;

@Component
public class ColaboradorWebMapper {

    public Colaborador toDomain(ColaboradorRequest request) {
        if (request == null) return null;

        Colaborador colaborador = new Colaborador();
        colaborador.setNome(request.getNome());
        colaborador.setCpf(request.getCpf());
        colaborador.setMatricula(request.getMatricula());
        colaborador.setEmail(request.getEmail());
        colaborador.setDataAdmissao(request.getDataAdmissao());
        colaborador.setStatus(request.getStatus());

        return colaborador;
    }

    public ColaboradorResponse toResponse(Colaborador colaborador) {
        if (colaborador == null) return null;

        ColaboradorResponse response = new ColaboradorResponse();
        response.setId(colaborador.getId());
        response.setClienteId(colaborador.getClienteId());
        response.setEmpresaId(colaborador.getEmpresaId());
        response.setDepartamentoId(colaborador.getDepartamentoId());
        response.setCentroCustoId(colaborador.getCentroCustoId());
        response.setNome(colaborador.getNome());
        response.setCpf(colaborador.getCpf());
        response.setMatricula(colaborador.getMatricula());
        response.setEmail(colaborador.getEmail());
        response.setDataAdmissao(colaborador.getDataAdmissao());
        response.setDataDemissao(colaborador.getDataDemissao());
        response.setStatus(colaborador.getStatus());
        response.setCriadoEm(colaborador.getCriadoEm());
        response.setAtualizadoEm(colaborador.getAtualizadoEm());

        return response;
    }
} 