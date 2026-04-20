package com.medico.core.infrastructure.controllers;

import com.medico.core.core.dtos.RequestDTO;
import com.medico.core.core.dtos.ResponseDTO;
import com.medico.core.core.exceptions.GenericException;
import com.medico.core.infrastructure.services.bo.BasicService;
import com.medico.core.infrastructure.services.bo.BasicServiceTrace;
import com.medico.core.infrastructure.services.generics.ParseObjectJsonService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

@Slf4j
public abstract class BasicController <S extends RequestDTO, R extends ResponseDTO>{

    @Autowired
    private HttpServletRequest httpServletRequest;

    private final ExecutorService executor = Executors.newCachedThreadPool();

    private String idProcess;

    @Autowired
    private ParseObjectJsonService parseObjectJsonService;
    public ResponseEntity<R> handleRequest(@Validated @RequestBody S request, String requestName, Boolean trace, Function<S, R> serviceFunction) throws GenericException
    {
        long startTime = System.currentTimeMillis();
        log.info("Inicio de ejecución request: " + requestName);
        idProcess = String.valueOf(UUID.randomUUID());
        httpServletRequest.setAttribute("ID_PROCCESS", idProcess);
        httpServletRequest.setAttribute("TRACE", trace);
        request.setProcessId(idProcess);
        preProccess(request, trace, requestName);
        R response;
        if (serviceFunction != null) {
            response = proccessRequest(serviceFunction, request);
        } else {
            response = proccessRequest(request);
        }
        response.setIdProccess(idProcess);
        response = postProccess(request, response, trace, requestName);
        long endTime = System.currentTimeMillis(); // Registro de la fecha de finalización
        long executionTime = endTime - startTime; // Cálculo del tiempo de ejecución
        log.info("Execution Time "+requestName+" : " + executionTime + " milliseconds");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private R postProccess(S request, R response, Boolean trace, String requestName) throws GenericException{
        String requestJson = "";
        try {
            requestJson = parseObjectJsonService.parseObjectToJson(request);
            log.info("Request: " + requestJson);
        }catch (IOException e){
            log.error("Error al realizar el parceso del request a json ", e);
            throw new GenericException("000", "El request no se puede transformar a json " + request.getClass(), "El request no se puede transformar a json");
        }
        String responseJson = "";
        try {
            responseJson = parseObjectJsonService.parseObjectToJson(response);
            log.info("Request: " + responseJson);
        }catch (IOException e){
            log.error("Error al realizar el parceso del response a json ", e);
            throw new GenericException("000", "El response no se puede transformar a json " + response.getClass(), "El response no se puede transformar a json");
        }
        if (trace){
            String token = httpServletRequest.getHeader("Authorization");
            String sesion = httpServletRequest.getHeader("SessionId");
            String finalResponseJson = responseJson;
            Callable<Void> traceTask = () -> {
                getServiceTrace().trace(finalResponseJson, sesion, token, idProcess, "RESPONSE", requestName);
                return null;
            };
            Future<Void> future = executor.submit(traceTask);
            try {
                future.get(); // Esto bloqueará hasta que el hilo termine
            } catch (Exception e) {
                log.error("Error al termninar el hilo", e);
            }
        }
        response.setIdProccess(idProcess);
        return response;
    }

    private void preProccess(S request, Boolean trace, String requestName) throws GenericException{
        if (request.getSistema() == null)
            request.setSistema("WEB");
        if (request.getIp() ==null)
            request.setIp(httpServletRequest.getRemoteAddr());
        String requestJson = "";
        try {
            requestJson = parseObjectJsonService.parseObjectToJson(request);
            log.info("Request: " + requestJson);
        }catch (IOException e){
            log.error("Error al realizar el parceso del request a json ", e);
            throw new GenericException("000", "El request no se puede transformar a json " + request.getClass(), "El request no se puede transformar a json");
        }
        if (trace){
            String token = httpServletRequest.getHeader("Authorization");
            String sesion = httpServletRequest.getHeader("SessionId");
            String finalResponseJson = requestJson;
            Callable<Void> traceTask = () -> {
                getServiceTrace().trace(finalResponseJson, sesion, token, idProcess, "REQUEST", requestName);
                return null;
            };
            Future<Void> future = executor.submit(traceTask);
            try {
                future.get(); // Esto bloqueará hasta que el hilo termine
            } catch (Exception e) {
                log.error("Error al termninar el hilo", e);
            }
        }
    }

    protected R proccessRequest ( S request) throws GenericException
    {
        return (R) getService().proccesService(request);
    }

    protected R proccessRequest(Function<S, R> serviceFunction, S request) throws GenericException {
        return serviceFunction.apply(request);
    }

    protected abstract BasicService getService();

    protected  BasicServiceTrace getServiceTrace(){
        return null;
    }


}
