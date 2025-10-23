package com.peopleflow.pessoascontratos.inbound.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.peopleflow.pessoascontratos.core.valueobject.Cpf;
import com.peopleflow.pessoascontratos.core.valueobject.Email;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import com.peopleflow.pessoascontratos.inbound.web.dto.serialization.CpfDeserializer;
import com.peopleflow.pessoascontratos.inbound.web.dto.serialization.EmailDeserializer;
import com.peopleflow.pessoascontratos.inbound.web.dto.serialization.StatusColaboradorDeserializer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ColaboradorRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "CPF é obrigatório")
    @Valid
    @JsonDeserialize(using = CpfDeserializer.class)
    private Cpf cpf;
    
    private String matricula;
    
    @NotNull(message = "Email é obrigatório")
    @Valid
    @JsonDeserialize(using = EmailDeserializer.class)
    private Email email;
    
    private LocalDate dataAdmissao;
    
    @JsonDeserialize(using = StatusColaboradorDeserializer.class)
    private StatusColaborador status;
}