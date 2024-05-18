package com.zhou03.distribute.dao

import com.zhou03.distribute.domain.User
import com.zhou03.distribute.domain.Users
import com.zhou03.distribute.util.aesDecrypt
import com.zhou03.phase.dao.BaseDao
import jakarta.servlet.http.Cookie
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.or
import org.ktorm.dsl.update
import org.springframework.stereotype.Component

@Component
class UserDao : BaseDao<User, Users>(Users) {

    fun check(cookies: Array<Cookie>?): User? {
        if (cookies == null) return null
        cookies.forEach {
            if (it.name == "token" && it.value != "") {
                val token = it.value.aesDecrypt().split(":")
                if (token.size == 2) {
                    val user = check(token[0], token[1])
                    if (user != null) return user
                }
            }
        }
        return null
    }

    fun getById(id: Int) = findOne { it.id eq id }

    fun check(username: String, password: String): User? {
        return findOne { (it.username eq username) or (it.email eq username) and (it.password eq password) }
    }

    fun countUsernameByValue(username: String) = count { (it.username eq username) }

    fun countEmailByValue(email: String) = count { (it.email eq email) }

    fun changePassword(email: String, newPassword: String): Boolean {
        val updatedRows = database.update(Users) {
            set(it.password, newPassword)
            where { it.email eq email }
        }
        return updatedRows > 0
    }

    fun emailIsEmpty(id: Int) = count { (it.id eq id) and (it.email eq "") } != 0

    fun setEmail(id: Int, email: String) {
        database.update(Users) {
            set(it.email, email)
            where { (it.email eq "") }
        }
    }
}