package com.medico.rag.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.ai.document.Document;

import java.util.List;

@Data
@Builder
public class SearchResponse {

    private String query;
    private String answer;
    private List<Document> documents;
}