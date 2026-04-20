package com.medico.rag.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PdfProcessResponse {
    private String fileName;
    private int totalChunks;
    private String status;
}