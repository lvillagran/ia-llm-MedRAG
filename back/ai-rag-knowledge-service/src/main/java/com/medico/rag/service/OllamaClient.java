package com.medico.rag.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import java.util.Map;
import java.util.List;

/**
 * Cliente para interactuar con Ollama (embeddings y LLM).
 */
@Component
public class OllamaClient {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String ollamaBaseUrl = "http://localhost:11434"; // Ajusta según tu configuración

    /**
     * Genera embedding de un texto usando el modelo de embeddings.
     *
     * @param text Texto de entrada
     * @return embedding como arreglo de floats
     */
    public float[] generateEmbedding(String text) {
        String url = ollamaBaseUrl + "/api/embeddings";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Map<String, Object> body = Map.of(
                "model", "mxbai-embed-large",
                "prompt", text
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        // Ollama devuelve una lista de Double
        List<Double> embedding = (List<Double>) response.getBody().get("embedding");

        // Convertir a float[]
        float[] floatEmbedding = new float[embedding.size()];
        for (int i = 0; i < embedding.size(); i++) {
            floatEmbedding[i] = embedding.get(i).floatValue();
        }

        return floatEmbedding;
    }

    /**
     * Genera respuesta de lenguaje natural usando el modelo LLM.
     *
     * @param prompt Prompt con contexto + pregunta
     * @return Respuesta generada por el LLM
     */
    public String generateAnswer(String prompt) {
        String url = ollamaBaseUrl + "/api/generate";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        Map<String, Object> body = Map.of(
                "model", "mistral",
                "prompt", prompt
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return (String) response.getBody().get("response");
    }
}