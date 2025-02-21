package com.springBootApi.service

import com.springBootApi.model.CustomResponse
import com.springBootApi.model.ErrorResponse
import com.springBootApi.model.FlightRegistry
import com.springBootApi.model.ResponseModel
import com.springBootApi.repository.FlightsRepository
import jakarta.servlet.http.HttpServletRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class FlightService(
    private val flightsRepository: FlightsRepository,
) {

    fun addRegistry(flightRegistry: FlightRegistry, request: HttpServletRequest): ResponseModel {
        val savedRegistry = flightsRepository.save(flightRegistry)

        return CustomResponse(
            total = 1,
            page = 1,
            pageSize = 1,
            totalPages = 1,
            prevPage = "No available page",
            nextPage = "No available page",
            data = listOf(savedRegistry)
        )
    }

    fun findAll(page: Int, pageSize: Int, request: HttpServletRequest): ResponseModel {
        // Tenemos que restar 1 porque las páginas en Mongo empiezan desde 0
        val pageable = PageRequest.of(if (page < 1) 1 else page, pageSize)
        val pageResult = flightsRepository.findAll(pageable)

        val totalItems = pageResult.totalElements
        // Aqui también restamos 1 porque sino la última página nos la devuelve sin datos
        val totalPages = pageResult.totalPages - 1

        when {
             page < 1 -> {
                 return ErrorResponse(
                     errMessage = "Page number $page not available",
                     code = BAD_REQUEST,
                     detail = "Pages start from number 1!",
                     path = request.requestURL.toString(),
                     method = request.method.toString()
                 )
             }
            page > totalPages -> {
                return ErrorResponse(
                    errMessage = "Page number $page not reachable",
                    code = BAD_REQUEST,
                    detail = "The page you are trying to reach has no elements in it!",
                    path = request.requestURL.toString(),
                    method = request.method.toString()
                )
            }
        }

        val prevPage = if (page > 1) "flights?page=${page - 1}&page_size=${pageSize}" else "flights?page=${totalPages - 1}&pageSize=${pageSize}"
        val nextPage = if (page < totalPages) "flights?page=${page + 1}&page_size=${pageSize}" else "flights?page=1&pageSize=${pageSize}"

        return CustomResponse(
            total =  totalItems.toInt(),
            page = page,
            pageSize = pageSize,
            totalPages = totalPages,
            prevPage = prevPage,
            nextPage = nextPage,
            data = pageResult.content,
        )
    }

    fun findRegistryById(id: String, request: HttpServletRequest): ResponseModel {
        val flightRegistry = flightsRepository.findById(id)
        if (!flightRegistry.isPresent)
            return ErrorResponse(
                errMessage = "Element with id: ($id) not found.",
                code = NOT_FOUND,
                detail = "Using an invalid or wrong id for flight Registry!",
                path = request.requestURL.toString(),
                method = request.method.toString()
            )

        return flightRegistry.get()
    }

    fun findByOriginAndDestination(
        origin: String =  "",
        destination: String = "",
        request: HttpServletRequest,
        page: Int = 1,
        pageSize: Int = 10
    ): ResponseModel {
        val pageable = PageRequest.of((page - 1).coerceAtLeast(0), pageSize)
        lateinit var pageResult: Page<FlightRegistry>
        val useCase = initializeLocationPage(origin, destination)

        when(useCase) {
            0 -> {
                println("Buscando por origen: $origin")
                pageResult = flightsRepository.findByOrigin(origin, pageable)
                println("Resultados: ${pageResult.content}")
            }
            1 -> {
                println("Buscando por destino: $destination")
                pageResult = flightsRepository.findByDestination(destination, pageable)
                println("Resultados: ${pageResult.content}")
            }
            2 -> {
                println("Buscando por origen y destino: $origin - $destination")
                pageResult = flightsRepository.findByOriginAndDestination(origin, destination, pageable)
                println("Resultados: ${pageResult.content}")
            }
            3 -> {
                println("Sin filtros válidos")
                pageResult = Page.empty()
            }
        }

        if (pageResult.isEmpty)
            return ErrorResponse(
                errMessage = "No flights found with origin: $origin and destination: $destination",
                code = NOT_FOUND,
                detail = "No flights found with the origin and destination you provided!",
                path = request.requestURL.toString(),
                method = request.method.toString()
            )

        val totalItems = pageResult.totalElements
        val totalPages = pageResult.totalPages - 1

        val prevPage = if (page > 1) "flights/routes?page=${page - 1}&page_size=${pageSize}" else "flights?page=${totalPages - 1}&pageSize=${pageSize}"
        val nextPage = if (page < totalPages) "flights/routes?page=${page + 1}&page_size=${pageSize}" else "flights?page=1&pageSize=${pageSize}"

        val response = CustomResponse(
            total = totalItems.toInt(),
            page = page,
            pageSize = pageSize,
            totalPages = totalPages,
            prevPage = prevPage,
            nextPage = nextPage,
            data = pageResult.content
        )

        return response
    }

    fun findBySeason(
        year: Int,
        month: Int = 0,
        page: Int = 1,
        pageSize: Int = 10,
        request: HttpServletRequest
    ): ResponseModel {
        val pageable = PageRequest.of((page - 1).coerceAtLeast(0), pageSize)
        lateinit var pageResult: Page<FlightRegistry>
        val useCase = initializeSeasonPage(year, month)

        when(useCase) {
            0 -> {
                println("Buscando por año y mes: $year - $month")
                pageResult = flightsRepository.findByYearAndMonth(year, month, pageable)
                println("Resultados: ${pageResult.content}")
            }
            1 -> {
                println("Buscando por año: $year")
                pageResult = flightsRepository.findByYear(year, pageable)
                println("Resultados: ${pageResult.content}")
            }
            2 -> {
                println("Buscando por mes: $month")
                pageResult = flightsRepository.findByMonth(month, pageable)
                println("Resultados: ${pageResult.content}")
            }
            3 -> {
                println("Sin filtros válidos")
                pageResult = Page.empty()
            }
        }

        when {
            pageResult.isEmpty && useCase == 0 -> {
                return ErrorResponse(
                    errMessage = "No flights found for year: $year and month: $month",
                    code = NOT_FOUND,
                    detail = "No flights found for the year and month you provided!",
                    path = request.requestURL.toString(),
                    method = request.method.toString()
                )
            }
            pageResult.isEmpty && useCase == 1 -> {
                return ErrorResponse(
                    errMessage = "No flights found for year: $year",
                    code = NOT_FOUND,
                    detail = "No flights found for the year you provided!",
                    path = request.requestURL.toString(),
                    method = request.method.toString()
                )
            }
            pageResult.isEmpty && useCase == 2 -> {
                return ErrorResponse(
                    errMessage = "No flights found for the month: $month",
                    code = NOT_FOUND,
                    detail = "No flights found for the month you provided!",
                    path = request.requestURL.toString(),
                    method = request.method.toString()
                )
            }
            pageResult.isEmpty && useCase == 3 -> {
                return ErrorResponse(
                    errMessage = "No flights found for the filters provided",
                    code = NOT_FOUND,
                    detail = "No flights found for the filters you provided!",
                    path = request.requestURL.toString(),
                    method = request.method.toString()
                )
            }
        }

        val totalItem = pageResult.totalElements
        val totalPages = pageResult.totalPages

        val prevPage = if (page > 1) "flights/year/$year?page=${page - 1}&pageSize=$pageSize" else ""
        val nextPage = if (page < totalPages) "flights/year/$year?page=${page + 1}&pageSize=$pageSize" else "flights/year/?page=1&pageSize=$pageSize"

        return CustomResponse(
            total = totalItem.toInt(),
            page = page,
            pageSize = pageSize,
            totalPages = totalPages,
            prevPage = prevPage,
            nextPage = nextPage,
            data = pageResult.content
        )
    }

    fun findMaxPaxFlight(request: HttpServletRequest): ResponseModel {
        val flight = flightsRepository.findMaxPaxFlight()
            ?: return ErrorResponse(
                errMessage = "Something went wrong",
                code = INTERNAL_SERVER_ERROR,
                detail = "Something went wrong trying to retrieve the max pax flight",
                path = request.requestURL.toString(),
                method = request.method.toString()
            )

        return CustomResponse(
            total = 1,
            page = 1,
            pageSize = 1,
            totalPages = 1,
            prevPage = "",
            nextPage = "",
            data = listOf(flight)
        )
    }

    private fun initializeLocationPage(origin: String, destination: String): Int {
        return when {
            origin.isNotBlank() && destination.isBlank() -> 0  // Filtrar solo por origen
            destination.isNotBlank() && origin.isBlank() -> 1  // Filtrar solo por destino
            origin.isNotBlank() && destination.isNotBlank() -> 2  // Filtrar por ambos
            else -> 3  // No se proporcionaron filtros
        }
    }

    private fun initializeSeasonPage(year: Int, month: Int): Int {
        val currentYear = LocalDate.now().year
        val yearInRange = year in 2019..currentYear
        val monthInRange = month in 1..12
        return when {
            yearInRange && monthInRange -> 0  // Filtrar por año y mes
            yearInRange && month == 0 -> 1  // Filtrar solo por año
            year == 0 && monthInRange -> 2 // Filtrar solo por mes
            else -> 3  // No se proporcionaron filtros
        }
    }
}