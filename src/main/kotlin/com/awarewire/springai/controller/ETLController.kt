package com.awarewire.springai.controller

import org.springframework.ai.document.Document
import org.springframework.ai.reader.ExtractedTextFormatter
import org.springframework.ai.reader.pdf.PagePdfDocumentReader
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("etls")
class ETLController(private val vectorStore: VectorStore) {

    @GetMapping
    fun testETL(): ResponseEntity<List<Document>> {
        val pdfReader = PagePdfDocumentReader(
            "classpath:/pdf/sample2.pdf",
            PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageExtractedTextFormatter(
                    ExtractedTextFormatter.builder()
                        .withNumberOfTopTextLinesToDelete(0)
                        .build()
                )
                .withPagesPerDocument(1)
                .build()
        )

        vectorStore.add(pdfReader.read())

        val results: List<Document> = vectorStore.similaritySearch(
            SearchRequest.query("PDF es un formato de archivo para representar documentos").withTopK(1)
        )

        return ResponseEntity.ok(results)
    }
}