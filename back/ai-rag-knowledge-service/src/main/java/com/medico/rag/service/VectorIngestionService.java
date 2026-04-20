package com.medico.rag.service;

import com.medico.rag.repository.VectorStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VectorIngestionService {

    private final VectorStoreRepository vectorStoreRepository;

    public int ingestChunks(List<String> chunks, String fileName) {
        String safeFileName = (fileName != null && !fileName.isBlank())
                ? fileName.trim()
                : "unknown.pdf";

        List<String> cleanChunks = chunks.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(chunk -> !chunk.isBlank())
                .toList();

        if (cleanChunks.isEmpty()) {
            throw new IllegalArgumentException("No hay chunks válidos para procesar");
        }

        List<Document> documents = new ArrayList<>();

        for (int i = 0; i < cleanChunks.size(); i++) {
            String chunk = cleanChunks.get(i);

            Map<String, Object> metadata = new HashMap<>();
            metadata.put("fileName", safeFileName);
            metadata.put("chunkIndex", i);
            metadata.put("source", "pdf");

            metadata.entrySet().removeIf(entry -> entry.getValue() == null);

            Document document = new Document(chunk, metadata);
            documents.add(document);
        }

        if (documents.isEmpty()) {
            throw new IllegalStateException("No hay documentos válidos para insertar en Milvus");
        }

        log.info("Insertando {} documentos en la base vectorial", documents.size());

        vectorStoreRepository.saveAll(documents);

        return documents.size();
    }
}