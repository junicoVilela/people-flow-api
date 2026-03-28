package com.peopleflow.pessoascontratos.core.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoContratoFilter {

    private String tipo;
    private String nomeArquivo;

    public boolean hasAnyCriteria() {
        return (tipo != null && !tipo.isBlank()) || (nomeArquivo != null && !nomeArquivo.isBlank());
    }
}
