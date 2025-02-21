package com.springBootApi.repository

import com.springBootApi.model.ApiKey
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ApiKeyRepository : MongoRepository<ApiKey, String>  {

    override fun findById(id: String): Optional<ApiKey>

    fun findByKey(key: String): Optional<ApiKey>

    override fun <S : ApiKey?> save(entity: S & Any): S & Any

}