package com.peopleflow.common.valueobject;

import com.peopleflow.common.exception.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Value Object para Inscrição Estadual (IE)
 * 
 * Encapsula validação e formatação de Inscrição Estadual brasileira.
 * A validação é flexível pois cada estado brasileiro tem seu próprio formato.
 * Este é um value object genérico que pode ser reutilizado em qualquer módulo.
 */
@Getter
@EqualsAndHashCode
@ToString
public class InscricaoEstadual {
    private final String valor;

    /**
     * Construtor privado - use o método of() para criar instâncias
     */
    private InscricaoEstadual(String ie) {
        String ieLimpa = ie.replaceAll("[^0-9]", "");
        
        if (!isValidInscricaoEstadual(ieLimpa)) {
            throw new BusinessException("IE_INVALIDA", "Inscrição Estadual inválida: " + ie);
        }
        
        this.valor = formatarInscricaoEstadual(ieLimpa);
    }

    /**
     * Factory method que permite IE opcional (null)
     * Útil para casos onde a IE não é obrigatória
     */
    public static InscricaoEstadual of(String ie) {
        if (ie == null || ie.trim().isEmpty()) {
            return null;
        }
        return new InscricaoEstadual(ie);
    }

    public String getValorNumerico() {
        return valor.replaceAll("[^0-9]", "");
    }

    private boolean isValidInscricaoEstadual(String ie) {
        if (ie == null || ie.trim().isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        String ieLimpa = ie.replaceAll("[^0-9]", "");
        
        // Validações básicas
        // A IE deve ter entre 8 e 14 dígitos (varia por estado)
        if (ieLimpa.length() < 8 || ieLimpa.length() > 14) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais (ex: 00000000)
        if (ieLimpa.matches("(\\d)\\1+")) {
            return false;
        }
        
        // Validação básica de formato numérico
        // A validação completa por estado seria muito complexa,
        // então aceitamos qualquer sequência numérica válida
        return ieLimpa.matches("\\d{8,14}");
    }

    private String formatarInscricaoEstadual(String ie) {
        // Formatação padrão: XXX.XXX.XXX.XXX
        // Se tiver 9 dígitos: XXX.XXX.XXX
        // Se tiver 10 dígitos: XXX.XXX.XXX.X
        // Se tiver 11 dígitos: XXX.XXX.XXX.XX
        // Se tiver 12 dígitos: XXX.XXX.XXX.XXX
        // Se tiver 13 dígitos: XXX.XXX.XXX.XXXX
        // Se tiver 14 dígitos: XX.XXX.XXX.XXX.XXX
        
        String ieLimpa = ie.replaceAll("[^0-9]", "");
        
        if (ieLimpa.length() <= 9) {
            // Formato: XXX.XXX.XXX
            if (ieLimpa.length() == 9) {
                return ieLimpa.substring(0, 3) + "." +
                       ieLimpa.substring(3, 6) + "." +
                       ieLimpa.substring(6, 9);
            } else {
                // Para IEs menores, retorna sem formatação
                return ieLimpa;
            }
        } else if (ieLimpa.length() == 10) {
            // Formato: XXX.XXX.XXX.X
            return ieLimpa.substring(0, 3) + "." +
                   ieLimpa.substring(3, 6) + "." +
                   ieLimpa.substring(6, 9) + "." +
                   ieLimpa.substring(9, 10);
        } else if (ieLimpa.length() == 11) {
            // Formato: XXX.XXX.XXX.XX
            return ieLimpa.substring(0, 3) + "." +
                   ieLimpa.substring(3, 6) + "." +
                   ieLimpa.substring(6, 9) + "." +
                   ieLimpa.substring(9, 11);
        } else if (ieLimpa.length() == 12) {
            // Formato: XXX.XXX.XXX.XXX
            return ieLimpa.substring(0, 3) + "." +
                   ieLimpa.substring(3, 6) + "." +
                   ieLimpa.substring(6, 9) + "." +
                   ieLimpa.substring(9, 12);
        } else if (ieLimpa.length() == 13) {
            // Formato: XXX.XXX.XXX.XXXX
            return ieLimpa.substring(0, 3) + "." +
                   ieLimpa.substring(3, 6) + "." +
                   ieLimpa.substring(6, 9) + "." +
                   ieLimpa.substring(9, 13);
        } else {
            // Formato: XX.XXX.XXX.XXX.XXX (14 dígitos)
            return ieLimpa.substring(0, 2) + "." +
                   ieLimpa.substring(2, 5) + "." +
                   ieLimpa.substring(5, 8) + "." +
                   ieLimpa.substring(8, 11) + "." +
                   ieLimpa.substring(11, 14);
        }
    }
}

