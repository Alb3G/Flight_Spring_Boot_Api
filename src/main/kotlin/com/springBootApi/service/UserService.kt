@file:Suppress("SENSELESS_COMPARISON")

package com.springBootApi.service

import com.springBootApi.model.*
import com.springBootApi.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Service

/**
 * Servicio para la gestión de usuarios.
 *
 * @property userRepository Repositorio de usuarios.
 * @property apiKeyService Servicio para la gestión de claves API.
 */
@Service
class UserService(
    private val userRepository: UserRepository,
    private val apiKeyService: ApiKeyService
) {

    /**
     * Crea un nuevo usuario.
     *
     * @param user Datos del usuario a crear.
     * @param request Solicitud HTTP.
     * @return Modelo de respuesta con el resultado de la operación.
     */
    fun createUser(user: User, request: HttpServletRequest): ResponseModel {
        val userExist = userRepository.existsByEmail(user.email)

        if (userExist)
            return ErrorResponse(
                errMessage = "User already Exists",
                code = CONFLICT,
                detail = "You already have an account try to login instead!",
                path = request.requestURL.toString(),
                method = request.method.toString()
            )

        val response = userRepository.save(user)

        if (response == null)
            return ErrorResponse(
                errMessage = "Error while creating your account",
                code = INTERNAL_SERVER_ERROR,
                detail = "We had some trouble creating your account",
                path = request.requestURL.toString(),
                method = request.method.toString()
            )

        val userFromDb = userRepository.findByEmail(user.email)

        val key = apiKeyService.createApiKey() // Crea y guarda la clave en la base de datos
        val userWithKey = apiKeyService.assignKeyToUser(userFromDb, key)

        userRepository.save(userWithKey)

        return UserResponse(
            message = "Account created Succesfully",
            user = userWithKey
        )
    }

    /**
     * Valida las credenciales de un usuario.
     *
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param request Solicitud HTTP.
     * @return Modelo de respuesta con el resultado de la operación.
     */
    fun validateUser(email: String, password: String, request: HttpServletRequest): ResponseModel {
        val user = userRepository.findByEmail(email)
        if (user == null)
            return ErrorResponse(
                errMessage = "User not found",
                code = NOT_FOUND,
                detail = "User with email: $email not found!",
                path = request.requestURL.toString(),
                method = request.method.toString()
            )

        if (password != user.password)
            return ErrorResponse(
                errMessage = "Wrong Password!",
                code = UNAUTHORIZED,
                detail = "The password you are using is not correct!",
                path = request.requestURL.toString(),
                method = request.method.toString()
            )

        return UserResponse(
            message = "Account info retrieved succesfully!",
            user = user
        )
    }
}