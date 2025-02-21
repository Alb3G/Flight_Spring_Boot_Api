package com.springBootApi.controller

import com.springBootApi.model.ErrorResponse
import com.springBootApi.model.FlightRegistry
import com.springBootApi.model.ResponseModel
import com.springBootApi.service.FlightService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Flights Api", description = "Operaciones relacionadas con registros de vuelos")
class FlightsController {

    @Autowired
    private lateinit var flightService: FlightService

    @GetMapping("/flights")
    @Operation(summary = "Fetch all available flight in pages of 10 elements")
    fun findAll(
        @RequestParam page: Int = 1,
        @RequestParam pageSize: Int = 10,
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val response = flightService.findAll(page, pageSize, request)

        if (response is ErrorResponse)
            return ResponseEntity(response, HttpStatus.BAD_REQUEST)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/flights/{id}")
    fun findRegistryById(@PathVariable id: String, request: HttpServletRequest): ResponseEntity<ResponseModel> {
        val registryOptional = flightService.findRegistryById(id, request)
        
        if (registryOptional is ErrorResponse)
            return ResponseEntity(registryOptional, registryOptional.code)

        return ResponseEntity(registryOptional, HttpStatus.OK)
    }

    @GetMapping("/flights/routes")
    fun findRegistryByOrigin(
        @RequestParam origin: String?,
        @RequestParam destination: String?,
        @RequestParam page: Int = 1,
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val registryResponse = flightService.findByOriginAndDestination(
            origin = origin ?: "",
            page = page,
            destination = destination ?: "",
            request = request
        )

        if (registryResponse is ErrorResponse)
            return ResponseEntity(registryResponse, registryResponse.code)

        return ResponseEntity(registryResponse, HttpStatus.OK)
    }

    @GetMapping("/flights/year")
    fun findByYear(
        @RequestParam year: Int = 0,
        @RequestParam page: Int = 1,
        @RequestParam month: Int = 0,
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val response = flightService.findBySeason(year, month, page, request = request)

        if (response is ErrorResponse)
            return ResponseEntity(response, response.code)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/flights/maxPax")
    fun findMaxPaxFlight(
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val response = flightService.findMaxPaxFlight(request)

        if (response is ErrorResponse)
            return ResponseEntity(response, response.code)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/addRegistry")
    fun addRegistry(
        @RequestBody registry: FlightRegistry,
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val response = flightService.addRegistry(registry, request)

        if (response is ErrorResponse)
            return ResponseEntity(response, response.code)

        return ResponseEntity(response, HttpStatus.CREATED)
    }

}