package com.awarewire.springai.controller

import com.awarewire.springai.dto.ResponseDTO
import org.springframework.ai.image.ImagePrompt
import org.springframework.ai.openai.OpenAiImageModel
import org.springframework.ai.openai.OpenAiImageOptions
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/images")
@CrossOrigin(origins = ["*"])
class ImageController(
    private val openAiImageModel: OpenAiImageModel
) {

    // SPRING AI SUPPORTS DALL-E, THE IMAGE GENERATION MODEL FROM OPENAI
    @GetMapping("/generate")
    fun generateImage(@RequestParam("param") param: String): ResponseEntity<ResponseDTO<String>> {
        val imageResponse = openAiImageModel.call(
            ImagePrompt(
                param,
                OpenAiImageOptions.builder()
                    .withModel("dall-e-3")
                    .withQuality("hd") // standard, hd
                    .withN(1) // cantidad de imágenes a generar, dall-e-3 solo permite n=1
                    .withHeight(1024)
                    .withWidth(1024)
                    .build()
            )
        )

        val url = imageResponse.result.output.url

        return ResponseEntity.ok(ResponseDTO(200, "success", url))
    }

    @GetMapping("/generate/dalle2")
    fun generateImageDalle2(@RequestParam("param") param: String): ResponseEntity<ResponseDTO<List<String>>> {
        val imageResponse = openAiImageModel.call(
            ImagePrompt(
                param,
                OpenAiImageOptions.builder()
                    .withModel("dall-e-2")
                    .withQuality("standard")
                    .withN(3) // cantidad de imágenes a generar
                    .withHeight(256)
                    .withWidth(256)
                    .build()
            )
        )

        val images = imageResponse.results.map { it.output.url }

        return ResponseEntity.ok(ResponseDTO(200, "success", images))
    }

    @GetMapping("/generate/B64")
    fun generateImageB64(@RequestParam("param") param: String): String {
        val imageResponse = openAiImageModel.call(
            ImagePrompt(
                param,
                OpenAiImageOptions.builder()
                    .withModel("dall-e-3")
                    .withQuality("standard")
                    .withN(1) // cantidad de imágenes a generar, dall-e-3 solo permite n=1
                    .withHeight(1024)
                    .withWidth(1024)
                    .withResponseFormat("b64_json")
                    .build()
            )
        )

        return imageResponse.result.output.b64Json

    }
}