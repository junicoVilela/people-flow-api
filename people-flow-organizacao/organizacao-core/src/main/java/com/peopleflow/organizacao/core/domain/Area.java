package com.peopleflow.organizacao.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.organizacao.core.valueobjects.StatusOrganizacao;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Area {

    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private StatusOrganizacao status;

    public static class AreaBuilder {
        public Area build() {
            if (status == null) {
                status = StatusOrganizacao.ATIVO;
            }
            Area area = new Area(
                    id,
                    codigo,
                    nome,
                    descricao,
                    status);
            if (area.nome != null) {
                area.validarInvariantes();
            }
            return area;
        }
    }

    private void validarInvariantes() {
        if (nome == null || nome.trim().isEmpty()) {
            throw new BusinessException("NOME_OBRIGATORIO", "Nome é obrigatório");
        }
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new BusinessException("CODIGO_OBRIGATORIO", "Código é obrigatório");
        }
    }

    public static Area nova(String codigo, String nome, String descricao) {
        return Area.builder()
                .codigo(codigo)
                .nome(nome)
                .descricao(descricao)
                .build();
    }

    public Area atualizar(String codigo, String nome, String descricao) {
        return this.toBuilder()
                .codigo(codigo)
                .nome(nome)
                .descricao(descricao)
                .build();
    }

    public Area ativar() {
        if (isExcluido()) {
            throw new BusinessException("AREA_EXCLUIDA", "Não é possível ativar área excluída");
        }
        return this.toBuilder().status(StatusOrganizacao.ATIVO).build();
    }

    public Area inativar() {
        return this.toBuilder().status(StatusOrganizacao.INATIVO).build();
    }

    public Area excluir() {
        if (isExcluido()) {
            throw new BusinessException("AREA_JA_EXCLUIDA", "Área já está excluída");
        }
        return this.toBuilder().status(StatusOrganizacao.EXCLUIDO).build();
    }

    public boolean isExcluido() {
        return status != null && status.isExcluido();
    }
}
