package com.springBootApi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

/**
 * Representa una clave API.
 *
 * @param _id El identificador de la clave API.
 * @param key El valor de la clave API.
 * @param expiresAt La fecha de expiración de la clave API.
 * @param createdAt La fecha de creación de la clave API.
 * @param role El rol asociado a la clave API.
 * @param enabled Indica si la clave API está habilitada.
 * @param rateLimit El límite de solicitudes permitidas por la clave API.
 */
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

/**
 * Enumera los roles posibles para una clave API.
 */
enum class ApiKeyRole {
    CLIENT, ADMIN
}
