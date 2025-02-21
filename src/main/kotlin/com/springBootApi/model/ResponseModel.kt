package com.springBootApi.model

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

/**
 * Modelo de respuesta personalizada.
 *
 * @param total El número total de elementos.
 * @param page El número de la página actual.
 * @param pageSize El tamaño de la página.
 * @param totalPages El número total de páginas.
 * @param data La lista de datos de la respuesta.
 * @param prevPage La URL de la página anterior.
 * @param nextPage La URL de la página siguiente.
 */
data class CustomResponse(
    val total: Int?,
    val page: Int?,
    val pageSize: Int?,
    val totalPages: Int?,
    val data: List<ResponseModel>,
    val prevPage: String?,
    val nextPage: String?,
): ResponseModel()

sealed class ResponseModel

/**
 * Modelo de respuesta de error.
 *
 * @param errMessage El mensaje de error.
 * @param code El código de estado HTTP.
 * @param detail Los detalles del error.
 * @param timeStamp La marca de tiempo del error.
 * @param path La ruta de la solicitud que causó el error.
 * @param method El método HTTP de la solicitud que causó el error.
 */
data class ErrorResponse(
    val errMessage: String,
    val code: HttpStatus,
    val detail: String,
    val timeStamp: String = LocalDateTime.now().toString(),
    val path: String,
    val method: String
): ResponseModel()

/**
 * Modelo de respuesta de usuario.
 *
 * @param message El mensaje de la respuesta.
 * @param user El usuario de la respuesta.
 */
data class UserResponse(
    val message: String,
    val user: User
): ResponseModel()