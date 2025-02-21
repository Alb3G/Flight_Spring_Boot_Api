package com.springBootApi.service

import com.springBootApi.model.*
import com.springBootApi.repository.ApiKeyRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class ApiKeyService {

    @Autowired
    lateinit var apiKeyRepository: ApiKeyRepository

    fun findByKey(key: String) = apiKeyRepository.findByKey(key)

    fun findById(id: String) = apiKeyRepository.findById(id)

    fun findAll(request: HttpServletRequest, page: Int = 1, pageSize: Int = 10): ResponseModel {
        val pageable = PageRequest.of((page - 1).coerceAtLeast(0), pageSize)
        val pageResult = apiKeyRepository.findAll(pageable)

        return if (pageResult.hasContent()) {
            val totalItems = pageResult.totalElements
            val totalPages = pageResult.totalPages

            val prevPage = if (page > 1) "apiKeys?page=${page - 1}&pageSize=${pageSize}" else ""
            val nextPage = if (page < totalPages) "apiKeys?page=${page + 1}&pageSize=${pageSize}" else ""

            CustomResponse(
                total = totalItems.toInt(),
                page = page,
                pageSize = pageSize,
                totalPages = totalPages,
                prevPage = prevPage,
                nextPage = nextPage,
                data = pageResult.content
            )
        } else {
            ErrorResponse(
                errMessage = "ApiKey not found",
                code = HttpStatus.NOT_FOUND,
                detail = "No API keys found!",
                path = request.requestURL.toString(),
                method = request.method.toString()
            )
        }
    }

    fun saveApiKey(apiKey: ApiKey): ApiKey = apiKeyRepository.save(apiKey)

    fun createApiKey(role: ApiKeyRole = ApiKeyRole.CLIENT): ApiKey {
        val apikey = ApiKey(role = role)
        return saveApiKey(apiKey = apikey)
    }

    fun assignKeyToUser(user: User, key: ApiKey): User {
        return user.copy(key = key)
    }

}