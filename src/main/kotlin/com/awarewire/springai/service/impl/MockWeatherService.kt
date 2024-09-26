package com.awarewire.springai.service.impl

import java.util.function.Function

class MockWeatherService : Function<MockWeatherService.Request?, MockWeatherService.Response> {
    enum class TemperatureUnit {
        C, F
    }

    @JvmRecord
    data class Request(val location: String, val unit: TemperatureUnit)

    @JvmRecord
    data class Response(val temp: Double, val unit: TemperatureUnit)

    override fun apply(request: Request?): Response {
        return Response(30.0, TemperatureUnit.C)
    }
}

