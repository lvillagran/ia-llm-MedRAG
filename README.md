# ia-llm-rag
Backend para implementar Retrieval-Augmented Generation (RAG) con modelos de lenguaje. Integra Milvus como base vectorial y Ollama para ejecutar LLMs locales. Construido con Spring Boot, permite búsquedas semánticas y generación de respuestas contextualizadas a partir de documentos.


# IA‑LLM‑MedRAG: AI RAG Knowledge Service

## 📖 Descripción
**IA‑LLM‑MedRAG** es un servicio de conocimiento basado en **RAG (Retrieval-Augmented Generation)** diseñado para el ámbito médico.  
El sistema permite:
- Ingesta de documentos PDF.
- Segmentación de texto (*chunking*).
- Generación de embeddings semánticos.
- Almacenamiento y búsqueda vectorial.
- Exposición de una API REST para consultas.

Este proyecto está construido sobre **Spring Boot 3.5.12** y **Spring AI 1.1.3**, integrando modelos de lenguaje y almacenamiento vectorial para ofrecer respuestas fundamentadas en evidencia médica.

---

## ⚙️ Tecnologías principales
- **Java 21**
- **Spring Boot** (Web, Validation, Actuator)
- **Spring AI** (Ollama, Vector Store Milvus)
- **Apache PDFBox** (procesamiento de PDFs)
- **Lombok** (reducción de boilerplate)
- **Milvus** (base de datos vectorial)
- **Spring Boot DevTools** (hot reload)
- **JUnit / Spring Boot Test** (testing)

---

## 📂 Estructura del Proyecto
- `com.medico.ia.knowledge.Main` → clase principal del servicio.
- `ai-rag-knowledge-service` → artefacto Maven.
- Dependencias gestionadas con **Spring AI BOM**.

---
## 🚀 BackEnd
<img width="1848" height="1060" alt="image" src="https://github.com/user-attachments/assets/acac2a1e-dfbb-4e5d-832e-c29b7db2b010" />

## 🚀 Terminal Ubuntu
<img width="1848" height="1060" alt="image" src="https://github.com/user-attachments/assets/36ed330c-786a-45ae-8901-ec737a75e825" />

## 🚀 Ingesta
<img width="1848" height="1060" alt="image" src="https://github.com/user-attachments/assets/d213b84d-a98f-41ed-86d1-ebc9a82b4221" />

## 🚀 Base Vectorial milvus
<img width="1848" height="1060" alt="image" src="https://github.com/user-attachments/assets/bf80a127-0ff3-4801-a4ec-66a145a90aca" />

## 🚀 Chunks
<img width="1848" height="1060" alt="image" src="https://github.com/user-attachments/assets/7cace520-41d6-468e-a799-c57b1e902e06" />

## 🚀 promt
<img width="1848" height="1060" alt="image" src="https://github.com/user-attachments/assets/fe7efe4b-c059-4cdc-879f-ba2ecf7d7c55" />


## 🚀 Instalación y Ejecución


🚧 Estado del Proyecto
Modelo en desarrollo
