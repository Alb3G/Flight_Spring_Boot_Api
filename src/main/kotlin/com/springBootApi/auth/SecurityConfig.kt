package com.springBootApi.auth

import com.springBootApi.service.ApiKeyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Autowired
    lateinit var apiKeyService: ApiKeyService

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors {  } // Habilitar CORS
            .csrf { it.disable() } // Deshabilitar CSRF para permitir peticiones externas
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v1/api-docs/**",
                    "/webjars/**"
                ).permitAll() // Permitir Swagger sin autenticación

                it.requestMatchers("/api/v1/register").permitAll()
                it.requestMatchers("/api/v1/accountInfo").permitAll()
                it.requestMatchers("/api/v1/test/hello").permitAll()
                it.requestMatchers("/api/v1/addRegistry").hasRole("ADMIN")
                it.requestMatchers("/api-keys/**").hasRole("ADMIN")
                it.anyRequest().authenticated() // Todas las demás rutas requieren autenticación
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // Sin sesiones
            .addFilterBefore(ApiKeyAuthFilter(apiKeyService), UsernamePasswordAuthenticationFilter::class.java) // Agregar después de `permitAll()`

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("*")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}

