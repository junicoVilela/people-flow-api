package com.peopleflow.pessoascontratos.core.domain;

import com.peopleflow.common.exception.BusinessException;
import com.peopleflow.pessoascontratos.core.model.Colaborador;
import com.peopleflow.pessoascontratos.core.valueobject.StatusColaborador;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ColaboradorDomainService {

    public void validarDemissao(Colaborador colaborador, LocalDate dataDemissao) {
        if (dataDemissao == null) {
            throw new BusinessException("DATA_DEMISSAO_OBRIGATORIA", "Data de demissão é obrigatória");
        }
        
        if (colaborador.getDataAdmissao() != null && dataDemissao.isBefore(colaborador.getDataAdmissao())) {
            throw new BusinessException("DATA_DEMISSAO_INVALIDA", 
                "Data de demissão não pode ser anterior à data de admissão");
        }
        
        if (colaborador.getStatus().isDemitido()) {
            throw new BusinessException("COLABORADOR_JA_DEMITIDO", 
                "Colaborador já está demitido");
        }
    }

    public void validarAtivacao(Colaborador colaborador) {
        if (colaborador.getStatus().isExcluido()) {
            throw new BusinessException("COLABORADOR_EXCLUIDO", 
                "Não é possível ativar colaborador excluído");
        }
    }

    public void validarExclusao(Colaborador colaborador) {
        if (colaborador.getStatus().isExcluido()) {
            throw new BusinessException("COLABORADOR_JA_EXCLUIDO", 
                "Colaborador já está excluído");
        }
    }
}
