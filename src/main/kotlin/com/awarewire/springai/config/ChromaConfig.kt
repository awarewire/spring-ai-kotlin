package com.awarewire.springai.config

import org.springframework.ai.chroma.ChromaApi
import org.springframework.ai.openai.OpenAiEmbeddingModel
import org.springframework.ai.vectorstore.ChromaVectorStore
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class ChromaConfig {

    // Se agregó esto para indicar el chromaUrl y no dar problemas al levantar el proyecto
    @Bean
    fun chromaApi(restClientBuilder: RestClient.Builder): ChromaApi {
        val chromaUrl = "http://localhost:8000"
        val chromaApi = ChromaApi(chromaUrl, restClientBuilder)
        return chromaApi
    }

    @Bean
    fun chromaVectorStore(openAiEmbeddingModel: OpenAiEmbeddingModel, chromaApi: ChromaApi): ChromaVectorStore {
        return ChromaVectorStore(openAiEmbeddingModel, chromaApi, true)
    }
}