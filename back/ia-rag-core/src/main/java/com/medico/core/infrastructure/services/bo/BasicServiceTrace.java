package com.medico.core.infrastructure.services.bo;


public abstract class BasicServiceTrace {


    public abstract void trace(String data, String sesion, String token, String idProcess, String tipo, String requestName);
}
