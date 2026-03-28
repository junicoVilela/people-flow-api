package com.peopleflow.pessoascontratos.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.pessoascontratos.core.valueobject.TipoDocumento;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DocumentoContrato {

    private Long id;
    private Long contratoId;
    private TipoDocumento tipo;
    private String nomeArquivo;
    private String mimeType;
    private Long tamanhoBytes;
    private String storageKey;
    private Integer versao;

    public static DocumentoContrato novoDocumento(
            Long contratoId,
            TipoDocumento tipo,
            String nomeArquivo,
            String mimeType,
            Long tamanhoBytes,
            String storageKey) {

        DocumentoContrato documento = DocumentoContrato.builder()
                .contratoId(contratoId)
                .tipo(tipo)
                .nomeArquivo(nomeArquivo)
                .mimeType(mimeType)
                .tamanhoBytes(tamanhoBytes)
                .storageKey(storageKey)
                .versao(1)
                .build();

        documento.validarInvariantes();
        return documento;
    }

    public void validarInvariantes() {
        if (contratoId == null) {
            throw new BusinessException("CONTRATO_ID_OBRIGATORIO", "contratoId é obrigatório para o documento");
        }
        if (tipo == null) {
            throw new BusinessException("TIPO_OBRIGATORIO", "tipo é obrigatório para o documento");
        }
        if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
            throw new BusinessException("NOME_ARQUIVO_OBRIGATORIO", "nomeArquivo é obrigatório para o documento");
        }
        if (mimeType == null || mimeType.trim().isEmpty()) {
            throw new BusinessException("MIME_TYPE_OBRIGATORIO", "mimeType é obrigatório para o documento");
        }
        if (tamanhoBytes == null || tamanhoBytes < 0) {
            throw new BusinessException("TAMANHO_BYTES_INVALIDO", "tamanhoBytes deve ser um valor não-negativo");
        }
        if (storageKey == null || storageKey.trim().isEmpty()) {
            throw new BusinessException("STORAGE_KEY_OBRIGATORIO", "storageKey é obrigatório para o documento");
        }
    }
}
