package com.medico.core.infrastructure.services.generics;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * The type Parse object json service.
 *
 * @param <T> the type parameter
 */
@Component
public class ParseObjectJsonService  {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public <T> String parseObjectToJson(T object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    public <T> T parseJsonToObject(String jsonString, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonString, clazz);
    }

    public<T> T convertJsonToObject(String jsonString, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(jsonString, typeReference);
    }

}
