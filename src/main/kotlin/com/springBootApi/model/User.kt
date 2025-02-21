package com.springBootApi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "Users")
data class User(
    @Id val _id: String? = null,
    val email: String,
    val password: String,
    val key: ApiKey? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
