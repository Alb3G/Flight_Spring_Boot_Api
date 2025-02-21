package com.springBootApi.model

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

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

data class ErrorResponse(
    val errMessage: String,
    val code: HttpStatus,
    val detail: String,
    val timeStamp: String = LocalDateTime.now().toString(),
    val path: String,
    val method: String
): ResponseModel()

data class UserResponse(
    val message: String,
    val user: User
): ResponseModel()

//data class ApiKeyResponse(
//    val key: String,
//    val name: String,
//    val expiresAt: LocalDateTime,
//    val role: ApiKeyRole,
//    val rateLimit: Int
//)

