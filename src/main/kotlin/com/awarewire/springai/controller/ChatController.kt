package com.awarewire.springai.controller

import com.awarewire.springai.dto.AuthorBookDTO
import com.awarewire.springai.dto.BookInfoDTO
import com.awarewire.springai.dto.ResponseDTO
import com.awarewire.springai.util.ChatHistory

import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.converter.BeanOutputConverter
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaOptions
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chats")
@CrossOrigin(origins = ["*"])
class ChatController(
    val openAiChatModel: OpenAiChatModel,
    val myChatHistory: ChatHistory,
    val ollamaChatModel: OllamaChatModel
) {

    @GetMapping("/generate/dummy")
    fun dummy(@RequestParam(value = "message") message: String): ResponseEntity<ResponseDTO<String>> {
        val result = openAiChatModel.call(message);
        return ResponseEntity.ok(ResponseDTO(200, "success", result))
    }

    @GetMapping("/generate/prompt")
    fun getChatPrompt(@RequestParam(value = "message") message: String): ResponseEntity<ResponseDTO<String>> {
        val chatResponse = openAiChatModel.call(Prompt(message));
        val result = chatResponse.result.output.content
        return ResponseEntity.ok(ResponseDTO(200, "success", result))
    }

    @GetMapping("/generate/promptTemplate")
    fun generateTextWithPromt(
        @RequestParam(value = "param") param: String,
        @RequestParam(value = "topic") topic: String
    ): ResponseEntity<ResponseDTO<String>> {

        val promptTemplate = PromptTemplate("Dime acerca de $param y este $topic")
        val prompt = promptTemplate.create(mapOf("param" to param, "topic" to topic))
        val chatResponse = openAiChatModel.call(prompt)
        val result = chatResponse.result.output.content
        return ResponseEntity.ok(ResponseDTO(200, "success", result))
    }

    @GetMapping("/generate/output_parse/author")
    fun generateOutPutParseAuthor(@RequestParam(value = "author") author: String): ResponseEntity<AuthorBookDTO> {
        val outputConverter = BeanOutputConverter(AuthorBookDTO::class.java)
        val template = "Dime los títulos de libros de {author}. {responseIA}"
        val promptTemplate =
            PromptTemplate(template, mapOf<String, Any>("author" to author, "responseIA" to outputConverter.format))
        val prompt = promptTemplate.create(mapOf("author" to author))

        val generation = openAiChatModel.call(prompt).result
        val authorBook = outputConverter.convert(generation.output.content)
        return ResponseEntity.ok(authorBook)
    }

    @GetMapping("/generate/output_parse/book")
    fun generateOutPutParseBook(@RequestParam(value = "bookName") bookName: String): ResponseEntity<BookInfoDTO> {
        val outputConverter = BeanOutputConverter(BookInfoDTO::class.java)
        val template =  "Dime el autor y un resumen de este libro cuyo nombre es {bookName}. {responseIA}"

        val promptTemplate =
            PromptTemplate(template, mapOf<String, Any>("bookName" to bookName, "responseIA" to outputConverter.format))

        val prompt = promptTemplate.create(mapOf("bookName" to bookName))

        val generation = openAiChatModel.call(prompt).result
        val bookInfoDTO = outputConverter.convert(generation.output.content)
        return ResponseEntity.ok(bookInfoDTO)
    }

    @GetMapping("/generate/conversation/dummy")
   fun  generateConversationDummy(@RequestParam(value = "message") message: String): ResponseEntity<ResponseDTO<String>>{
        val listMessages = mutableListOf<UserMessage>()
        val chatMessage1 = UserMessage("Hablemos de charizard")
        val chatMessage2 = UserMessage(message)

        listMessages.add(chatMessage1)
        listMessages.add(chatMessage2)

        val chatResponse = openAiChatModel.call(Prompt(listMessages.toList()))
        val result = chatResponse.result.output.content
        return ResponseEntity.ok(ResponseDTO(200, "success", result))
   }

    @GetMapping("/generate/conversation/chatHistory")
    fun generateConversationHistory(
        @RequestParam("message") message: String
    ): ResponseEntity<ResponseDTO<String>> {
        myChatHistory.addMessage("1", UserMessage(message))

        val chatResponse = openAiChatModel.call(Prompt(myChatHistory.getAll("1")))
        val result = chatResponse.result.output.content

        return ResponseEntity.ok(ResponseDTO(200, "success", result))
    }

    @GetMapping("/generate/ollama/dummy")
    fun getChatOllamaDummy(
        @RequestParam("message") message: String
    ): String {
        val chatResponse = ollamaChatModel.call(Prompt(message, OllamaOptions.create().withModel("gemma2:2b")))
        return chatResponse.result.output.content
    }


}