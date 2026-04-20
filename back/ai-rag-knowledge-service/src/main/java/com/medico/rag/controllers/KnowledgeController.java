package com.medico.rag.controllers;

import com.medico.rag.model.PdfProcessResponse;
import com.medico.rag.model.SearchRequest;
import com.medico.rag.model.SearchResponse;
import com.medico.rag.service.PdfReaderService;
import com.medico.rag.service.VectorSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador REST para operaciones de RAG (Retrieval-Augmented Generation).
 *
 * Expone endpoints para:
 * - Ingestar documentos PDF, procesarlos en fragmentos (chunks) y almacenar sus embeddings en Milvus.
 * - Consultar la base vectorial para recuperar chunks relevantes.
 * - Generar respuestas completas mediante la combinación de búsqueda semántica y modelos de lenguaje (LLM).
 *
 * Este controlador centraliza las operaciones de conocimiento dentro del proyecto,
 * facilitando la integración con servicios de ingesta, búsqueda y generación.
 *
 * @author Leonardo Villagran <leonardo.nvillagrans@gmail.com>
 */

@RestController
@RequestMapping("/api/v1/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final PdfReaderService pdfReaderService;
    private final VectorSearchService vectorSearchService;

    /**
     * Ingesta de información en la base vectorial Milvus.
     *
     * Este endpoint recibe un archivo PDF, lo procesa en fragmentos (chunks),
     * genera embeddings a partir de su contenido y los almacena en Milvus
     * para habilitar búsquedas semánticas posteriores.
     *
     * @param file Archivo PDF a procesar
     * @return PdfProcessResponse con el resultado de la ingesta (estado, cantidad de chunks, etc.)
     */
    @PostMapping("/ingesta")
    public PdfProcessResponse ingestaUploadPdf(@RequestParam("file") MultipartFile file) {
        return pdfReaderService.processPdf(file);
    }


    /**
     * Recupera los chunks relevantes desde la base vectorial Milvus.
     *
     * Este endpoint recibe una consulta del usuario, genera su embedding
     * y realiza una búsqueda semántica en Milvus para obtener los fragmentos
     * más cercanos al contenido de la pregunta.
     *
     * No invoca al LLM, por lo que devuelve únicamente los chunks con sus
     * respectivos puntajes de similitud, útil para pruebas técnicas y auditoría.
     *
     * @param request Objeto con la consulta del usuario (SearchRequest)
     * @return SearchResponse con la lista de chunks relevantes y sus scores
     */
    @PostMapping("/searchChunks")
    public SearchResponse searchChunks(@RequestBody SearchRequest request) {
        return vectorSearchService.searchChunks(request);
    }


    /**
     * Prompt completo para el usuario final.
     *
     * Este endpoint recibe una consulta del usuario, genera su embedding y
     * realiza una búsqueda semántica en la base vectorial Milvus para recuperar
     * los fragmentos más relevantes. Posteriormente, combina dichos fragmentos
     * como contexto y los envía al modelo de lenguaje (LLM) para producir una
     * respuesta redactada en lenguaje natural.
     *
     * Es el flujo completo de RAG (Retrieval-Augmented Generation), pensado para
     * el usuario final, ya que devuelve tanto los chunks relevantes como la
     * respuesta generada por el LLM.
     *
     * @param request Objeto con la consulta del usuario (SearchRequest)
     * @return SearchResponse con la pregunta original, los chunks relevantes y la respuesta final del LLM
     */
    @PostMapping("/promt")
    public SearchResponse promt(@RequestBody SearchRequest request) {
        return vectorSearchService.searchAndAnswer(request);
    }



}