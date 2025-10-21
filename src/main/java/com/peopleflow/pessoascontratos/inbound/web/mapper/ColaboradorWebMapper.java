package com.peopleflow.pessoascontratos.inbound.web.mapper;

import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.ports.out.ColaboradorFiltros;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorFiltrosRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorPageResponse;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorRequest;
import com.peopleflow.pessoascontratos.inbound.web.dto.ColaboradorResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;

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
        response.setCpf(colaborador.getCpf() != null ? colaborador.getCpf().getValor() : null);
        response.setMatricula(colaborador.getMatricula());
        response.setEmail(colaborador.getEmail() != null ? colaborador.getEmail().getValor() : null);
        response.setDataAdmissao(colaborador.getDataAdmissao());
        response.setDataDemissao(colaborador.getDataDemissao());
        response.setStatus(colaborador.getStatus() != null ? colaborador.getStatus().getValor() : null);

        return response;
    }

    public ColaboradorPageResponse toPageResponse(Page<Colaborador> page) {
        if (page == null) return null;

        List<ColaboradorResponse> content = page.getContent()
                .stream()
                .map(this::toResponse)
                .toList();

        return new ColaboradorPageResponse(
            content,
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isFirst(),
            page.isLast(),
            page.hasNext(),
            page.hasPrevious()
        );
    }

    public ColaboradorFiltros toDomain(ColaboradorFiltrosRequest request) {
        if (request == null) return null;

        return ColaboradorFiltros.builder()
            .nome(request.getNome())
            .cpf(request.getCpf())
            .email(request.getEmail())
            .matricula(request.getMatricula())
            .status(request.getStatus())
            .clienteId(request.getClienteId())
            .empresaId(request.getEmpresaId())
            .departamentoId(request.getDepartamentoId())
            .centroCustoId(request.getCentroCustoId())
            .dataAdmissaoInicio(request.getDataAdmissaoInicio())
            .dataAdmissaoFim(request.getDataAdmissaoFim())
            .dataDemissaoInicio(request.getDataDemissaoInicio())
            .dataDemissaoFim(request.getDataDemissaoFim())
            .build();
    }
}
