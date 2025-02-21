package com.springBootApi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

/**
 * Representa un usuario en el sistema.
 *
 * @param _id Identificador único del usuario.
 * @param email Correo electrónico del usuario.
 * @param password Contraseña del usuario.
 * @param key Clave API asociada al usuario.
 * @param isActive Indica si el usuario está activo.
 * @param createdAt Fecha y hora de creación del usuario.
 */
@Document(collection = "Users")
data class User(
    @Id val _id: String? = null,
    val email: String,
    val password: String,
    val key: ApiKey? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
