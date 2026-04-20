package com.medico.rag.model;

import lombok.Data;

@Data
public class SearchRequest {
    private String query;
    private Integer topK;
}