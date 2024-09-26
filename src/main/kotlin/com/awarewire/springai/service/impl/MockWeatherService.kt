package com.awarewire.springai.service.impl

class MockWeatherService : (MockWeatherService.Request) -> MockWeatherService.Response {

    enum class Unit { C, F }

    data class Request(val location: String, val unit: Unit)

    data class Response(val temp: Double, val unit: Unit)

    override fun invoke(request: Request): Response {
        return Response(30.0, Unit.C)
    }
}

