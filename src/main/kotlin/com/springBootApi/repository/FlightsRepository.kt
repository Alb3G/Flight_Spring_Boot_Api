package com.springBootApi.repository

import com.springBootApi.model.FlightRegistry
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * Repositorio para gestionar las operaciones de la entidad FlightRegistry en MongoDB.
 */
@Repository
interface FlightsRepository : MongoRepository<FlightRegistry, String> {

    /**
     * Encuentra todos los registros de vuelos con paginación.
     *
     * @param pageable Información de paginación.
     * @return Una página de registros de vuelos.
     */
    override fun findAll(pageable: Pageable): Page<FlightRegistry>

    /**
     * Encuentra un registro de vuelo por su ID.
     *
     * @param id El ID del registro de vuelo.
     * @return Un Optional con el registro de vuelo si se encuentra.
     */
    override fun findById(id: String): Optional<FlightRegistry>

    /**
     * Guarda un registro de vuelo.
     *
     * @param entity El registro de vuelo a guardar.
     * @return El registro de vuelo guardado.
     */
    override fun <S : FlightRegistry?> save(entity: S & Any): S & Any

    /**
     * Encuentra registros de vuelos por origen con paginación.
     *
     * @param origin El origen del vuelo.
     * @param pageable Información de paginación.
     * @return Una página de registros de vuelos.
     */
    fun findByOrigin(origin: String, pageable: Pageable): Page<FlightRegistry>

    /**
     * Encuentra registros de vuelos por destino con paginación.
     *
     * @param destination El destino del vuelo.
     * @param pageable Información de paginación.
     * @return Una página de registros de vuelos.
     */
    fun findByDestination(destination: String, pageable: Pageable): Page<FlightRegistry>

    /**
     * Encuentra registros de vuelos por origen y destino con paginación.
     *
     * @param origin El origen del vuelo.
     * @param destination El destino del vuelo.
     * @param pageable Información de paginación.
     * @return Una página de registros de vuelos.
     */
    fun findByOriginAndDestination(origin: String, destination: String, pageable: Pageable): Page<FlightRegistry>

    /**
     * Encuentra registros de vuelos por año con paginación.
     *
     * @param year El año del vuelo.
     * @param pageable Información de paginación.
     * @return Una página de registros de vuelos.
     */
    fun findByYear(year: Int, pageable: Pageable): Page<FlightRegistry>

    /**
     * Encuentra registros de vuelos por año y mes con paginación.
     *
     * @param year El año del vuelo.
     * @param month El mes del vuelo.
     * @param pageable Información de paginación.
     * @return Una página de registros de vuelos.
     */
    fun findByYearAndMonth(year: Int, month: Int, pageable: Pageable): Page<FlightRegistry>

    /**
     * Encuentra registros de vuelos por mes con paginación.
     *
     * @param month El mes del vuelo.
     * @param pageable Información de paginación.
     * @return Una página de registros de vuelos.
     */
    fun findByMonth(month: Int, pageable: Pageable): Page<FlightRegistry>

    /**
     * Encuentra el vuelo con el máximo número de pasajeros.
     *
     * @return El registro de vuelo con el máximo número de pasajeros.
     */
    @Aggregation(
        pipeline = [
            "{'\$sort': {'passengers': -1} }",
            "{'\$limit': 1}"
        ]
    )
    fun findMaxPaxFlight(): FlightRegistry?
}