package com.medico.rag.service;

import com.medico.rag.model.PdfProcessResponse;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfReaderService {

    private final TextChunkService textChunkService;
    private final VectorIngestionService vectorIngestionService;

    public PdfProcessResponse processPdf(MultipartFile file) {
        validateFile(file);

        String safeFileName = buildSafeFileName(file);

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            if (text == null || text.isBlank()) {
                throw new IllegalArgumentException("El PDF no contiene texto legible");
            }

            List<String> chunks = textChunkService.splitText(text);

            if (chunks == null || chunks.isEmpty()) {
                throw new IllegalArgumentException("No se pudieron generar fragmentos del PDF");
            }

            // Eliminar chunks nulos o vacíos
            chunks.removeIf(chunk -> chunk == null || chunk.isBlank());

            if (chunks.isEmpty()) {
                throw new IllegalArgumentException("Todos los fragmentos generados estaban vacíos");
            }

            int saved = vectorIngestionService.ingestChunks(chunks, safeFileName);

            return PdfProcessResponse.builder()
                    .fileName(safeFileName)
                    .totalChunks(saved)
                    .status("PROCESSED")
                    .build();

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo el archivo PDF", e);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error indexando chunks en la base vectorial", e);
        }
    }

    private void validateFile(MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException("El archivo es obligatorio");
        }

        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("El archivo no tiene nombre válido");
        }

        if (!originalFilename.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF");
        }
    }

    private String buildSafeFileName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            return "unknown.pdf";
        }
        return originalFilename.trim();
    }
}