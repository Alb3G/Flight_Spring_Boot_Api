package com.springBootApi.controller

import com.springBootApi.model.ApiKey
import com.springBootApi.service.ApiKeyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api-keys")
class ApiKeyController {

    @Autowired
    lateinit var apiKeyService: ApiKeyService

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ApiKey? {
        val key = apiKeyService.findById(id)
        println(key.get())
        return if (key.isPresent) key.get() else null
    }

    @GetMapping("/registries")
    fun findAll(): ResponseEntity<List<ApiKey>> {
        val apiKeys = apiKeyService.findAll()
        val response = ResponseEntity(apiKeys, HttpStatus.OK)
        return response
    }

}