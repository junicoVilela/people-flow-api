package com.peopleflow.pessoascontratos.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PersonRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String name;

    @Pattern(regexp = "^\\d{11}$|^\\d{14}$", message = "Documento deve ser CPF (11 dígitos) ou CNPJ (14 dígitos)")
    private String document;

    public String getName() { 
        return name; 
    }
    
    public String getDocument() { 
        return document; 
    }

    public void setName(String name) { 
        this.name = name; 
    }
    
    public void setDocument(String document) { 
        this.document = document; 
    }
} 