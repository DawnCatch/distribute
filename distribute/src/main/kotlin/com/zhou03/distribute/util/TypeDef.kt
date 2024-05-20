package com.zhou03.distribute.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.zhou03.distribute.adapter.LocalDateTimeTypeAdapter
import com.zhou03.distribute.model.Token
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.socket.WebSocketSession
import java.lang.reflect.Type
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*


fun HttpServletRequest.getToken() = this.getAttribute("token") as Token

fun HttpServletRequest.setToken(token: Token) = this.setAttribute("token", token)

fun WebSocketSession.getToken() = this.attributes["token"] as Token

fun ServerHttpRequest.getHeader(key: String): String {
    val values = this.headers[key]
    return if (values.isNullOrEmpty()) ""
    else values.first()
}

fun ServerHttpResponse.setHeader(key: String, value: String) {
    this.headers[key] = listOf(value)
}

fun gsonCreate(): Gson =
    GsonBuilder().registerTypeHierarchyAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter()).create()

inline fun <reified T> fromJson(msg: String): T = gsonCreate().fromJson<T>(msg, T::class.java)

inline fun <reified T> fromJson(json: String, typeToken: Type): T {
    return gsonCreate().fromJson<T>(json, typeToken)
}

fun toJson(value: Any): String = gsonCreate().toJson(value)

fun uuid() = UUID.randomUUID().toString().replace("-", "")

fun String.toLocalDateTime(): LocalDateTime {
    return try {
        LocalDateTime.parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
    } catch (e: DateTimeParseException) {
        LocalDateTime.now()
    }
}

fun LocalDateTime.toString(value: LocalDateTime): String =
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(value)

fun LocalDateTime.toMilliSecond() = this.toInstant(ZoneOffset.of("+8")).toEpochMilli()

fun Long.toLocalDateTime(): LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.of("+8"))