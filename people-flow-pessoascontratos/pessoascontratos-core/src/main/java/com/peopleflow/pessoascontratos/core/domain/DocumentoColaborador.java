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
public class DocumentoColaborador {

    private Long id;
    private Long colaboradorId;
    private TipoDocumento tipo;
    private String nomeArquivo;
    private String mimeType;
    private Long tamanhoBytes;
    private String storageKey;
    private Integer versao;

    public static DocumentoColaborador novoDocumento(
            Long colaboradorId,
            TipoDocumento tipo,
            String nomeArquivo,
            String mimeType,
            Long tamanhoBytes,
            String storageKey) {

        DocumentoColaborador documento = DocumentoColaborador.builder()
                .colaboradorId(colaboradorId)
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
        if (colaboradorId == null) {
            throw new BusinessException("COLABORADOR_ID_OBRIGATORIO", "colaboradorId é obrigatório para um documento");
        }
        if (tipo == null) {
            throw new BusinessException("TIPO_OBRIGATORIO", "tipo é obrigatório para um documento");
        }
        if (nomeArquivo == null || nomeArquivo.trim().isEmpty()) {
            throw new BusinessException("NOME_ARQUIVO_OBRIGATORIO", "nomeArquivo é obrigatório para um documento");
        }
        if (mimeType == null || mimeType.trim().isEmpty()) {
            throw new BusinessException("MIME_TYPE_OBRIGATORIO", "mimeType é obrigatório para um documento");
        }
        if (tamanhoBytes == null || tamanhoBytes < 0) {
            throw new BusinessException("TAMANHO_BYTES_INVALIDO", "tamanhoBytes deve ser um valor não-negativo");
        }
        if (storageKey == null || storageKey.trim().isEmpty()) {
            throw new BusinessException("STORAGE_KEY_OBRIGATORIO", "storageKey é obrigatório para um documento");
        }
    }
}
