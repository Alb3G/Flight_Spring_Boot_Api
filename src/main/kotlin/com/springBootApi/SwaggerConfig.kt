package com.springBootApi

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Configuración de Swagger para la documentación de la API.
 */
@Configuration
class SwaggerConfig {

    /**
     * Bean que configura la instancia de OpenAPI con la información de la API.
     *
     * @return instancia de OpenAPI configurada.
     */
    @Bean
    fun customApi(): OpenAPI {
        return OpenAPI()
            .openapi("3.0.0")
            .info(
                Info()
                    .title("Proyecto Flights API")
                    .version("1.0")
                    .description("Documentación sobre la funcionalidad de la API")
            )
    }

}