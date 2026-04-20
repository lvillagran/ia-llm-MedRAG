package com.medico.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VectorDocument {
    private String docId;
    private String fileName;
    private String text;
    private float[] embedding;
}