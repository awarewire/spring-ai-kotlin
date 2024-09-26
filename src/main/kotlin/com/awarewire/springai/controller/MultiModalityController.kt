package com.awarewire.springai.controller

import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.image.ImagePrompt
import org.springframework.ai.model.Media
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.OpenAiImageModel
import org.springframework.ai.openai.OpenAiImageOptions
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.ClassPathResource
import org.springframework.util.MimeTypeUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
@RequestMapping("/multimodality")
class MultiModalityController(
    private val openAiChatModel: OpenAiChatModel,
    private val openAiImageModel: OpenAiImageModel
) {

    @GetMapping("/analyze-image")
    fun multiModality(): String {
        val classPathResource = ClassPathResource("/images/img.png")

        val userMessage = UserMessage(
            "Explicame que ves en esta imagen?",
            Media(MimeTypeUtils.IMAGE_PNG, classPathResource)
        )

        val response = openAiChatModel.call(Prompt(userMessage))

        return response.result.output.content
    }

    @GetMapping("/analyze-image/image_url")
    fun multiModalityURL(): String {
        val userMessage = UserMessage(
            "Explicame que ves en esta imagen?",
            Media(
                MimeTypeUtils.IMAGE_JPEG,
                URI("https://img.europapress.es/fotoweb/fotonoticia_20220412165352_1200.jpg").toURL()
            )
        )

        val response = openAiChatModel.call(Prompt(listOf(userMessage)))

        return response.result.output.content
    }

    @GetMapping("/analyze-image/images-review")
    fun multiModalityURLReview(): String {
        val userMessage = UserMessage(
            "Explicame que ves en estas imagenes?",
            listOf(
                Media(
                    MimeTypeUtils.IMAGE_JPEG,
                    URI("https://img.europapress.es/fotoweb/fotonoticia_20220412165352_1200.jpg").toURL()
                ),
                Media(
                    MimeTypeUtils.IMAGE_JPEG,
                    URI("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkr4crv5vRotmYd78_VghLFcnM73R-9CES1g&s").toURL()
                )
            )
        )

        val response = openAiChatModel.call(Prompt(listOf(userMessage)))

        return response.result.output.content
    }

    @GetMapping("/analyze-image/upload")
    fun multiModalityUpload(@RequestParam("image") imageFile: MultipartFile): String {
        val userMessage = UserMessage(
            "Explicame que ves en esta imagen?",
            listOf(Media(MimeTypeUtils.IMAGE_JPEG, ByteArrayResource(imageFile.bytes)))
        )

        val response = openAiChatModel.call(Prompt(listOf(userMessage)))
        val description = response.result.output.content

        val imageResponse = openAiImageModel.call(
            ImagePrompt(
                "Generame una caricatura de esta descripción: $description",
                OpenAiImageOptions.builder()
                    .withModel("dall-e-3")
                    .withQuality("standard")
                    .withN(1)
                    .withHeight(1024)
                    .withWidth(1024)
                    .build()
            )
        )

        return imageResponse.result.output.url
    }
}