package com.peopleflow.pessoascontratos.inbound.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para registro de um documento do contrato")
public class DocumentoContratoRequest {

    @NotBlank(message = "tipo é obrigatório")
    @Schema(
            description = "Tipo do documento",
            allowableValues = {"contrato", "atestado", "exame", "certidao", "comprovante", "documento", "outro"},
            example = "contrato"
    )
    private String tipo;

    @NotBlank(message = "nomeArquivo é obrigatório")
    @Schema(description = "Nome original do arquivo", example = "contrato_clt_2025.pdf")
    private String nomeArquivo;

    @NotBlank(message = "mimeType é obrigatório")
    @Schema(description = "MIME type do arquivo", example = "application/pdf")
    private String mimeType;

    @NotNull(message = "tamanhoBytes é obrigatório")
    @Min(value = 0, message = "tamanhoBytes deve ser não-negativo")
    @Schema(description = "Tamanho do arquivo em bytes", example = "204800")
    private Long tamanhoBytes;

    @NotBlank(message = "storageKey é obrigatório")
    @Schema(description = "Chave de localização do arquivo no storage", example = "contratos/42/contrato_clt_2025.pdf")
    private String storageKey;
}
