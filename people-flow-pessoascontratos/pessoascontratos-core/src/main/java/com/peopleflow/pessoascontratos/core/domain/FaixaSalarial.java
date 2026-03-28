package com.peopleflow.pessoascontratos.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.pessoascontratos.core.valueobject.MoedaFaixa;
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
public class FaixaSalarial {

    private Long id;
    private Long cargoId;
    private BigDecimal faixaMin;
    private BigDecimal faixaMax;
    private MoedaFaixa moeda;

    public static FaixaSalarial nova(Long cargoId, BigDecimal faixaMin, BigDecimal faixaMax, MoedaFaixa moeda) {
        FaixaSalarial f = FaixaSalarial.builder()
                .cargoId(cargoId)
                .faixaMin(faixaMin)
                .faixaMax(faixaMax)
                .moeda(moeda != null ? moeda : MoedaFaixa.BRL)
                .build();
        f.validar();
        return f;
    }

    public FaixaSalarial atualizar(BigDecimal faixaMin, BigDecimal faixaMax, MoedaFaixa moeda) {
        FaixaSalarial f = this.toBuilder()
                .faixaMin(faixaMin)
                .faixaMax(faixaMax)
                .moeda(moeda != null ? moeda : MoedaFaixa.BRL)
                .build();
        f.validar();
        return f;
    }

    public void validar() {
        if (cargoId == null) {
            throw new BusinessException("FAIXA_CARGO_OBRIGATORIO", "cargoId é obrigatório");
        }
        if (faixaMin == null || faixaMax == null) {
            throw new BusinessException("FAIXA_VALORES_OBRIGATORIOS", "faixaMin e faixaMax são obrigatórios");
        }
        if (faixaMin.compareTo(BigDecimal.ZERO) < 0 || faixaMax.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("FAIXA_NEGATIVA", "Valores da faixa não podem ser negativos");
        }
        if (faixaMax.compareTo(faixaMin) < 0) {
            throw new BusinessException("FAIXA_MIN_MAX_INVALIDA", "faixaMax deve ser maior ou igual a faixaMin");
        }
        if (moeda == null) {
            throw new BusinessException("FAIXA_MOEDA_OBRIGATORIA", "Moeda é obrigatória");
        }
    }
}
