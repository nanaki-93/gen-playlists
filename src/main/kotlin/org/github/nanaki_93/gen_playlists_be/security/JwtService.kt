package org.github.nanaki_93.gen_playlists_be.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.github.nanaki_93.gen_playlists_be.model.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
    @param:Value("\${jwt.secret}") private val secretKey: String,
    @param:Value("\${jwt.expiration}") private val jwtExpiration: Long
) {

    fun extractUsername(token: String): String =
        extractClaim(token) { it.subject }

    fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun generateToken(user: User): String =
        generateToken(emptyMap(), user)

    fun generateToken(
        extraClaims: Map<String, Any>,
        user: User
    ): String =
        Jwts.builder()
            .claims(extraClaims)
            .subject(user.email)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(signInKey)
            .compact()

    fun isTokenValid(token: String, user: UserDetails): Boolean {
        val username = extractUsername(token)
        return (username == user.username) && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean =
        extractExpiration(token).before(Date())

    private fun extractExpiration(token: String): Date =
        extractClaim(token) { it.expiration }

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(signInKey)
            .build()
            .parseSignedClaims(token)
            .payload

    private val signInKey: SecretKey
        get() {
            val keyBytes = Decoders.BASE64.decode(secretKey)
            return Keys.hmacShaKeyFor(keyBytes)
        }
}