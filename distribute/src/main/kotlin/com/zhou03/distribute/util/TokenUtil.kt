package com.zhou03.distribute.util

import com.zhou03.distribute.model.Token
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwsHeader
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SecureDigestAlgorithm
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

object TokenUtil {

    private const val ACCESS_EXPIRE: Int = 24 * 60 * 60

    private val ALGORITHM: SecureDigestAlgorithm<SecretKey, SecretKey> = Jwts.SIG.HS256

    private const val SECRET = "20240516202405162024051620240516"

    private val key: SecretKey = Keys.hmacShaKeyFor(SECRET.toMd5().toByteArray())

    private const val JWT_ISS = "distribute"

    const val SUBJECT = "Authorization"

    fun genAccessToken(token: Token): String {
        val uuid = UUID.randomUUID().toString()
        val expirationDate = Date.from(Instant.now().plusSeconds(ACCESS_EXPIRE.toLong()))
        return Jwts.builder().header().add("typ", "JWT").add("alg", "HS256").and().claim("token", toJson(token))
            .id(uuid).expiration(expirationDate).issuedAt(Date()).subject(SUBJECT).issuer(JWT_ISS)
            .signWith(key, ALGORITHM).compact()
    }

    fun genAccessToken(arg1: String, arg2: Any, expire: Int = ACCESS_EXPIRE): String {
        val uuid = UUID.randomUUID().toString()
        val expirationDate = Date.from(Instant.now().plusSeconds(expire.toLong()))
        return Jwts.builder().header().add("typ", "JWT").add("alg", "HS256").and().claim(arg1, toJson(arg2)).id(uuid)
            .expiration(expirationDate).issuedAt(Date()).subject(SUBJECT).issuer(JWT_ISS).signWith(key, ALGORITHM)
            .compact()
    }

    private fun parseClaim(token: String?): Jws<Claims> {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token)
    }

    fun parseHeader(token: String?): JwsHeader {
        return parseClaim(token).header
    }

    fun parsePayload(token: String?): Claims {
        return parseClaim(token).payload
    }
}

fun String.refreshToken(): String {
    try {
        val claims = TokenUtil.parsePayload(this)
        val token = claims.getToken() ?: return this
        return TokenUtil.genAccessToken(token)
    } catch (_: Exception) {
    }
    return this
}

fun Claims.getToken(): Token? = getAndTrans<Token>("token")

inline fun <reified T> Claims.getAndTrans(key: String): T? {
    return try {
        fromJson<T>(this[key, String::class.java])
    } catch (_: Exception) {
        null
    }
}