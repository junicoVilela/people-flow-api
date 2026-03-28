package com.peopleflow.pessoascontratos.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.pessoascontratos.core.valueobject.ParentescoDependente;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Dependente {

    private Long id;
    private Long colaboradorId;
    private String nome;
    private ParentescoDependente parentesco;
    private LocalDate dataNascimento;
    private String cpf;

    public static Dependente novo(
            Long colaboradorId,
            String nome,
            ParentescoDependente parentesco,
            LocalDate dataNascimento,
            String cpf) {
        Dependente d = Dependente.builder()
                .colaboradorId(colaboradorId)
                .nome(trimReq(nome))
                .parentesco(parentesco)
                .dataNascimento(dataNascimento)
                .cpf(trimNull(cpf))
                .build();
        d.validar();
        return d;
    }

    public Dependente atualizar(String nome, ParentescoDependente parentesco, LocalDate dataNascimento, String cpf) {
        Dependente d = this.toBuilder()
                .nome(trimReq(nome))
                .parentesco(parentesco)
                .dataNascimento(dataNascimento)
                .cpf(trimNull(cpf))
                .build();
        d.validar();
        return d;
    }

    public void validar() {
        if (colaboradorId == null) {
            throw new BusinessException("DEPENDENTE_COLAB_OBRIGATORIO", "Colaborador é obrigatório");
        }
        if (nome == null || nome.isBlank()) {
            throw new BusinessException("DEPENDENTE_NOME_OBRIGATORIO", "Nome é obrigatório");
        }
        if (parentesco == null) {
            throw new BusinessException("DEPENDENTE_PARENTESCO_OBRIGATORIO", "Parentesco é obrigatório");
        }
        if (cpf != null && cpf.length() != 14) {
            throw new BusinessException("DEPENDENTE_CPF_TAMANHO", "CPF deve ter 14 caracteres (formato 999.999.999-99) ou ficar em branco");
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
