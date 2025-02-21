package com.springBootApi.controller

import com.springBootApi.model.CustomResponse
import com.springBootApi.model.ErrorResponse
import com.springBootApi.model.FlightRegistry
import com.springBootApi.model.ResponseModel
import com.springBootApi.service.FlightService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
@Tag(name = "flights-controller", description = "Flight operations")
class FlightsController {

    @Autowired
    private lateinit var flightService: FlightService

    @GetMapping("/flights")
    @Operation(summary = "Fetch all available flight in pages of 10 elements")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CustomResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun findAll(
        @RequestParam page: Int = 1,
        @RequestParam pageSize: Int = 10,
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val response = flightService.findAll(page, pageSize, request)

        if (response is ErrorResponse)
            return ResponseEntity(response, response.code)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/flights/{id}")
    @Operation(summary = "Fetch single flight by Id")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = FlightRegistry::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun findRegistryById(@PathVariable id: String, request: HttpServletRequest): ResponseEntity<ResponseModel> {
        val registryOptional = flightService.findRegistryById(id, request)
        
        if (registryOptional is ErrorResponse)
            return ResponseEntity(registryOptional, registryOptional.code)

        return ResponseEntity(registryOptional, HttpStatus.OK)
    }

    @GetMapping("/flights/routes")
    @Operation(summary = "Fetch flights by origin, destination or both at the same time.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CustomResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
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
    @Operation(summary = "Filter flights by year, by month or both")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CustomResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
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
    @Operation(summary = "Fetch flight wit max amount of passengers")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CustomResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    fun findMaxPaxFlight(
        request: HttpServletRequest
    ): ResponseEntity<ResponseModel> {
        val response = flightService.findMaxPaxFlight(request)

        if (response is ErrorResponse)
            return ResponseEntity(response, response.code)

        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/addRegistry")
    @Operation(summary = "Add a new flight, endpoint protected by Admin key only.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = CustomResponse::class))]
            )
        ]
    )
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