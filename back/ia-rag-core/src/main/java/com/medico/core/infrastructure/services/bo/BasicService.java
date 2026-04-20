package com.medico.core.infrastructure.services.bo;

import com.medico.core.core.dtos.RequestDTO;
import com.medico.core.core.dtos.ResponseDTO;
import com.medico.core.core.exceptions.GenericException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public  class BasicService <S extends RequestDTO, R extends ResponseDTO>{


    public  R proccesService(S request) throws GenericException{
        long startTime = System.currentTimeMillis();
        log.info("Inicio de ejecución request: " + this.getClass().getCanonicalName());
        preProcess(request);
        R ressponse = handleProccess(request);
        postProcess(request, ressponse);
        long endTime = System.currentTimeMillis(); /* Registro de la fecha de finalización */
        long executionTime = endTime - startTime; /* Cálculo del tiempo de ejecución */
        log.info("Execution Time "+this.getClass().getCanonicalName()+" : " + executionTime + " milliseconds");
        return ressponse;
    }

    protected  R handleProccess(S request) throws GenericException{
        return null;
    }

    public  void preProcess(S request) throws GenericException{

    }

    public  void postProcess(S request, R response) throws GenericException{

    }

}
