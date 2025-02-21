package com.springBootApi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "Api_Keys")
data class ApiKey(
    @Id val _id: String? = null,
    val key: String = UUID.randomUUID().toString(),
    val expiresAt: LocalDateTime = LocalDateTime.now().plusMonths(1),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val role: ApiKeyRole = ApiKeyRole.CLIENT,
    val enabled: Boolean = true,
    val rateLimit: Int = 100
): ResponseModel()

enum class ApiKeyRole {
    CLIENT, ADMIN
}
