package com.peopleflow.pessoascontratos.core.domain;

import com.peopleflow.common.exception.BusinessException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JornadaTrabalho {

    private Long id;
    private String descricao;
    private BigDecimal cargaSemanalHoras;

    public static JornadaTrabalho nova(String descricao, BigDecimal cargaSemanalHoras) {
        JornadaTrabalho j = JornadaTrabalho.builder()
                .descricao(trimDesc(descricao))
                .cargaSemanalHoras(cargaSemanalHoras)
                .build();
        j.validar();
        return j;
    }

    public JornadaTrabalho atualizar(String descricao, BigDecimal cargaSemanalHoras) {
        JornadaTrabalho j = this.toBuilder()
                .descricao(trimDesc(descricao))
                .cargaSemanalHoras(cargaSemanalHoras)
                .build();
        j.validar();
        return j;
    }

    public void validar() {
        if (descricao == null || descricao.isBlank()) {
            throw new BusinessException("JORNADA_DESCRICAO_OBRIGATORIA", "Descrição é obrigatória");
        }
        if (cargaSemanalHoras != null) {
            if (cargaSemanalHoras.compareTo(BigDecimal.ZERO) < 0
                    || cargaSemanalHoras.compareTo(new BigDecimal("60")) > 0) {
                throw new BusinessException(
                        "JORNADA_CARGA_INVALIDA",
                        "Carga semanal deve estar entre 0 e 60 horas");
            }
        }
    }

    private static String trimDesc(String s) {
        return s == null ? null : s.trim();
    }
}
