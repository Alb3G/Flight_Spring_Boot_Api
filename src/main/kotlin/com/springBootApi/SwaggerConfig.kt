package com.springBootApi

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customApi(): OpenAPI {
        return OpenAPI()
            .openapi("3.0.0")
            .info(
                Info()
                    .title("Flights API project")
                    .version("1.0")
                    .description("Docs about api functionality")
            )
    }

}