package com.springBootApi.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

/**
 * Representa un registro de vuelo.
 *
 * @param _id El identificador del registro de vuelo.
 * @param year El año del vuelo.
 * @param month El mes del vuelo.
 * @param origin El origen del vuelo.
 * @param destination El destino del vuelo.
 * @param originType El tipo de origen del vuelo.
 * @param passengers El número de pasajeros en el vuelo.
 * @param annualVariation La variación anual de pasajeros.
 */
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
