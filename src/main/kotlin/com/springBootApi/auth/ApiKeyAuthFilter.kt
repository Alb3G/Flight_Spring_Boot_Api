package com.springBootApi.auth

import com.springBootApi.service.ApiKeyService
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.time.LocalDateTime

/**
 * Filtro de autenticación basado en API Key.
 * Este filtro se ejecuta una vez por solicitud y verifica la validez de la API Key.
 *
 * @property apiKeyService Servicio para manejar las API Keys.
 */
@Component
class ApiKeyAuthFilter(
    private val apiKeyService: ApiKeyService,
) : OncePerRequestFilter() {
    /**
     * Método que realiza el filtrado interno de la solicitud.
     *
     * @param request La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @param filterChain La cadena de filtros.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val allowedPaths = listOf(
            "/swagger-ui.html",
            "/swagger-ui/index.html",
            "/swagger-ui/",
            "/v1/api-docs",
            "/v1/api-docs/",
            "/webjars/",
            "/swagger-resources/",
            "/api/v1/register",
            "/api/v1/accountInfo"
        )

        val apiKey = request.getHeader("X-API-KEY")

        if (allowedPaths.any { request.requestURI.startsWith(it) }) {
            filterChain.doFilter(request, response)
            return
        }

        if (apiKey == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Api key field!")
            return
        }

        val apiKeyOptional = apiKeyService.findByKey(apiKey)

        if (apiKeyOptional.isEmpty) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "API key not found!")
            return
        }

        val key = apiKeyOptional.get()

        if (!key.enabled) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Disabled API key")
            return
        }

        if (key.expiresAt.isBefore(LocalDateTime.now())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "API key expired")
            return
        }

        // Crear autenticación con rol basado en la API Key
        val authorities = listOf(GrantedAuthority { "ROLE_${key.role.name.uppercase()}" }) // Convertir a formato ADMIN
        val user = User(key.key, "", authorities)
        val authentication = UsernamePasswordAuthenticationToken(user, null, authorities)

        // Guardar autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().authentication = authentication

        request.setAttribute("role", key.role)
        filterChain.doFilter(request, response)
    }

}
