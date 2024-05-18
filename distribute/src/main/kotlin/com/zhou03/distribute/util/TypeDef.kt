package com.zhou03.distribute.util

import com.google.gson.Gson
import com.zhou03.distribute.model.Token
import jakarta.servlet.http.HttpServletRequest
import java.util.*

fun HttpServletRequest.getToken() = this.getAttribute("token") as Token

fun HttpServletRequest.setToken(token: Token) = this.setAttribute("token", token)

inline fun <reified T> fromJson(msg: String) = Gson().fromJson<T>(msg, T::class.java)

fun toJson(value: Any) = Gson().toJson(value)

fun uuid() = UUID.randomUUID().toString().replace("-", "")