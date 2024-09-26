package com.awarewire.springai.controller

import org.springframework.ai.openai.OpenAiAudioSpeechModel
import org.springframework.ai.openai.OpenAiAudioSpeechOptions
import org.springframework.ai.openai.api.OpenAiAudioApi
import org.springframework.ai.openai.audio.speech.SpeechPrompt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.ByteArrayInputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@RestController
@RequestMapping("/tts")
class TTSController(
    private val openAiAudioSpeechModel: OpenAiAudioSpeechModel
) {

    @GetMapping
    fun tts(@RequestParam("message") message: String): ByteArray {
        val speechOptions = OpenAiAudioSpeechOptions.builder()
            .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
            .withVoice(OpenAiAudioApi.SpeechRequest.Voice.NOVA)
            .withSpeed(1.0f) // 2.0f
            .withModel(OpenAiAudioApi.TtsModel.TTS_1_HD.value)
            .build()

        val prompt = SpeechPrompt(message, speechOptions)
        val response = openAiAudioSpeechModel.call(prompt)

        val responseBytes = response.result.output

        // Crear el directorio si no existe
        val directory = Paths.get("src/main/resources/audios/tts/")
        if (Files.notExists(directory)) {
            Files.createDirectories(directory)
        }

        // Ruta del archivo
        val filePath = directory.resolve("tts_1.mp3")

        ByteArrayInputStream(responseBytes).use { inputStream ->
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
        }

        return responseBytes
    }
}