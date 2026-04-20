package com.medico.core.core.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {
    private String sistema;
    private String ip;
    private String processId;
}
