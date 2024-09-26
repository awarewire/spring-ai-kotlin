package com.awarewire.springai.controller

import com.awarewire.springai.dto.ResponseDTO
import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions
import org.springframework.ai.openai.api.OpenAiAudioApi
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import org.springframework.web.multipart.MultipartFile
import org.springframework.http.MediaType


@RestController
@RequestMapping("/transcripts")
@CrossOrigin(origins = ["*"])
class TranscriptController(
    private val transcriptionModel: OpenAiAudioTranscriptionModel
) {

    // Se usa el modelo Whisper

    @GetMapping("/es")
    fun transcriptES(): ResponseEntity<String> {
        val options = OpenAiAudioTranscriptionOptions.builder()
            .withLanguage("es")
            .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
            .withTemperature(0f)
            .build()

        val audioFile = ClassPathResource("/audios/es_audio1.flac")

        val transcriptionPrompt = AudioTranscriptionPrompt(audioFile, options)
        val response = transcriptionModel.call(transcriptionPrompt)

        return ResponseEntity.ok(response.result.output)
    }

    @GetMapping("/en")
    fun transcriptEN(): ResponseEntity<String> {
        val options = OpenAiAudioTranscriptionOptions.builder()
            .withLanguage("en")
            .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
            .withTemperature(0f)
            .build()

        val audioFile = ClassPathResource("/audios/en_audio2.mp3")

        val transcriptionPrompt = AudioTranscriptionPrompt(audioFile, options)
        val response = transcriptionModel.call(transcriptionPrompt)

        return ResponseEntity.ok(response.result.output)
    }

    @PostMapping(value = ["/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun handleAudioUpload(@RequestParam("audio") audioFile: MultipartFile): ResponseEntity<ResponseDTO<String>> {
        return try {
            val uploadDirPath = "src/main/resources/audios/uploads/"

            // Crear el directorio de uploads si no existe
            val uploadPath = Paths.get(uploadDirPath)
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath)
            }

            // Guardar el archivo de audio en el servidor
            val fileName = "audio_" + System.currentTimeMillis() + ".wav"
            val filePath = uploadPath.resolve(fileName)
            Files.copy(audioFile.inputStream, filePath)

            // Realizar cualquier procesamiento adicional necesario
            val transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .withLanguage("es")
                .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .withTemperature(0f)
                .build()

            // No se usó ClassPathResource porque esa clase solo detecta archivos que no hayan sido creados en tiempo de ejecución
            // Se usó FileSystemResource porque sí detecta los archivos creados dinámicamente en tiempo de ejecución
            val audioFileUploaded = FileSystemResource("$uploadDirPath$fileName")

            val transcriptionRequest = AudioTranscriptionPrompt(audioFileUploaded, transcriptionOptions)
            val response = transcriptionModel.call(transcriptionRequest)

            ResponseEntity.ok(ResponseDTO(200, "success", response.result.output))
        } catch (e: IOException) {
            ResponseEntity.internalServerError().build()
        }
    }
}