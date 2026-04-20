package com.medico.rag.repository;

import com.medico.rag.model.VectorDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class VectorStoreRepository {

    private final VectorStore vectorStore;

    public void saveAll(List<Document> documents) {
        if (documents == null || documents.isEmpty()) {
            throw new IllegalArgumentException("La lista de documentos no puede estar vacía");
        }

        List<Document> safeDocuments = documents.stream()
                .filter(Objects::nonNull)
                .filter(doc -> doc.getText() != null && !doc.getText().isBlank())
                .map(this::sanitizeDocument)
                .toList();

        if (safeDocuments.isEmpty()) {
            throw new IllegalArgumentException("No hay documentos válidos para almacenar");
        }

        vectorStore.add(safeDocuments);
    }

    public List<Document> search(SearchRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("El SearchRequest no puede ser nulo");
        }
        return vectorStore.similaritySearch(request);
    }

    public void save(VectorDocument doc) {
        if (doc == null) {
            throw new IllegalArgumentException("El documento no puede ser nulo");
        }

        if (doc.getText() == null || doc.getText().isBlank()) {
            throw new IllegalArgumentException("El contenido del documento no puede estar vacío");
        }

        Map<String, Object> metadata = new HashMap<>();
        putIfNotBlank(metadata, "docId", doc.getDocId());
        putIfNotBlank(metadata, "fileName", doc.getFileName());

        Document document = new Document(doc.getText().trim(), metadata);
        vectorStore.add(List.of(document));
    }

    private Document sanitizeDocument(Document document) {
        Map<String, Object> safeMetadata = new HashMap<>();

        if (document.getMetadata() != null) {
            document.getMetadata().forEach((key, value) -> {
                if (key != null && value != null) {
                    safeMetadata.put(key, value);
                }
            });
        }

        return new Document(document.getText().trim(), safeMetadata);
    }

    private void putIfNotBlank(Map<String, Object> metadata, String key, String value) {
        if (value != null && !value.isBlank()) {
            metadata.put(key, value.trim());
        }
    }
}