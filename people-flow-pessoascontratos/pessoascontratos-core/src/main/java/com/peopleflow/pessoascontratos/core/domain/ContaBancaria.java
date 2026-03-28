package com.peopleflow.pessoascontratos.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.pessoascontratos.core.valueobject.TipoContaBancaria;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContaBancaria {

    private Long id;
    private Long colaboradorId;
    private String banco;
    private String agencia;
    private String conta;
    private TipoContaBancaria tipo;
    private String pix;

    public static ContaBancaria novo(
            Long colaboradorId,
            String banco,
            String agencia,
            String conta,
            TipoContaBancaria tipo,
            String pix) {
        ContaBancaria c = ContaBancaria.builder()
                .colaboradorId(colaboradorId)
                .banco(trimReq(banco))
                .agencia(trimReq(agencia))
                .conta(trimReq(conta))
                .tipo(tipo)
                .pix(trimNull(pix))
                .build();
        c.validar();
        return c;
    }

    public ContaBancaria atualizar(String banco, String agencia, String conta, TipoContaBancaria tipo, String pix) {
        ContaBancaria c = this.toBuilder()
                .banco(trimReq(banco))
                .agencia(trimReq(agencia))
                .conta(trimReq(conta))
                .tipo(tipo)
                .pix(trimNull(pix))
                .build();
        c.validar();
        return c;
    }

    public void validar() {
        if (colaboradorId == null) {
            throw new BusinessException("CONTA_COLAB_OBRIGATORIO", "Colaborador é obrigatório");
        }
        if (banco == null || banco.isBlank()) {
            throw new BusinessException("CONTA_BANCO_OBRIGATORIO", "Banco é obrigatório");
        }
        if (agencia == null || agencia.isBlank()) {
            throw new BusinessException("CONTA_AGENCIA_OBRIGATORIA", "Agência é obrigatória");
        }
        if (conta == null || conta.isBlank()) {
            throw new BusinessException("CONTA_NUMERO_OBRIGATORIO", "Conta é obrigatória");
        }
    }

    private static String trimReq(String s) {
        return s == null ? null : s.trim();
    }

    private static String trimNull(String s) {
        if (s == null) {
            return null;
        }
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
