package com.springBootApi.repository

import com.springBootApi.model.ApiKey
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repositorio para la entidad ApiKey.
 */
@Repository
interface ApiKeyRepository : MongoRepository<ApiKey, String>  {

    /**
     * Encuentra una ApiKey por su ID.
     *
     * @param id El ID de la ApiKey.
     * @return Un Optional que contiene la ApiKey si se encuentra.
     */
    override fun findById(id: String): Optional<ApiKey>

    /**
     * Encuentra una ApiKey por su clave.
     *
     * @param key La clave de la ApiKey.
     * @return Un Optional que contiene la ApiKey si se encuentra.
     */
    fun findByKey(key: String): Optional<ApiKey>

    /**
     * Guarda una entidad ApiKey.
     *
     * @param entity La entidad ApiKey a guardar.
     * @return La entidad ApiKey guardada.
     */
    override fun <S : ApiKey?> save(entity: S & Any): S & Any

}