package com.awarewire.springai.config

import com.awarewire.springai.repo.IBookRepo
import com.awarewire.springai.service.impl.BookFunctionServiceImpl
import org.springframework.ai.model.function.FunctionCallback
import org.springframework.ai.model.function.FunctionCallbackWrapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import com.awarewire.springai.service.impl.Request
import com.awarewire.springai.service.impl.WeatherServiceFunction
import com.awarewire.springai.service.impl.WeatherServiceImpl

@Configuration
class FunctionCallingConfig {

    @Bean
    fun weatherFunctionInfo(): FunctionCallback{
        return FunctionCallbackWrapper.builder(WeatherServiceFunction(WeatherServiceImpl()))
            .withName("weatherFunction")
            .withDescription("Get current weather forecast")
            //.withResponseConverter { response -> "" + response.temp + response.unit }
            .build()
    }

    @Bean
    fun bookInfoFunction(repo: IBookRepo): FunctionCallback {
        return FunctionCallbackWrapper.builder(BookFunctionServiceImpl(repo))
            .withName("BookInfo")
            .withDescription("Get book info from book name")
            .withInputType(Request::class.java)
            .withResponseConverter { response -> "" + response.books }
            .build()
    }

}