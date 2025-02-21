package com.springBootApi.service

import com.springBootApi.model.ApiKey
import com.springBootApi.model.ApiKeyRole
import com.springBootApi.model.User
import com.springBootApi.repository.ApiKeyRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ApiKeyService {

    @Autowired
    lateinit var apiKeyRepository: ApiKeyRepository

    fun findByKey(key: String) = apiKeyRepository.findByKey(key)

    fun findById(id: String) = apiKeyRepository.findById(id)

    fun findAll(): List<ApiKey> = apiKeyRepository.findAll()

    fun saveApiKey(apiKey: ApiKey): ApiKey = apiKeyRepository.save(apiKey)

    fun createApiKey(role: ApiKeyRole = ApiKeyRole.CLIENT): ApiKey {
        val apikey = ApiKey(role = role)
        return saveApiKey(apiKey = apikey)
    }

    fun assignKeyToUser(user: User, key: ApiKey): User {
        return user.copy(key = key)
    }

}