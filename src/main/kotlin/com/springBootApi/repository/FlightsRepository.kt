package com.springBootApi.repository

import com.springBootApi.model.FlightRegistry
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface FlightsRepository : MongoRepository<FlightRegistry, String> {

    override fun findAll(pageable: Pageable): Page<FlightRegistry>

    override fun findById(id: String): Optional<FlightRegistry>

    override fun <S : FlightRegistry?> save(entity: S & Any): S & Any

    fun findByOrigin(origin: String, pageable: Pageable): Page<FlightRegistry>

    fun findByDestination(destination: String, pageable: Pageable): Page<FlightRegistry>

    fun findByOriginAndDestination(origin: String, destination: String, pageable: Pageable): Page<FlightRegistry>

    fun findByYear(year: Int, pageable: Pageable): Page<FlightRegistry>

    fun findByYearAndMonth(year: Int, month: Int, pageable: Pageable): Page<FlightRegistry>

    fun findByMonth(month: Int, pageable: Pageable): Page<FlightRegistry>

    @Aggregation(
        pipeline = [
            "{'\$sort': {'passengers': -1} }",
            "{'\$limit': 1}"
        ]
    )
    fun findMaxPaxFlight(): FlightRegistry?
}