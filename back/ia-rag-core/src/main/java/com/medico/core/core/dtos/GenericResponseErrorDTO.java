package com.medico.core.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class GenericResponseErrorDTO {

    private String code;
    private String technicalMessage;
    private String shortMessage;
    private String dataAux;
    private Date dateTime;

    private String idProccess;

}
