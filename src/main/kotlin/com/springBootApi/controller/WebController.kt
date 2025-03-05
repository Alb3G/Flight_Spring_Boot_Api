package com.springBootApi.controller

import com.springBootApi.model.CustomResponse
import com.springBootApi.model.ErrorResponse
import com.springBootApi.service.FlightService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/web")
class WebController {

    @Autowired
    private lateinit var flightService: FlightService

    @GetMapping("/home")
    fun home(
        model: Model,
        request: HttpServletRequest,
        @RequestParam page: Int = 1,
        @RequestParam pageSize: Int = 10,
    ): String {
        val flightsResponse = flightService.findAll(page, pageSize, request)

        if (flightsResponse is ErrorResponse) {
            model.addAttribute("error", flightsResponse)
            return "error"
        }

        val flights = flightsResponse as CustomResponse
        model.addAttribute("flights", flights.data)
        model.addAttribute("currentPage", page)
        model.addAttribute("totalPages", flights.totalPages)

        return "home"
    }


    @GetMapping("/flight/{id}")
    fun flightDetail(
        @PathVariable("id") id: String,
        request: HttpServletRequest,
        model: Model,
    ): String {
        val flightResponse = flightService.findRegistryById(id, request)
        if (flightResponse is ErrorResponse) {
            model.addAttribute("error", flightResponse)
            return "error"
        }
        model.addAttribute("flight", flightResponse)
        return "flightDetail"
    }

}