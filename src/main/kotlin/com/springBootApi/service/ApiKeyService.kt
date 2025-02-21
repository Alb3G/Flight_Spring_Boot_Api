package com.springBootApi.service

import com.springBootApi.model.*
import com.springBootApi.repository.ApiKeyRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

/**
 * Servicio para gestionar las claves API.
 */
@Service
class ApiKeyService {

    @Autowired
    lateinit var apiKeyRepository: ApiKeyRepository

    /**
     * Encuentra una clave API por su valor.
     *
     * @param key El valor de la clave API.
     * @return La clave API encontrada.
     */
    fun findByKey(key: String) = apiKeyRepository.findByKey(key)

    /**
     * Encuentra una clave API por su ID.
     *
     * @param id El ID de la clave API.
     * @return La clave API encontrada.
     */
    fun findById(id: String) = apiKeyRepository.findById(id)

    /**
     * Encuentra todas las claves API con paginación.
     *
     * @param request La solicitud HTTP.
     * @param page El número de página.
     * @param pageSize El tamaño de la página.
     * @return Un modelo de respuesta con las claves API encontradas.
     */
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

    /**
     * Guarda una clave API.
     *
     * @param apiKey La clave API a guardar.
     * @return La clave API guardada.
     */
    fun saveApiKey(apiKey: ApiKey): ApiKey = apiKeyRepository.save(apiKey)

    /**
     * Crea una nueva clave API con un rol específico.
     *
     * @param role El rol de la clave API.
     * @return La clave API creada.
     */
    fun createApiKey(role: ApiKeyRole = ApiKeyRole.CLIENT): ApiKey {
        val apikey = ApiKey(role = role)
        return saveApiKey(apiKey = apikey)
    }

    /**
     * Asigna una clave API a un usuario.
     *
     * @param user El usuario al que se le asignará la clave API.
     * @param key La clave API a asignar.
     * @return El usuario con la clave API asignada.
     */
    fun assignKeyToUser(user: User, key: ApiKey): User {
        return user.copy(key = key)
    }

}