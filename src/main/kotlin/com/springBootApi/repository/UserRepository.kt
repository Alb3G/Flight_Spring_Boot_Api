package com.springBootApi.repository

import com.springBootApi.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {

    override fun <S : User?> save(entity: S & Any): S & Any

    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): User

}