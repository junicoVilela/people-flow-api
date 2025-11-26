package com.peopleflow.common.util;

import com.peopleflow.common.exception.DuplicateResourceException;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 * Utilitários comuns para serviços de domínio
 */
public final class ServiceUtils {

    private ServiceUtils() {}

    /**
     * Compara dois valores e adiciona o nome do campo à lista se forem diferentes
     * 
     * @param lista Lista de campos alterados
     * @param nomeCampo Nome do campo
     * @param valorOriginal Valor original
     * @param valorAtualizado Valor atualizado
     */
    public static void compararEAdicionar(List<String> lista, String nomeCampo, Object valorOriginal, Object valorAtualizado) {
        if (!Objects.equals(valorOriginal, valorAtualizado)) {
            lista.add(nomeCampo);
        }
    }

    /**
     * Valida unicidade de um campo para criação
     * 
     * @param nomeCampo Nome do campo para mensagem de erro
     * @param valor Valor a ser validado
     * @param validador Função que verifica se o valor já existe
     * @throws DuplicateResourceException se o valor já existir
     */
    public static void validarUnicidadeCampo(
            String nomeCampo,
            String valor,
            Predicate<String> validador) {

        if (validador.test(valor)) {
            throw new DuplicateResourceException(nomeCampo, valor);
        }
    }

    /**
     * Valida unicidade de um campo para atualização (excluindo o próprio registro)
     * 
     * @param nomeCampo Nome do campo para mensagem de erro
     * @param valor Valor a ser validado
     * @param idExcluir ID do registro a ser excluído da validação
     * @param validador Função que verifica se o valor já existe excluindo o ID
     * @throws DuplicateResourceException se o valor já existir em outro registro
     */
    public static void validarUnicidadeCampoComExclusao(
            String nomeCampo,
            String valor,
            Long idExcluir,
            BiPredicate<String, Long> validador) {

        if (validador.test(valor, idExcluir)) {
            throw new DuplicateResourceException(nomeCampo, valor);
        }
    }
}

