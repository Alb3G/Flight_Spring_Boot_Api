package com.springBootApi.controller

import com.springBootApi.model.ErrorResponse
import com.springBootApi.model.ResponseModel
import com.springBootApi.model.User
import com.springBootApi.service.UserService
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
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping("/register")
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