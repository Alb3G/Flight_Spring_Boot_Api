package com.springBootApi.controller

import com.springBootApi.model.ApiKey
import com.springBootApi.model.CustomResponse
import com.springBootApi.model.ErrorResponse
import com.springBootApi.model.ResponseModel
import com.springBootApi.service.ApiKeyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controlador para operaciones de claves API, asegurado por clave de administrador.
 */
@RestController
@RequestMapping("/api-keys")
@Tag(name = "api-key controller", description = "Api key operations controller secured by Admin key")
class ApiKeyController {

    @Autowired
    lateinit var apiKeyService: ApiKeyService

    /**
     * Obtiene una clave API por su ID.
     *
     * @param id El ID de la clave API.
     * @param request La solicitud HTTP.
     * @return ResponseEntity con la clave API o un error si no se encuentra.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Fetch api-key by ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ApiKey::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun findById(
        @PathVariable id: String,
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val key = apiKeyService.findById(id)
        return if (key.isPresent)
            ResponseEntity(key.get(), HttpStatus.OK)
        else ResponseEntity(ErrorResponse(
            errMessage = "ApiKey not found",
            code = NOT_FOUND,
            detail = "The id of the api key provided might be wrong!",
            path = request.requestURL.toString(),
            method = request.method.toString()
        ), NOT_FOUND)
    }

    /**
     * Obtiene todas las claves API disponibles.
     *
     * @param request La solicitud HTTP.
     * @return ResponseEntity con todas las claves API o un error si ocurre un problema.
     */
    @GetMapping("/registries")
    @Operation(summary = "Fetch al api-keys available")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CustomResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun findAll(
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val response = apiKeyService.findAll(request)
        if (response is ErrorResponse)
            return ResponseEntity(response, response.code)

        return ResponseEntity(response, HttpStatus.OK)
    }

}