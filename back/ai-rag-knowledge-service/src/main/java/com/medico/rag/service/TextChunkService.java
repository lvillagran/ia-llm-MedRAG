package com.medico.rag.service;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkService {

    public List<String> splitText(String text) {
        return splitText(text, 1000, 150);
    }

    public List<String> splitText(String text, int chunkSize, int overlap) {

        List<String> chunks = new ArrayList<>();

        if (text == null || text.isBlank()) {
            return chunks;
        }

        int start = 0;

        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            chunks.add(text.substring(start, end));

            if (end == text.length()) break;

            start = end - overlap;
        }

        return chunks;
    }
}