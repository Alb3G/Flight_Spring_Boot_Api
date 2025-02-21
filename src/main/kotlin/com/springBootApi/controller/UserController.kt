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

@RestController
@RequestMapping("/api/v1")
@Tag(name = "user-controller", description = "User operations like register or account Info")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/register")
    @Operation(summary = "Creates an account and sets an api key for the user")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
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

    @PostMapping("/accountInfo")
    @Operation(summary = "Fetch all account information if password is correct")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = UserResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
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

    data class RegisterBody(
        val email: String,
        val password: String
    )
}