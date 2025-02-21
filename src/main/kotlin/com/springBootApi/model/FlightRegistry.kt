package com.springBootApi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Flights")
data class FlightRegistry(
    @Id val _id: String? = null,
    val year: Int,
    val month: Int,
    val origin: String,
    val destination: String,
    val originType: String,
    val passengers: Int,
    val annualVariation: String
): ResponseModel()
