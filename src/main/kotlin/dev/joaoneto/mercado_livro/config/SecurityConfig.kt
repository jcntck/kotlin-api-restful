package dev.joaoneto.mercado_livro.config

import dev.joaoneto.mercado_livro.repository.CustomerRepository
import dev.joaoneto.mercado_livro.security.AuthenticationFilter
import dev.joaoneto.mercado_livro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val userDetailsCustomService: UserDetailsCustomService
) {

    private val PUBLIC_POST_MATCHERS = arrayOf(
        "/customers",
        "/login"
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
                .anyRequest().authenticated()
        }
        http.sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        http.addFilter(AuthenticationFilter(authenticationManager, customerRepository))
        return http.build()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}