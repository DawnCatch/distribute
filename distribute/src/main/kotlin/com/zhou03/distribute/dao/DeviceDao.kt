package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.Device
import com.zhou03.distribute.domain.Devices
import com.zhou03.distribute.util.aesDecrypt
import jakarta.servlet.http.Cookie
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.springframework.stereotype.Component

@Component
class DeviceDao : BaseDao<Device, Devices>(Devices) {

    fun check(cookies: Array<Cookie>?): Device? {
        if (cookies == null) return null
        cookies.forEach {
            if (it.name == "device" && it.value != "") {
                val value = it.value.aesDecrypt()
                val device = getByCode(value)
                if (device != null) return device
            }
        }
        return null
    }

    fun check(code: String) = findOne { (it.code eq code) and (it.status eq true) }

    fun getByCode(code: String) = findOne { (it.code eq code) and (it.status eq true) }

    fun getById(id: Int) = findOne { (it.id eq id) and (it.status eq true) }

    fun getOwnById(id: Int, userId: Int) = findOne { (it.id eq id) and (it.userId eq userId) and (it.status eq true) }

    fun listOwn(userId: Int) = findList { (it.userId eq userId) and (it.status eq true) }
}