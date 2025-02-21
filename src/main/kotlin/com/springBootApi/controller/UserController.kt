package com.springBootApi.controller

import com.springBootApi.model.*
import com.springBootApi.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Controlador de usuarios.
 *
 * Proporciona operaciones como registrar una cuenta o obtener información de la cuenta.
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "user-controller", description = "Operaciones de usuario como registrar o información de cuenta")
class UserController {

    @Autowired
    lateinit var userService: UserService

    /**
     * Crea una cuenta y asigna una clave API al usuario.
     *
     * @param body El cuerpo de la solicitud que contiene el correo electrónico y la contraseña.
     * @param request La solicitud HTTP.
     * @return ResponseEntity con el modelo de respuesta.
     */
    @PostMapping("/register")
    @Operation(summary = "Crea una cuenta y asigna una clave API al usuario")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Éxito",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Solicitud incorrecta",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun register(
        @RequestBody body: RegisterBody,
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val user = User(
            email = body.email,
            password = body.password,
        )
        val response = userService.createUser(user, request)

        if (response is ErrorResponse)
            return ResponseEntity(response, response.code)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * Obtiene toda la información de la cuenta si la contraseña es correcta.
     *
     * @param body El cuerpo de la solicitud que contiene el correo electrónico y la contraseña.
     * @param request La solicitud HTTP.
     * @return ResponseEntity con el modelo de respuesta.
     */
    @PostMapping("/accountInfo")
    @Operation(summary = "Obtiene toda la información de la cuenta si la contraseña es correcta")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Éxito",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Solicitud incorrecta",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun getAccountInfo(
        @RequestBody body: RegisterBody,
        request: HttpServletRequest
    ):ResponseEntity<ResponseModel> {
        val response = userService.validateUser(body.email, body.password, request)

        if (response is ErrorResponse)
            return ResponseEntity(response, response.code)

        return ResponseEntity(response, HttpStatus.OK)
    }

    /**
     * Cuerpo de la solicitud para registrar o obtener información de la cuenta.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     */
    data class RegisterBody(
        val email: String,
        val password: String
    )
}