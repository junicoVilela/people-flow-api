package com.peopleflow.pessoascontratos.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColaboradorFilter {
    
    private String nome;
    private String cpf;
    private String email;
    private String matricula;
    private String status;
    private Long clienteId;
    private Long empresaId;
    private Long departamentoId;
    private Long centroCustoId;
    private LocalDate dataAdmissaoInicio;
    private LocalDate dataAdmissaoFim;
    private LocalDate dataDemissaoInicio;
    private LocalDate dataDemissaoFim;

    public boolean hasAnyCriteria() {
        return nome != null || 
               cpf != null || 
               email != null || 
               matricula != null || 
               status != null ||
               clienteId != null || 
               empresaId != null || 
               departamentoId != null || 
               centroCustoId != null ||
               dataAdmissaoInicio != null || 
               dataAdmissaoFim != null ||
               dataDemissaoInicio != null || 
               dataDemissaoFim != null;
    }

    public boolean hasDataAdmissaoRange() {
        return dataAdmissaoInicio != null || dataAdmissaoFim != null;
    }

    public boolean hasDataDemissaoRange() {
        return dataDemissaoInicio != null || dataDemissaoFim != null;
    }
}

