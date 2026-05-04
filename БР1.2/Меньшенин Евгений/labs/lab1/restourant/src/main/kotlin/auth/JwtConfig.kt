package com.mezhendosina.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {
    private const val SECRET = "secret"
    private const val EXPIRATION_MS = 300_000L
    private const val ISSUER = "restaurant-booking-api"
    const val REALM = "Restaurant Booking API"

    fun generateToken(userId: Int): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + EXPIRATION_MS))
            .sign(Algorithm.HMAC256(SECRET))
    }

    fun verifyToken(token: String): Int? {
        return try {
            val verifier = JWT.require(Algorithm.HMAC256(SECRET))
                .withIssuer(ISSUER)
                .build()
            val decoded = verifier.verify(token)
            decoded.getClaim("userId").asInt()
        } catch (e: Exception) {
            null
        }
    }
}
