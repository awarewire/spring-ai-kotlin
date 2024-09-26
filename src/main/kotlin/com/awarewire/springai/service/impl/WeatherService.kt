package com.awarewire.springai.service.impl

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import java.util.concurrent.CompletableFuture
import java.util.function.Function

interface WeatherService {
    suspend fun forecast(request: WeatherRequest): WeatherResponse
}

data class WeatherRequest(val location: String = "")

sealed interface WeatherResponse {
    data class Success(val temp: Double, val unit: TempUnit) : WeatherResponse
    data class Failure(val reason: String) : WeatherResponse
}

enum class TempUnit(val symbol: String, val description: String) {
    CELSIUS("C", "Celsius"),
    FAHRENHEIT("F", "Fahrenheit")
}

class WeatherServiceImpl(
    private val unit: TempUnit = TempUnit.CELSIUS,
) : WeatherService {
    override suspend fun forecast(request: WeatherRequest): WeatherResponse = coroutineScope {
        val jobs = listOf(
            async { callSlowWeatherService(request) },
            async { callFastWeatherService(request) }
        )
        val result = select {
            jobs.forEach { job -> job.onAwait { job } }
        }.also {
            coroutineContext.cancelChildren()
        }
        result.await()
    }

    private suspend fun callSlowWeatherService(request: WeatherRequest): WeatherResponse {
        val ms = request.location.length * 100L
        delay(ms)
        return WeatherResponse.Success(20.0, unit)
    }

    private suspend fun callFastWeatherService(request: WeatherRequest): WeatherResponse {
        val ms = request.location.length * 10L
        delay(ms)
        return WeatherResponse.Success(25.0, unit)
    }
}

class WeatherServiceFunction(
    private val weatherService: WeatherService,
) : Function<WeatherRequest, WeatherResponse> {

    private val jobScope: CoroutineScope
        get() = CoroutineScope(Job() + Dispatchers.IO)

    override fun apply(request: WeatherRequest): WeatherResponse {
        val future = CompletableFuture<WeatherResponse>()
        jobScope.launch {
            try {
                val response = weatherService.forecast(request)
                future.complete(response)
            } catch (e: Exception) {
                future.complete(WeatherResponse.Failure(e.message ?: "Unknown error"))
            }
        }
        // Block until the future is complete
        return future.get()
    }
}