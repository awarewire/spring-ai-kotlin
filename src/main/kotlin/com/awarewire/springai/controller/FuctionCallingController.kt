package com.awarewire.springai.controller

import org.apache.catalina.User
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/functions")
class FuctionCallingController(val openAiChatModel: OpenAiChatModel) {

    @GetMapping("/dummy")
    fun getDumy(): ResponseEntity<String> {
        val userMessage = UserMessage("¿Cuál es el clima en Santiago de Chile?")
        val response = openAiChatModel.call(Prompt(listOf(userMessage)))
        val result = response.result.output.content
        return ResponseEntity.ok(result)
    }

    @GetMapping("/weather")
    fun getWeatherInfo(): ResponseEntity<String> {
        val userMessage = UserMessage("¿Cuál es el clima en Santiago de Chile?")
        val response = openAiChatModel.call(
            Prompt(
                listOf(userMessage),
                OpenAiChatOptions.builder().withFunction("weatherFunction").build()
            )
        )
        val result = response.result.output.content
        return ResponseEntity.ok(result)
    }

    @GetMapping("/book")
    fun getBookInfo(@RequestParam("bookName") bookName: String): ResponseEntity<String> {
        val userMessage = UserMessage("¿Cuál es la información de este libro $bookName?")
        val response = openAiChatModel.call(
            Prompt(
                listOf(userMessage),
                OpenAiChatOptions.builder().withFunction("BookInfo").build()
            )
        )
        val result = response.result.output.content
        return ResponseEntity.ok(result)
    }
}