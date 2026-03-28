package com.peopleflow.pessoascontratos.inbound.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoContratoResponse {

    private Long id;
    private Long contratoId;
    private String tipo;
    private String nomeArquivo;
    private String mimeType;
    private Long tamanhoBytes;
    private String storageKey;
    private Integer versao;
}
