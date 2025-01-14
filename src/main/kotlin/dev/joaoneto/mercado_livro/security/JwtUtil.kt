package dev.joaoneto.mercado_livro.security

import dev.joaoneto.mercado_livro.exception.AuthenticationException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Component
class JwtUtil {

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    @Value("\${jwt.secret}")
    private val secret: String? = null

    private fun getSecretKey(): SecretKey {
        val digest = MessageDigest.getInstance("SHA-512")
        val hashedKey = digest.digest(secret!!.toByteArray())
        return SecretKeySpec(hashedKey, "HmacSHA512")
    }

    fun generateToken(id: Int): String {
        val secretKey = getSecretKey()
        return Jwts.builder()
            .subject(id.toString())
            .expiration(Date(System.currentTimeMillis() + expiration!!))
            .signWith(secretKey)
            .compact()
    }

    fun isValidToken(token: String): Boolean {
        val claims = getClaims(token)
        if (claims.subject == null || claims.expiration == null || Date().after(claims.expiration)) return false
        return true
    }

    private fun getClaims(token: String): Claims {
        try {
            return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).payload
        } catch (ex: Exception) {
            throw AuthenticationException("Token inválido", "999")
        }
    }

    fun getSubject(token: String): String {
        return getClaims(token).subject
    }
}