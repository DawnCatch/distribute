package com.zhou03.distribute.util

import com.zhou03.distribute.domain.Device
import com.zhou03.distribute.domain.User
import jakarta.servlet.http.Cookie

fun cookie(key: String, value: String, age: Int) = Cookie(key, value.aesEncrypt()).apply {
    path = "/"
    maxAge = age * 24 * 60 * 60
}

fun token(user: User) = cookie("token", "${user.username}:${user.password}", 7)

fun device(device: Device) = cookie("device", device.code, 30)