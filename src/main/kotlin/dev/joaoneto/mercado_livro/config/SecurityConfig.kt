package dev.joaoneto.mercado_livro.config

import dev.joaoneto.mercado_livro.enums.Role
import dev.joaoneto.mercado_livro.repository.CustomerRepository
import dev.joaoneto.mercado_livro.security.AuthenticationFilter
import dev.joaoneto.mercado_livro.security.AuthorizationFilter
import dev.joaoneto.mercado_livro.security.CustomAuthenticationEntryPoint
import dev.joaoneto.mercado_livro.security.JwtUtil
import dev.joaoneto.mercado_livro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val userDetailsCustomService: UserDetailsCustomService,
    private val jwtUtil: JwtUtil,
    private val customEntryPoint: CustomAuthenticationEntryPoint
) {

    private val PUBLIC_POST_MATCHERS = arrayOf(
        "/customers",
        "/login",
    )

    private val SWAGGER_MATCHERS = arrayOf(
        "/v3/api-docs/**",
        "/swagger-ui/**"
    )

    private val ADMIN_MATCHERS = arrayOf(
        "/admin/**"
    )


    @Bean
    fun myAuthenticationManager(): AuthenticationManager {
        val provider = DaoAuthenticationProvider()
        provider.setUserDetailsService(userDetailsCustomService)
        provider.setPasswordEncoder(bCryptPasswordEncoder())
        return AuthenticationManager { authentication: Authentication? ->
            provider.authenticate(
                authentication
            )
        }
    }

    @Bean
    fun configure(http: HttpSecurity, authenticationManager: AuthenticationManager): SecurityFilterChain {
        http.cors { it.disable() }
        http.csrf { it.disable() }
        http.authorizeHttpRequests {
            it
                .requestMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()
                .requestMatchers(*SWAGGER_MATCHERS).permitAll()
                .requestMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description)
                .anyRequest().authenticated()
        }
        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.addFilter(AuthenticationFilter(authenticationManager, customerRepository, jwtUtil))
        http.addFilter(AuthorizationFilter(authenticationManager, userDetailsCustomService, jwtUtil))
        http.exceptionHandling { it.authenticationEntryPoint(customEntryPoint) }
        return http.build()
    }

    @Bean
    fun corsConfig(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}