package com.medico.rag.service;

import com.medico.rag.model.SearchRequest;
import com.medico.rag.model.SearchResponse;
import com.medico.rag.repository.VectorStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VectorSearchService {

    private static final int DEFAULT_TOP_K = 3;

    private final VectorStoreRepository vectorStoreRepository;
    private final ChatModel chatModel;

    public SearchResponse searchChunks(SearchRequest request) {
        validateRequest(request);

        String query = request.getQuery().trim();
        int topK = resolveTopK(request);

        List<Document> documents = vectorStoreRepository.search(
                org.springframework.ai.vectorstore.SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .build()
        );

        documents = documents.stream()
                .collect(Collectors.toMap(
                        Document::getText,
                        d -> d,
                        (d1, d2) -> d1
                ))
                .values()
                .stream()
                .toList();

        return SearchResponse.builder()
                .query(query)
                .documents(documents)
                .build();
    }

    public SearchResponse searchAndAnswer(SearchRequest request) {
        validateRequest(request);

        String query = request.getQuery().trim();
        int topK = resolveTopK(request);

        List<Document> documents = vectorStoreRepository.search(
                org.springframework.ai.vectorstore.SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .build()
        );

        if (documents == null || documents.isEmpty()) {
            return SearchResponse.builder()
                    .query(query)
                    .answer("No se encontró información relevante en la base vectorial.")
                    .documents(List.of())
                    .build();
        }

        String context = documents.stream().limit(3)
                .map(Document::getText)
                .filter(text -> text != null && !text.isBlank())
                .collect(Collectors.joining("\n\n---\n\n"));

        String prompt = """
                Responde SOLO con base en el contexto proporcionado.
                Si la respuesta no está en el contexto, responde: "No encontrado en el contexto".

                CONTEXTO:
                %s

                PREGUNTA:
                %s
                """.formatted(context, query);

        String answer = ChatClient.create(chatModel)
                .prompt()
                .user(prompt)
                .call()
                .content();

        return SearchResponse.builder()
                .query(query)
                .answer(answer)
                .documents(documents)
                .build();
    }

    private void validateRequest(SearchRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("El request no puede ser nulo");
        }

        if (request.getQuery() == null || request.getQuery().isBlank()) {
            throw new IllegalArgumentException("La consulta no puede estar vacía");
        }
    }

    private int resolveTopK(SearchRequest request) {
        return request.getTopK() != null && request.getTopK() > 0
                ? request.getTopK()
                : DEFAULT_TOP_K;
    }
}