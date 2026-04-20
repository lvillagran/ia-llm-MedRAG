package com.medico.rag.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa un fragmento (chunk) recuperado desde la base vectorial Milvus.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChunkResult {
    private String text;   // contenido del fragmento
    private double score;  // puntaje de similitud
    private String docId;  // opcional: identificador del documento origen
}
