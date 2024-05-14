package com.zhou03.distribute.util

import com.google.gson.Gson
import com.zhou03.distribute.domain.User
import jakarta.servlet.http.HttpServletRequest
import java.util.*

fun HttpServletRequest.getUser() = this.getAttribute("user") as User
fun HttpServletRequest.setUser(user: User) = this.setAttribute("user", user)

inline fun <reified T> fromJson(msg: String) = Gson().fromJson<T>(msg, T::class.java)

fun toJson(value: Any) = Gson().toJson(value)

fun uuid() = UUID.randomUUID().toString().replace("-", "")