package com.peopleflow.pessoascontratos.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.pessoascontratos.core.valueobject.RegimeContrato;
import com.peopleflow.pessoascontratos.core.valueobject.TipoContrato;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Contrato {

    private Long id;
    private Long colaboradorId;
    private Long jornadaId;
    private Long cargoId;
    private TipoContrato tipo;
    private RegimeContrato regime;
    private BigDecimal salarioBase;
    private LocalDate inicio;
    private LocalDate fim;

    public static Contrato novo(
            Long colaboradorId,
            Long jornadaId,
            Long cargoId,
            LocalDate inicio,
            LocalDate fim,
            TipoContrato tipo,
            RegimeContrato regime,
            BigDecimal salarioBase) {

        Contrato c = Contrato.builder()
                .colaboradorId(colaboradorId)
                .jornadaId(jornadaId)
                .cargoId(cargoId)
                .inicio(inicio)
                .fim(fim)
                .tipo(tipo)
                .regime(regime)
                .salarioBase(salarioBase)
                .build();
        c.validar();
        return c;
    }

    public Contrato atualizar(
            Long jornadaId,
            Long cargoId,
            LocalDate inicio,
            LocalDate fim,
            TipoContrato tipo,
            RegimeContrato regime,
            BigDecimal salarioBase) {

        Contrato c = this.toBuilder()
                .jornadaId(jornadaId)
                .cargoId(cargoId)
                .inicio(inicio)
                .fim(fim)
                .tipo(tipo)
                .regime(regime)
                .salarioBase(salarioBase)
                .build();
        c.validar();
        return c;
    }

    public void validar() {
        if (colaboradorId == null) {
            throw new BusinessException("CONTRATO_COLABORADOR_OBRIGATORIO", "colaboradorId é obrigatório");
        }
        if (jornadaId == null) {
            throw new BusinessException("CONTRATO_JORNADA_OBRIGATORIO", "jornadaId é obrigatório");
        }
        if (cargoId == null) {
            throw new BusinessException("CONTRATO_CARGO_OBRIGATORIO", "cargoId é obrigatório");
        }
        if (inicio == null) {
            throw new BusinessException("CONTRATO_INICIO_OBRIGATORIO", "Data de início é obrigatória");
        }
        if (fim != null && fim.isBefore(inicio)) {
            throw new BusinessException("CONTRATO_DATAS_INVALIDAS", "Data fim não pode ser anterior à data de início");
        }
        if (salarioBase != null && salarioBase.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("CONTRATO_SALARIO_INVALIDO", "Salário base não pode ser negativo");
        }
    }
}
