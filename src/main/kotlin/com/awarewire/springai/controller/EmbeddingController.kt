package com.awarewire.springai.controller

import org.springframework.ai.document.Document
import org.springframework.ai.embedding.EmbeddingResponse
import org.springframework.ai.openai.OpenAiEmbeddingModel
import org.springframework.ai.vectorstore.ChromaVectorStore
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/embeddings")
class EmbeddingController(
    private val openAiEmbeddingModel: OpenAiEmbeddingModel,
    private val vectorStore: ChromaVectorStore
) {

    @GetMapping("/generate")
    fun generateEmbeddings(@RequestParam("message") message: String): Map<String, EmbeddingResponse> {
        val response = openAiEmbeddingModel.embedForResponse(listOf(message))
        return mapOf("embedding" to response)
    }

    @GetMapping("/vectorstore")
    fun useVectorStore(@RequestParam("message") message: String): List<Document> {
        val documents = listOf(
            Document("Spring AI es lo máximo", mapOf("meta1" to "meta1")),
            Document("Python es más popular en IA"),
            Document("El futuro es la Inteligencia Artificial", mapOf("meta1" to "meta1"))
        )

        vectorStore.add(documents)

        val results = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(1))

        return results
    }
}