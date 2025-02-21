package com.springBootApi.repository

import com.springBootApi.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Repositorio para la entidad `User`.
 */
@Repository
interface UserRepository : MongoRepository<User, String> {

    /**
     * Guarda una entidad `User`.
     *
     * @param entity La entidad `User` a guardar.
     * @return La entidad `User` guardada.
     */
    override fun <S : User?> save(entity: S & Any): S & Any

    /**
     * Verifica si existe un usuario con el correo electrónico proporcionado.
     *
     * @param email El correo electrónico a verificar.
     * @return `true` si existe un usuario con el correo electrónico, `false` en caso contrario.
     */
    fun existsByEmail(email: String): Boolean

    /**
     * Encuentra un usuario por su correo electrónico.
     *
     * @param email El correo electrónico del usuario a encontrar.
     * @return El usuario encontrado.
     */
    fun findByEmail(email: String): User

}